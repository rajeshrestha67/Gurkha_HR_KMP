package com.gurkha.hr.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.home.Model.HomeScreenActions
import com.gurkha.hr.home.Model.HomeScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HomeScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeScreenState()
    )

    fun action(action: HomeScreenActions) {
        when (action) {
            is HomeScreenActions.OnRequestLeave -> {
                TODO()
            }

            is HomeScreenActions.OnSearchedClicked -> {
                println("Search Clicked")
            }

            is HomeScreenActions.OnChatClicked -> {
                println("Chat Clicked")
            }

            is HomeScreenActions.OnNotificationClicked -> {
                println("Notification Clicked")
            }

            is HomeScreenActions.OnCheckInClicked -> {
                TODO()
            }

            is HomeScreenActions.OnCheckOutClicked -> {
                TODO()
            }

            is HomeScreenActions.OnRequestAttendance -> {
                TODO()
            }

            is HomeScreenActions.OnSpecificDayClicked -> {
                TODO()
            }
        }
    }
}