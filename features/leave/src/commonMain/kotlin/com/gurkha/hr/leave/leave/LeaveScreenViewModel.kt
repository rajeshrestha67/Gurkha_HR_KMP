package com.gurkha.hr.leave.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.attendance.attendanceStatus.useCase.AttendanceStatusUseCase
import com.gurkha.hr.leave.model.leave.AttendanceStatusEnum
import com.gurkha.hr.leave.model.leave.LeaveScreenAction
import com.gurkha.hr.leave.model.leave.LeaveScreenState
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

    //fetch for the pending in the starting
    init {
        fetchAttendanceStatus(
            attendanceStatus = AttendanceStatusEnum.PENDING,
            employeeName = "",
            isSelf = "Y"
        )
    }

    fun onAction(action: LeaveScreenAction) {
        when (action) {
            is LeaveScreenAction.OnStatusChange -> {
                _state.update {
                    it.copy(
                        attendanceStatus = action.status,
                        currentTapItem = when (action.status) {
                            AttendanceStatusEnum.PENDING -> it.pendingTapItem
                            AttendanceStatusEnum.APPROVED -> it.approvedTapItem
                            else -> it.cancelTapItem
                        }
                    )
                }
                if (state.value.currentTapItem.result.isEmpty()) {
                    fetchAttendanceStatus(
                        attendanceStatus = state.value.attendanceStatus,
                        employeeName = "",
                        isSelf = "Y"
                    )
                }
            }

            is LeaveScreenAction.UpdateRequestData -> {
                _state.update {
                    it.copy(
                        leaveRequestDataJson = action.json
                    )
                }
            }
        }
    }

    fun fetchAttendanceStatus(
        attendanceStatus: AttendanceStatusEnum,
        employeeName: String,
        isSelf: String
    ) = viewModelScope.launch {

        _state.update {
            when (attendanceStatus) {
                AttendanceStatusEnum.PENDING -> {
                    it.copy(
                        pendingTapItem = it.pendingTapItem.copy(isLoading = true)
                    )
                }

                AttendanceStatusEnum.APPROVED -> {
                    it.copy(
                        approvedTapItem = it.approvedTapItem.copy(isLoading = true)
                    )
                }

                else -> {
                    it.copy(
                        cancelTapItem = it.cancelTapItem.copy(isLoading = true)
                    )
                }
            }

        }
        attendanceStatusUseCase(
            attendanceStatus = attendanceStatus.value,
            employeeName = employeeName,
            isSelf = isSelf
        ).onSuccess { data ->
            when (attendanceStatus) {
                AttendanceStatusEnum.PENDING -> {
                    _state.update {
                        it.copy(
                            pendingTapItem = it.pendingTapItem.copy(
                                isLoading = false,
                                result = data
                            )
                        )
                    }
                }

                AttendanceStatusEnum.APPROVED -> {
                    _state.update {
                        it.copy(
                            approvedTapItem = it.approvedTapItem.copy(
                                isLoading = false,
                                result = data
                            )
                        )
                    }
                }

                else -> {
                    _state.update {
                        it.copy(
                            cancelTapItem = it.cancelTapItem.copy(
                                isLoading = false,
                                result = data
                            )
                        )
                    }
                }
            }
            _state.update {
                it.copy(
                    currentTapItem = when (attendanceStatus) {
                        AttendanceStatusEnum.PENDING -> it.pendingTapItem
                        AttendanceStatusEnum.APPROVED -> it.approvedTapItem
                        else -> it.cancelTapItem
                    }
                )
            }
        }
    }

}