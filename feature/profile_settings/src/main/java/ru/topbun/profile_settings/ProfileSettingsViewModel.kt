package ru.topbun.profile_settings

import android.net.Uri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.account.UpdateAccountInfoEntity
import ru.topbun.domain.useCases.account.GetAccountInfoUseCase
import ru.topbun.domain.useCases.account.UpdateAccountInfoUseCase
import ru.topbun.domain.useCases.upload.UploadFileUseCase
import ru.topbun.domain.validation.account.UpdateAccountInfoValidator
import ru.topbun.domain.validation.account.UpdateAccountInfoValidatorError

internal class ProfileSettingsViewModel(
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val updateAccountInfoUseCase: UpdateAccountInfoUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
    private val updateAccountInfoValidator: UpdateAccountInfoValidator,
    private val snackbarManager: SnackbarManager,
) : MVI<ProfileSettingsIntent, ProfileSettingsState, ProfileSettingsEvent>(ProfileSettingsState()) {

    private var loadJob: Job? = null
    private var saveJob: Job? = null

    private fun changeUsername(value: String) {
        if (value.length <= MAX_USERNAME_LENGTH) {
            _state.update { it.copy(username = value) }
        }
    }

    private fun changePhoto(uri: Uri?) {
        _state.update {
            it.copy(
                photoUri = uri,
                isPhotoChanged = true
            )
        }
    }

    private fun clearPhoto() {
        _state.update {
            it.copy(
                photoUri = null,
                photoUrl = null,
                isPhotoChanged = true
            )
        }
    }

    private fun loadAccount() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.update { it.copy(accountStatus = ScreenUiState.Loading) }
            getAccountInfoUseCase().onSuccess { account ->
                _state.update {
                    it.copy(
                        accountStatus = ScreenUiState.Success,
                        account = account,
                        username = account.username,
                        photoUrl = account.photoUrl,
                        photoUri = null,
                        isPhotoChanged = false
                    )
                }
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update { it.copy(accountStatus = ScreenUiState.Error) }
            }
        }
    }

    private suspend fun uploadPhoto(uri: Uri): String? {
        var url: String? = null
        uploadFileUseCase(uri.toString()).onSuccess { url = it }.onError { error, _ ->
            val message = when (error) {
                DataError.Network.UNAUTHORIZED -> "Пользователь не авторизован"
                DataError.Network.INVALID_DATA -> "Файл превышает размер 8 мб, либо не верный формат файла"
                DataError.Network.REQUEST_TIMEOUT -> "Время ожидания превышено. Проверьте интернет-соединение или попробуйте позже"
                DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
                DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
                DataError.Network.NO_INTERNET -> "Отсутствует интернет-соединение"
                else -> "Произошла ошибка. Попробуйте позже"
            }
            snackbarManager.showMessage(message)
        }
        return url
    }

    private fun save() = with(state.value) {
        if (saveStatus.isLoading) return@with

        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            _state.update { it.copy(saveStatus = ScreenUiState.Loading) }

            val newPhotoUrl = when {
                photoUri != null -> uploadPhoto(photoUri) ?: run {
                    _state.update { it.copy(saveStatus = ScreenUiState.Error) }
                    return@launch
                }
                isPhotoChanged && photoUri == null -> null
                else -> photoUrl
            }

            val data = UpdateAccountInfoEntity(
                username = username.trim(),
                photoUrl = newPhotoUrl
            )

            val validation = updateAccountInfoValidator.validate(data)
            validation.onError { error, _ ->
                val message = when (error) {
                    UpdateAccountInfoValidatorError.USERNAME_SHORT -> "Имя должно быть не короче 4 символов"
                }
                snackbarManager.showMessage(message)
                _state.update { it.copy(saveStatus = ScreenUiState.Error) }
                return@launch
            }

            updateAccountInfoUseCase(data).onSuccess { account ->
                _state.update {
                    it.copy(
                        account = account,
                        username = account.username,
                        photoUrl = account.photoUrl,
                        photoUri = null,
                        isPhotoChanged = false,
                        saveStatus = ScreenUiState.Success
                    )
                }
                _events.send(ProfileSettingsEvent.Saved)
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update { it.copy(saveStatus = ScreenUiState.Error) }
            }
        }
    }

    override suspend fun handleIntent(intent: ProfileSettingsIntent) {
        when (intent) {
            ProfileSettingsIntent.LoadAccount -> loadAccount()
            ProfileSettingsIntent.Save -> save()
            ProfileSettingsIntent.ClearPhoto -> clearPhoto()
            is ProfileSettingsIntent.ChangeUsername -> changeUsername(intent.value)
            is ProfileSettingsIntent.ChangePhoto -> changePhoto(intent.uri)
        }
    }

    private fun DataError.toMessage(): String = when (this) {
        DataError.Network.UNAUTHORIZED -> "Пользователь не авторизован"
        DataError.Network.BAD_REQUEST -> "Произошла ошибка при сохранении"
        DataError.Network.INVALID_DATA -> "Проверьте корректность введённых данных"
        DataError.Network.REQUEST_TIMEOUT -> "Время ожидания превышено. Проверьте интернет-соединение или попробуйте позже"
        DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
        DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
        DataError.Network.NO_INTERNET -> "Отсутствует интернет-соединение"
        else -> "Произошла ошибка. Попробуйте позже"
    }

    companion object {
        private const val MAX_USERNAME_LENGTH = 32
    }
}
