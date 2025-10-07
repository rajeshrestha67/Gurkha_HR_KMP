package com.gurkha.hr.profile.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.profile_screen.ProfileScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    private val userDetailUseCase: FetchUserDetailUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileScreenState())
    val state = _state
        .onStart {
            fetchUserDetails()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileScreenState()
        )

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
                    userProfileUrl = data.userProfileUrl,
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