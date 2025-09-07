package com.gurkha.hr.splashscreen

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.splash.CheckFirstTimeUserUseCase
import com.gurkha.hr.domain.splash.UpdateFirstTimeCheckUseCase
import com.gurkha.hr.splashscreen.model.Indicator
import com.gurkha.hr.splashscreen.model.OnBoardingAction
import com.gurkha.hr.splashscreen.model.ScreenList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class OnBoardingViewModel(
    private val checkFirstTimeUserUseCase: CheckFirstTimeUserUseCase,
    private val updateFirstTimeCheckUseCase: UpdateFirstTimeCheckUseCase
) : ViewModel() {

    val screens = ScreenList.screenList

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _navigationChannel = Channel<Boolean>()
    val navigationChannel = _navigationChannel.receiveAsFlow()

    val indicator = _currentPage.map { current ->
        screens.mapIndexed { index, _ ->
            Indicator(
                color = if (index == current) Color.Red else Color.Gray,
                width = if (index == current) 30.dp else 10.dp,
            )
        }
    }

//    actions
    fun action(action: OnBoardingAction) {
        viewModelScope.launch {
            when (action) {
                is OnBoardingAction.CheckFirstUser -> checkFirstUser()

                is OnBoardingAction.OnNext -> {
                    if (_currentPage.value < screens.size - 1) {
                        _currentPage.update { it + 1 }
                    } else {
                        _navigationChannel.send(true)
                    }
                }

                is OnBoardingAction.OnSkip -> {
                    _currentPage.update { screens.size - 1}
                }
            }
        }
    }

    // Called when pager is manually scrolled
    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }

    private fun checkFirstUser() = viewModelScope.launch {
        val firstTime = checkFirstTimeUserUseCase()
        if (firstTime) updateFirstTimeCheckUseCase()
        _navigationChannel.send(firstTime)
    }
}
