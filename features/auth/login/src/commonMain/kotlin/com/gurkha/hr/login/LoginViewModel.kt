package com.gurkha.hr.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.auth.login.usecase.LoginUseCase
import com.gurkha.hr.login.model.LoginScreenAction
import com.gurkha.hr.login.model.LoginScreenState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.networkhelper.toErrorMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginScreenState())

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()
    val state = _state.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginScreenState()
    )

    fun onAction(action: LoginScreenAction) {
        when (action) {
            is LoginScreenAction.OnPasswordChanged -> {
                _state.update {
                    it.copy(password = action.password)
                }
            }

            is LoginScreenAction.OnUsernameChanged -> {
                _state.update {
                    it.copy(username = action.username)
                }
            }

            is LoginScreenAction.OnPasswordError -> {
                _state.update {
                    it.copy(passwordError = action.passwordError)
                }
            }

            is LoginScreenAction.OnUsernameError -> {
                _state.update {
                    it.copy(usernameError = action.usernameError)
                }
            }

            LoginScreenAction.LoginClicked -> {
                login()
            }
        }
    }

    private fun login() = viewModelScope.launch {
        //loginUseCase("Chirag.dangol@mbank.com.np", "OVf#9PfTs")
        loginUseCase(
            username = state.value.username,
            password = state.value.password
        ).onSuccess { data ->

        }.onError { error ->
            _errorChannel.send(error.toErrorMessage())
        }
    }
}