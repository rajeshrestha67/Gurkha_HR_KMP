package com.gurkha.hr.settings.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.form.PasswordValidateUseCase
import com.gurkha.hr.res.SharedRes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ChangePasswordViewModel(
    private val newPasswordValidateUseCase: PasswordValidateUseCase
): ViewModel(){
    private val _state = MutableStateFlow(value = ChangePasswordScreenState())

    private val _errorChannel = Channel<String>()

    val errorChannel = _errorChannel.receiveAsFlow()

    private val _successChannel = Channel<Boolean>()

    val state = _state
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChangePasswordScreenState()
        )

    fun onAction(action: ChangePasswordScreenAction){
        when(action){
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
                when{
                    newPasswordError != null -> {
                        _state.update { it.copy(newPasswordError = newPasswordError.errorMsg) }
                    }
                    state.value.newPassword != state.value.confirmPassword -> {
                        _state.update {
                            it.copy(confirmPasswordError = SharedRes.Strings.invalidPasswordLowercase)
                        }
                    }
//                    else ->
                }

            }
        }
    }


}