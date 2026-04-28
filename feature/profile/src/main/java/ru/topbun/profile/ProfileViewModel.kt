package ru.topbun.profile

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
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeByUserIdEntity
import ru.topbun.domain.useCases.account.GetAccountInfoUseCase
import ru.topbun.domain.useCases.account.GetProfileUseCase
import ru.topbun.domain.useCases.favorite.GetCachedFavoriteRecipesUseCase
import ru.topbun.domain.useCases.favorite.GetFavoriteRecipesUseCase
import ru.topbun.domain.useCases.follow.SwitchFollowUserUseCase
import ru.topbun.domain.useCases.recipe.GetRecipeByUserIdUseCase
import ru.topbun.domain.useCases.session.HasSessionUseCase
import ru.topbun.domain.useCases.session.LogoutUseCase

internal class ProfileViewModel(
    mode: ProfileState.Mode,
    private val hasSessionUseCase: HasSessionUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val getRecipeByUserIdUseCase: GetRecipeByUserIdUseCase,
    private val getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase,
    private val getCachedFavoriteRecipesUseCase: GetCachedFavoriteRecipesUseCase,
    private val switchFollowUserUseCase: SwitchFollowUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val snackbarManager: SnackbarManager,
) : MVI<ProfileIntent, ProfileState, ProfileEvent>(ProfileState(mode = mode)) {

    private var profileJob: Job? = null
    private var recipesJob: Job? = null
    private var likedJob: Job? = null
    private var followJob: Job? = null

    private fun changeShowSettingsDialog(value: Boolean) =
        _state.update { it.copy(showSettingsDialog = value) }

    private fun changeShowLogoutDialog(value: Boolean) =
        _state.update { it.copy(showLogoutDialog = value) }

    private fun changeTab(tab: ProfileState.ProfileTab) {
        _state.update { it.copy(selectedTab = tab) }
        val list = when (tab) {
            ProfileState.ProfileTab.MyRecipes -> state.value.recipeList
            ProfileState.ProfileTab.Liked -> state.value.likedList
        }
        if (list.status == ScreenUiState.Idle) loadActiveTab()
    }

    private fun loadActiveTab() {
        when (state.value.selectedTab) {
            ProfileState.ProfileTab.MyRecipes -> loadRecipes()
            ProfileState.ProfileTab.Liked -> loadLiked()
        }
    }

    private fun checkSession() {
        if (state.value.mode !is ProfileState.Mode.Self) {
            _state.update { it.copy(profileUiState = ProfileState.ProfileUiState.SUCCESS) }
            loadProfile()
            return
        }
        val hasSession = hasSessionUseCase()
        val uiState = if (hasSession) {
            ProfileState.ProfileUiState.SUCCESS
        } else {
            ProfileState.ProfileUiState.NEED_AUTH
        }
        _state.update { it.copy(profileUiState = uiState) }
        if (hasSession && state.value.profileStatus == ScreenUiState.Idle) {
            loadProfile()
        }
    }

    private fun loadProfile() {
        profileJob?.cancel()
        profileJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(profileStatus = ScreenUiState.Loading) }

            when (val mode = state.value.mode) {
                is ProfileState.Mode.Self -> {
                    getAccountInfoUseCase().onSuccess { account ->
                        _state.update { it.copy(account = account) }
                        getProfileUseCase(account.id).onSuccess { profile ->
                            _state.update {
                                it.copy(
                                    profile = profile,
                                    profileStatus = ScreenUiState.Success
                                )
                            }
                            loadActiveTab()
                        }.onError { error, _ ->
                            handleProfileError(error)
                            loadActiveTab()
                        }
                    }.onError { error, _ ->
                        if (error == DataError.Network.UNAUTHORIZED) {
                            _state.update {
                                it.copy(
                                    profileUiState = ProfileState.ProfileUiState.NEED_AUTH,
                                    profileStatus = ScreenUiState.Idle
                                )
                            }
                        } else {
                            handleProfileError(error)
                        }
                    }
                }

                is ProfileState.Mode.Other -> {
                    getProfileUseCase(mode.userId).onSuccess { profile ->
                        _state.update {
                            it.copy(
                                profile = profile,
                                profileStatus = ScreenUiState.Success
                            )
                        }
                        loadActiveTab()
                    }.onError { error, _ ->
                        handleProfileError(error)
                        loadActiveTab()
                    }
                }
            }
        }
    }

    private fun handleProfileError(error: DataError) {
        _state.update { it.copy(profileStatus = ScreenUiState.Error) }
    }

    private fun refresh() {
        profileJob?.cancel()
        recipesJob?.cancel()
        likedJob?.cancel()
        _state.update {
            it.copy(
                recipeList = ProfileState.RecipeListUiState(),
                likedList = ProfileState.RecipeListUiState(),
            )
        }
        loadProfile()
    }

    private fun loadRecipes() {
        val current = state.value
        val userId = current.targetUserId ?: return
        val list = current.recipeList
        if (list.status.isLoading || list.isEndList) return

        recipesJob?.cancel()
        recipesJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(recipeList = it.recipeList.copy(status = ScreenUiState.Loading)) }
            val data = GetRecipeByUserIdEntity(offset = _state.value.recipeList.recipes.size)
            getRecipeByUserIdUseCase(userId, data).onSuccess { recipes ->
                _state.update { current ->
                    val merged = (current.recipeList.recipes + recipes).distinctBy { it.id }
                    current.copy(
                        recipeList = current.recipeList.copy(
                            recipes = merged,
                            status = ScreenUiState.Success,
                            isEndList = recipes.isEmpty()
                        )
                    )
                }
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update { it.copy(recipeList = it.recipeList.copy(status = ScreenUiState.Error)) }
            }
        }
    }

    private fun loadLiked() {
        val current = state.value
        val userId = current.targetUserId ?: return
        val list = current.likedList
        if (list.status.isLoading || list.isEndList) return

        likedJob?.cancel()
        likedJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(likedList = it.likedList.copy(status = ScreenUiState.Loading)) }
            getFavoriteRecipesUseCase(
                userId = userId,
                limit = LIKED_PAGE_SIZE,
                offset = list.recipes.size
            ).onSuccess { recipes ->
                _state.update { current ->
                    val merged = (current.likedList.recipes + recipes).distinctBy { it.id }
                    current.copy(
                        likedList = current.likedList.copy(
                            recipes = merged,
                            status = ScreenUiState.Success,
                            isEndList = recipes.isEmpty(),
                            isFromCache = false,
                        )
                    )
                }
            }.onError { error, _ ->
                fallbackLikedFromCache(userId, error)
            }
        }
    }

    private suspend fun fallbackLikedFromCache(userId: Int, error: DataError) {
        val current = state.value.likedList
        if (current.recipes.isNotEmpty()) {
            snackbarManager.showMessage(error.toMessage())
            _state.update { it.copy(likedList = it.likedList.copy(status = ScreenUiState.Error)) }
            return
        }
        val cached = getCachedFavoriteRecipesUseCase(userId)
        if (cached.isNotEmpty()) {
            _state.update {
                it.copy(
                    likedList = it.likedList.copy(
                        recipes = cached,
                        status = ScreenUiState.Success,
                        isEndList = true,
                        isFromCache = true,
                    )
                )
            }
            snackbarManager.showMessage("Показаны сохранённые рецепты — нет связи с сервером")
        } else {
            snackbarManager.showMessage(error.toMessage())
            _state.update { it.copy(likedList = it.likedList.copy(status = ScreenUiState.Error)) }
        }
    }

    private fun switchFollow() {
        val current = state.value
        val mode = current.mode
        if (mode !is ProfileState.Mode.Other) return
        val profile = current.profile ?: return
        if (current.followLoading) return

        val previousIsFollow = profile.isFollow
        val previousFollowers = profile.countFollowers
        val optimistic = profile.copy(
            isFollow = !previousIsFollow,
            countFollowers = previousFollowers + if (previousIsFollow) -1 else 1
        )

        followJob?.cancel()
        followJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(profile = optimistic, followLoading = true) }
            switchFollowUserUseCase(mode.userId).onSuccess { isFollow ->
                _state.update {
                    val delta = when {
                        isFollow == previousIsFollow -> 0
                        isFollow -> 1
                        else -> -1
                    }
                    val finalProfile = it.profile?.copy(
                        isFollow = isFollow,
                        countFollowers = previousFollowers + delta
                    ) ?: it.profile
                    it.copy(profile = finalProfile, followLoading = false)
                }
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update {
                    it.copy(
                        profile = profile,
                        followLoading = false
                    )
                }
            }
        }
    }

    private fun logout() {
        logoutUseCase()
        _state.update {
            ProfileState(
                mode = it.mode,
                profileUiState = ProfileState.ProfileUiState.NEED_AUTH
            )
        }
        viewModelScope.launch { _events.send(ProfileEvent.LoggedOut) }
    }

    override suspend fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.CheckSession -> checkSession()
            ProfileIntent.LoadProfile -> loadProfile()
            ProfileIntent.Refresh -> refresh()
            ProfileIntent.LoadRecipes -> loadRecipes()
            ProfileIntent.LoadLiked -> loadLiked()
            ProfileIntent.SwitchFollow -> switchFollow()
            ProfileIntent.Logout -> logout()
            is ProfileIntent.ChangeTab -> changeTab(intent.tab)
            is ProfileIntent.ChangeShowSettingsDialog -> changeShowSettingsDialog(intent.value)
            is ProfileIntent.ChangeShowLogoutDialog -> changeShowLogoutDialog(intent.value)
        }
    }

    private fun DataError.toMessage(): String = when (this) {
        DataError.Network.UNAUTHORIZED -> "Пользователь не авторизован"
        DataError.Network.BAD_REQUEST -> "Произошла ошибка при загрузке профиля"
        DataError.Network.INVALID_DATA -> "Проверьте корректность введённых данных"
        DataError.Network.NOT_FOUND -> "Профиль не найден"
        DataError.Network.FORBIDDEN -> "Нет доступа к профилю"
        DataError.Network.REQUEST_TIMEOUT -> "Время ожидания превышено. Проверьте интернет-соединение или попробуйте позже"
        DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
        DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
        DataError.Network.NO_INTERNET -> "Отсутствует интернет-соединение"
        else -> "Произошла ошибка. Попробуйте позже"
    }

    companion object {
        private const val LIKED_PAGE_SIZE = 20
    }
}
