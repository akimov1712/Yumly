package ru.topbun.profile_settings

import android.net.Uri
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.account.UserEntity

internal data class ProfileSettingsState(
    val accountStatus: ScreenUiState = ScreenUiState.Idle,
    val account: UserEntity? = null,
    val username: String = "",
    val photoUri: Uri? = null,
    val photoUrl: String? = null,
    val isPhotoChanged: Boolean = false,
    val saveStatus: ScreenUiState = ScreenUiState.Idle,
) {

    val canSave: Boolean
        get() = !saveStatus.isLoading &&
                !accountStatus.isLoading &&
                username.isNotBlank() &&
                hasChanges

    val hasChanges: Boolean
        get() {
            val accountUsername = account?.username.orEmpty()
            val accountPhoto = account?.photoUrl
            return username != accountUsername || isPhotoChanged || accountPhoto != photoUrl
        }

}

internal sealed interface ProfileSettingsIntent {

    data object LoadAccount : ProfileSettingsIntent
    data object Save : ProfileSettingsIntent
    data object ClearPhoto : ProfileSettingsIntent
    data class ChangeUsername(val value: String) : ProfileSettingsIntent
    data class ChangePhoto(val uri: Uri?) : ProfileSettingsIntent

}

internal sealed interface ProfileSettingsEvent {

    data object Saved : ProfileSettingsEvent

}
