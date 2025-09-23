package com.gurkha.hr.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.attendanceStatus.useCase.AttendanceStatusUseCase
import com.gurkha.hr.leave.model.LeaveScreenAction
import com.gurkha.hr.leave.model.LeaveScreenState
import com.gurkha.hr.networkhelper.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaveScreenViewModel(
    private val attendanceStatusUseCase: AttendanceStatusUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LeaveScreenState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LeaveScreenState()
        )

    fun onAction(action: LeaveScreenAction) {
        when (action) {
            is LeaveScreenAction.OnStatusChange -> {
                _state.update {
                    it.copy(
                        attendanceStatus = action.status
                    )
                }
                fetchAttendanceStatus(
                    attendanceStatus = state.value.attendanceStatus,
                    employeeName = "",
                    isSelf = "Y"
                )
            }
        }
    }

    fun fetchAttendanceStatus(
        attendanceStatus: String,
        employeeName: String,
        isSelf: String
    ) = viewModelScope.launch {
        println("fetchAttendanceStatus")
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        attendanceStatusUseCase(
            attendanceStatus = attendanceStatus,
            employeeName = employeeName,
            isSelf = isSelf
        ).onSuccess { data ->
            when (attendanceStatus) {
                "pending" -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            pendingResult = data
                        )
                    }
                }
                "approved" -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            approvedResult = data
                        )
                    }
                }
                "cancelled" -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            cancelledResult = data
                        )
                    }
                }
            }
        }
    }

}