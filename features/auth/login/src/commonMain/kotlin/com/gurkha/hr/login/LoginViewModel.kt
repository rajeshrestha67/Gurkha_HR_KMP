package com.gurkha.hr.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.auth.login.UserRemoteRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRemoteRepository: UserRemoteRepository,
): ViewModel() {

    fun login()=viewModelScope.launch{
        userRemoteRepository.login("test","test")
    }
}