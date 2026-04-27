package ru.topbun.profile_followers

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.useCases.account.GetProfileUseCase
import ru.topbun.domain.useCases.follow.GetFollowersUseCase
import ru.topbun.domain.useCases.follow.GetFollowingUseCase
import ru.topbun.navigation.ProfileScreenProvider

internal class FollowListViewModel(
    userId: Int,
    initialTab: ProfileScreenProvider.FollowsTab,
    private val getProfileUseCase: GetProfileUseCase,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getFollowingUseCase: GetFollowingUseCase,
    private val snackbarManager: SnackbarManager,
) : MVI<FollowListIntent, FollowListState, FollowListEvent>(
    FollowListState(userId = userId, selectedTab = initialTab)
) {

    private var profileJob: Job? = null
    private var followersJob: Job? = null
    private var followingJob: Job? = null

    private fun loadProfile() {
        profileJob?.cancel()
        profileJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(profileStatus = ScreenUiState.Loading) }
            getProfileUseCase(state.value.userId).onSuccess { profile ->
                _state.update {
                    it.copy(
                        targetProfile = profile,
                        profileStatus = ScreenUiState.Success
                    )
                }
                loadActiveTab()
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update { it.copy(profileStatus = ScreenUiState.Error) }
            }
        }
    }

    private fun refresh() {
        profileJob?.cancel()
        followersJob?.cancel()
        followingJob?.cancel()
        _state.update {
            it.copy(
                followersList = FollowListState.ListUiState(),
                followingList = FollowListState.ListUiState(),
            )
        }
        loadProfile()
    }

    private fun changeTab(tab: ProfileScreenProvider.FollowsTab) {
        _state.update { it.copy(selectedTab = tab) }
        val list = when (tab) {
            ProfileScreenProvider.FollowsTab.Followers -> state.value.followersList
            ProfileScreenProvider.FollowsTab.Following -> state.value.followingList
        }
        if (list.status == ScreenUiState.Idle) loadActiveTab()
    }

    private fun loadActiveTab() {
        when (state.value.selectedTab) {
            ProfileScreenProvider.FollowsTab.Followers -> loadFollowers()
            ProfileScreenProvider.FollowsTab.Following -> loadFollowing()
        }
    }

    private fun loadFollowers() {
        val current = state.value
        val list = current.followersList
        if (list.status.isLoading || list.isEndList) return

        followersJob?.cancel()
        followersJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(followersList = it.followersList.copy(status = ScreenUiState.Loading)) }
            getFollowersUseCase(
                userId = current.userId,
                limit = PAGE_SIZE,
                offset = list.users.size
            ).onSuccess { users ->
                _state.update { state ->
                    val merged = (state.followersList.users + users).distinctBy { it.userId }
                    state.copy(
                        followersList = state.followersList.copy(
                            users = merged,
                            status = ScreenUiState.Success,
                            isEndList = users.isEmpty()
                        )
                    )
                }
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update { it.copy(followersList = it.followersList.copy(status = ScreenUiState.Error)) }
            }
        }
    }

    private fun loadFollowing() {
        val current = state.value
        val list = current.followingList
        if (list.status.isLoading || list.isEndList) return

        followingJob?.cancel()
        followingJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(followingList = it.followingList.copy(status = ScreenUiState.Loading)) }
            getFollowingUseCase(
                userId = current.userId,
                limit = PAGE_SIZE,
                offset = list.users.size
            ).onSuccess { users ->
                _state.update { state ->
                    val merged = (state.followingList.users + users).distinctBy { it.userId }
                    state.copy(
                        followingList = state.followingList.copy(
                            users = merged,
                            status = ScreenUiState.Success,
                            isEndList = users.isEmpty()
                        )
                    )
                }
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update { it.copy(followingList = it.followingList.copy(status = ScreenUiState.Error)) }
            }
        }
    }

    override suspend fun handleIntent(intent: FollowListIntent) {
        when (intent) {
            FollowListIntent.LoadProfile -> loadProfile()
            FollowListIntent.Refresh -> refresh()
            FollowListIntent.LoadFollowers -> loadFollowers()
            FollowListIntent.LoadFollowing -> loadFollowing()
            is FollowListIntent.ChangeTab -> changeTab(intent.tab)
        }
    }

    private fun DataError.toMessage(): String = when (this) {
        DataError.Network.UNAUTHORIZED -> "Пользователь не авторизован"
        DataError.Network.BAD_REQUEST -> "Не удалось загрузить список"
        DataError.Network.INVALID_DATA -> "Проверьте корректность данных"
        DataError.Network.NOT_FOUND -> "Пользователь не найден"
        DataError.Network.FORBIDDEN -> "Нет доступа"
        DataError.Network.REQUEST_TIMEOUT -> "Время ожидания превышено. Проверьте интернет-соединение или попробуйте позже"
        DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
        DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
        DataError.Network.NO_INTERNET -> "Отсутствует интернет-соединение"
        else -> "Произошла ошибка. Попробуйте позже"
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

}
