package com.gurkha.hr.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.changePassword.usecase.ChangePasswordUseCase
import com.gurkha.hr.domain.form.PasswordValidateUseCase
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.settings.model.changePassword.ChangePasswordScreenAction
import com.gurkha.hr.settings.model.changePassword.ChangePasswordScreenState
import com.gurkha.model.network.toErrorMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val newPasswordValidateUseCase: PasswordValidateUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(value = ChangePasswordScreenState())

    private val _errorChannel = Channel<String>()

    val errorChannel = _errorChannel.receiveAsFlow()

    private val _successChannel = Channel<Boolean>()

    val successChannel = _successChannel.receiveAsFlow()

    val state = _state
        .stateIn(
            viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = ChangePasswordScreenState()
        )

    fun onAction(action: ChangePasswordScreenAction) {
        when (action) {
            is ChangePasswordScreenAction.OnConfirmPasswordError -> {
                _state.update {
                    it.copy(confirmPasswordError = action.confirmPasswordError)
                }
            }

            is ChangePasswordScreenAction.OnNewPasswordChanged -> {
                _state.update {
                    it.copy(newPassword = action.newPassword)
                }
            }

            is ChangePasswordScreenAction.OnNewPasswordError -> {
                _state.update {
                    it.copy(newPasswordError = action.newPasswordError)
                }
            }

            is ChangePasswordScreenAction.OnConfirmPasswordChanged -> {
                _state.update {
                    it.copy(confirmPassword = action.confirmPassword)
                }
            }

            ChangePasswordScreenAction.ConfirmClicked -> {
                val newPasswordError = newPasswordValidateUseCase(state.value.newPassword)
                when {
                    newPasswordError != null -> {
                        _state.update { it.copy(newPasswordError = newPasswordError) }
                    }

                    state.value.newPassword != state.value.confirmPassword -> {
                        _state.update {
                            it.copy(confirmPasswordError = SharedRes.Strings.password_does_not_match)
                        }
                    }

                    else -> changePassword()
                }

            }
        }
    }

    private fun changePassword() = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        changePasswordUseCase(
            newPassword = state.value.newPassword,
            confirmPassword = state.value.confirmPassword

        ).onSuccess { data ->
            _state.update {
                it.copy(isLoading = false)
            }
            _successChannel.send(true)
        }.onError { error ->
            _state.update {
                it.copy(isLoading = false)
            }
            _errorChannel.send(error.toErrorMessage())
        }
    }


}