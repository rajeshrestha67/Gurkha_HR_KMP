package com.gurkha.hr.profile.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.permissions.ProgressNotification
import com.gurkha.hr.domain.uploadImage.UploadImageUseCase
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.profile_screen.ProfileScreenState
import com.gurkha.hr.profile.profile_screen.model.ProfileScreenAction
import com.gurkha.model.uploadImage.ImageUpdateDocumentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ProfileScreenViewModel(
    private val userDetailUseCase: FetchUserDetailUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileScreenState())
    private val notification = ProgressNotification()

    val state = _state
        .onStart {
            fetchUserDetails()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileScreenState()
        )

    fun onAction(action: ProfileScreenAction) {
        when (action) {
            is ProfileScreenAction.OnProfileImageReceived -> {
                uploadImage(
                    uri = action.url
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun uploadImage(uri: String) = viewModelScope.launch {
        _state.update {
            it.copy(
                userProfileUrl = uri
            )
        }
        //notification.preloadImage(uri)

        uploadImageUseCase(
            filePath = uri,
            imageName = "image${Clock.System.now().toEpochMilliseconds()}",
            type = ImageUpdateDocumentType.PROFILE_IMAGE.key,
            onProgress = { progress ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main.immediate) {
                        notification.showNotification(
                            progress = progress
                        )
                    }
                }
            }
        ).onSuccess { data ->
            data.a
        }
    }

    private fun fetchUserDetails() = viewModelScope.launch {
        _state.update {
            it.copy(
                isProfileLoading = true
            )
        }
        userDetailUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isProfileLoading = false,
                    fullName = data.fullName,
                    levelName = data.levelName,
                    userProfileUrl = data.imageUrl,
                    phoneNumber = data.phoneNumber,
                    initials = data.initials
                )
            }
        }.onError {
            _state.update {
                it.copy(
                    isProfileLoading = false
                )
            }

        }

    }

}
