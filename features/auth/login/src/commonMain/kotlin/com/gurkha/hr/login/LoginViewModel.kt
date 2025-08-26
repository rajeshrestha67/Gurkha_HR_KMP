package com.gurkha.hr.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.auth.login.usecase.LoginUseCase
import com.gurkha.hr.domain.auth.login.repository.UserRemoteRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
): ViewModel() {

    fun login()=viewModelScope.launch{
        loginUseCase("Chirag.dangol@mbank.com.np","OVf#9PfTs")
    }
}