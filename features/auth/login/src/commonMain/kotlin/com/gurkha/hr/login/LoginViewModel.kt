package com.gurkha.hr.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.domain.auth.login.usecase.ClearTokenUseCase
import com.gurkha.hr.domain.auth.login.usecase.LoginUseCase
import com.gurkha.hr.domain.form.EmailValidateUseCase
import com.gurkha.hr.domain.form.PasswordValidateUseCase
import com.gurkha.hr.domain.splash.UpdateFirstTimeCheckUseCase
import com.gurkha.hr.domain.token.usecase.FetchTokenAllValueUseCase
import com.gurkha.hr.domain.token.usecase.UpdateBiometricEnableUseCase
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailFlowUseCase
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.login.model.LoginScreenAction
import com.gurkha.hr.login.model.LoginScreenState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.toErrorMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val clearTokenUseCase: ClearTokenUseCase,
    private val emailValidateUseCase: EmailValidateUseCase,
    private val passwordValidateUseCase: PasswordValidateUseCase,
    private val updateFirstTimeCheckUseCase: UpdateFirstTimeCheckUseCase,
    private val fetchTokenAllValueUseCase: FetchTokenAllValueUseCase,
    private val fetchUserDetailFlowUseCase: FetchUserDetailFlowUseCase,
    private val updateBiometricEnableUseCase: UpdateBiometricEnableUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginScreenState())

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private val _successChannel = Channel<Boolean>()
    val successChannel = _successChannel.receiveAsFlow()
    private var bioToken: String? = null

    private var previousEmail: String? = null

    private val _resetChannel = Channel<Boolean>()
    val resetChannel = _resetChannel.receiveAsFlow()
    val state = _state.combine(
        flow = fetchTokenAllValueUseCase(),
    ) { state, token ->
        bioToken = token.biometricToken
        state.copy(
            isBiometricEnabled = token.isBiometricEnable,
        )
    }
        .onStart {
            fetchUserDetailFlowUseCase().firstOrNull()?.let { user ->
                previousEmail = user.email
                _state.update {
                    it.copy(
                        username = user.email
                    )
                }
            }
            updateFirstTimeUser()
            clearToken()
        }
        .stateIn(
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

            is LoginScreenAction.OnBiometricLogin -> {
                biometricLogin()
            }

            is LoginScreenAction.OnResetBiometric -> {
                resetBiometric()
            }

            LoginScreenAction.LoginClicked -> {
                val usernameError = emailValidateUseCase(state.value.username)
                val passwordError = passwordValidateUseCase(state.value.password)
                when {
                    usernameError != null -> {
                        _state.update { it.copy(usernameError = usernameError) }
                    }

                    passwordError != null -> {
                        _state.update { it.copy(passwordError = passwordError) }
                    }

                    else -> login()
                }
            }
        }
    }

    private fun clearToken() = viewModelScope.launch {
        clearTokenUseCase()
    }

    private fun login() = viewModelScope.launch {

        _state.update {
            it.copy(isLoading = true)
        }

        //while login with the email and password send token null
        // else the token is sent and will be able to login with any pw
        loginUseCase(
            username = state.value.username,
            password = state.value.password
        ).onSuccess { data ->
            _successChannel.send(true)
            AppLogger.i(TAG, "login: api response $data")

            if (previousEmail != null && state.value.username != previousEmail) {
                resetBiometric()
            }
            _state.update {
                it.copy(isLoading = false)
            }
        }.onError { error ->
            _errorChannel.send(error.toErrorMessage())
            AppLogger.e(TAG, "login: api response", error)
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    private fun biometricLogin() = viewModelScope.launch {
        if (previousEmail != null && state.value.username != previousEmail) {
            _resetChannel.send(true)
            return@launch
        }
        _state.update {
            it.copy(isLoading = true)
        }
        loginUseCase(
            username = state.value.username,
            biometricToken = bioToken
        ).onSuccess { data ->
            _state.update {
                it.copy(isLoading = false)
            }
            _successChannel.send(true)
            AppLogger.i(TAG, "login: api response $data")
        }.onError { error ->
            _state.update {
                it.copy(isLoading = false)
            }
            _errorChannel.send(error.toErrorMessage())
            AppLogger.e(TAG, "login: api response", error)
        }
    }

    private fun resetBiometric() = viewModelScope.launch {
        val token = fetchTokenAllValueUseCase().firstOrNull() ?: Token()
        updateBiometricEnableUseCase(
            token.copy(
                isBiometricEnable = false,
                biometricToken = null
            )
        )
    }

    private fun updateFirstTimeUser() = viewModelScope.launch {
        updateFirstTimeCheckUseCase()
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}