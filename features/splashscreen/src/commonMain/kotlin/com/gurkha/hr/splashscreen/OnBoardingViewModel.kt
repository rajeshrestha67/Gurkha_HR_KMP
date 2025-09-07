package com.gurkha.hr.splashscreen

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.splash.CheckFirstTimeUserUseCase
import com.gurkha.hr.domain.splash.UpdateFirstTimeCheckUseCase
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.splashscreen.model.Indicator
import com.gurkha.hr.splashscreen.model.OnBoardingAction
import com.gurkha.hr.splashscreen.model.OnBoardingScreenState
import com.gurkha.hr.splashscreen.model.ScreenList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class OnBoardingViewModel(
    private val checkFirstTimeUserUseCase: CheckFirstTimeUserUseCase,
    private val updateFirstTimeCheckUseCase: UpdateFirstTimeCheckUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(OnBoardingScreenState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = OnBoardingScreenState()
        )

    //list of all the screens for the onboarding screen
    val screens = ScreenList.screenList


    private val _navigationChannel = Channel<Boolean>()
    val navigationChannel = _navigationChannel.receiveAsFlow()

    val indicators: StateFlow<List<Indicator>> = state
        .map { uiState ->
            screens.mapIndexed { index, _ ->
                Indicator(
                    color = if (index == uiState.currentPage) Color.Red else Color.Gray,
                    width = if (index == uiState.currentPage) 30.dp else 10.dp,
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


    //    actions
    fun action(action: OnBoardingAction) {
        when (action) {
            is OnBoardingAction.CheckFirstUser -> checkFirstUser()

            is OnBoardingAction.OnNext -> {
                if (state.value.currentPage < screens.size - 1) {
                    _state.update {
                        it.copy(
                            currentPage = it.currentPage + 1,
                        )
                    }
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
        val firstTime = checkFirstTimeUserUseCase()
        if (firstTime) updateFirstTimeCheckUseCase()
        _navigationChannel.send(firstTime)
    }
}
