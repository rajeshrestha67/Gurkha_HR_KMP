package com.gurkha.hr.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.splash.CheckFirstTimeUserUseCase
import com.gurkha.hr.domain.splash.UpdateFirstTimeCheckUseCase
import com.gurkha.hr.splashscreen.model.SplashScreenAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashscreenViewModel(
    private val checkFirstTimeUserUseCase: CheckFirstTimeUserUseCase,
    private val updateFirstTimeCheckUseCase: UpdateFirstTimeCheckUseCase
) : ViewModel() {

    private val _navigationChannel = Channel<Boolean>()
    val navigationChannel = _navigationChannel.receiveAsFlow()


    fun action(action: SplashScreenAction) = viewModelScope.launch {
        when (action) {
            is SplashScreenAction.CheckFirstUser -> {
                checkFirstUser()
            }
        }
    }

    private fun checkFirstUser() = viewModelScope.launch {

            val firstTime = checkFirstTimeUserUseCase()

            if (firstTime) {
                updateFirstTimeCheckUseCase()
            }
            _navigationChannel.send(firstTime)

    }

}

