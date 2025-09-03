package com.gurkha.hr.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.splash.CheckFirstTimeUserUseCase
import kotlinx.coroutines.launch

class SplashscreenViewModel(
    private val checkFirstTimeUserUseCase: CheckFirstTimeUserUseCase
) : ViewModel() {


    fun print() = viewModelScope.launch {
        println("SplashscreenViewModel ${checkFirstTimeUserUseCase()}")
    }

}

