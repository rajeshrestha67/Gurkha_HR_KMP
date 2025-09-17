package com.gurkha.hr.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.attendance.usecase.AttendanceUseCase
import com.gurkha.hr.home.Model.HomeScreenActions
import com.gurkha.hr.home.Model.HomeScreenState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val attendanceUseCase: AttendanceUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state
        .onStart {
            fetch()
        }

        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeScreenState()
    )

    fun onAction(action: HomeScreenActions) {
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

    private fun fetch() = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        attendanceUseCase(
            fromDate = "2025-09-16",
            toDate = "2025-09-16",
            enabledManualAttendance = "N",
            branchId = null
        ).onSuccess {data ->
            _state.update {
                it.copy(isLoading = false)
            }
            println("Success")
        }.onError {
            _state.update {
                it.copy(isLoading = false)
            }
            println("Error")

        }
    }
}