package com.gurkha.hr.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.splash.CheckFirstTimeUserUseCase
import com.gurkha.hr.domain.splash.UpdateFirstTimeCheckUseCase
import kotlinx.coroutines.launch

class SplashscreenViewModel(
    private val checkFirstTimeUserUseCase: CheckFirstTimeUserUseCase,
    private val updateFirstTimeCheckUseCase: UpdateFirstTimeCheckUseCase
) : ViewModel() {


    fun print() = viewModelScope.launch {
        val isFirstTime = checkFirstTimeUserUseCase()
        println("SplashscreenViewModel ${checkFirstTimeUserUseCase()}")

        if (isFirstTime) {
            updateFirstTimeCheckUseCase()
        }


    }


}

