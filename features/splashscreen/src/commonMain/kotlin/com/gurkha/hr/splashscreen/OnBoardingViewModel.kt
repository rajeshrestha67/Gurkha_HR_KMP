package com.gurkha.hr.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.splash.CheckFirstTimeUserUseCase
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.splashscreen.model.OnBoardingAction
import com.gurkha.hr.splashscreen.model.OnBoardingScreenState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class OnBoardingViewModel(
    private val checkFirstTimeUserUseCase: CheckFirstTimeUserUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(OnBoardingScreenState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = OnBoardingScreenState()
        )

    private val _navigationChannel = Channel<Boolean>()
    val navigationChannel = _navigationChannel.receiveAsFlow()

    // actions
    fun action(action: OnBoardingAction) {
        when (action) {
            is OnBoardingAction.CheckFirstUser -> checkFirstUser()

            is OnBoardingAction.OnNext -> {
                if (state.value.currentPage < _state.value.screens.size - 1) {
                    val nextPage =
                        (_state.value.currentPage + 1).coerceAtMost(_state.value.screens.lastIndex)
                    setCurrentPage(nextPage)
                } else {
                    viewModelScope.launch {
                        _navigationChannel.send(true)
                    }
                }
            }

            is OnBoardingAction.SetCurrentPage -> {
                setCurrentPage(action.page)
            }
        }
    }

    // Called when pager is manually scrolled
    private fun setCurrentPage(page: Int) {
        _state.update {
            val isLastPage = page == it.screens.size - 1
            it.copy(
                currentPage = page,
                title = if (isLastPage) SharedRes.Strings.getStarted else SharedRes.Strings.next
            )
        }
    }

    private fun checkFirstUser() = viewModelScope.launch {
        
        _navigationChannel.send(checkFirstTimeUserUseCase())
    }
}
