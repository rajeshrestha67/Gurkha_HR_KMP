package com.gurkha.hr.leave.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.attendance.attendanceStatus.useCase.AttendanceStatusUseCase
import com.gurkha.hr.domain.leave.leaveReport.useCase.LeaveReportUseCase
import com.gurkha.hr.domain.leave.leaveRequest.usecase.LeaveRequestUseCase
import com.gurkha.hr.leave.model.leave.LeaveScreenAction
import com.gurkha.hr.leave.model.leave.LeaveScreenState
import com.gurkha.hr.leave.model.leave.LeaveStatusEnum
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.networkhelper.toErrorMessage
import com.gurkha.model.leave.leave_request.LeaveRequestData
import com.gurkha.model.leave.ui.LeaveAssigneeUi
import com.gurkha.model.leave.ui.LeaveDurationUi
import com.gurkha.model.leave.ui.LeaveTypeUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class LeaveScreenViewModel(
    private val attendanceStatusUseCase: AttendanceStatusUseCase,
    private val leaveRequestUseCase: LeaveRequestUseCase,
    private val leaveReportUseCase: LeaveReportUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LeaveScreenState())
    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private val _successChannel = Channel<String>()
    val successChannel = _successChannel.receiveAsFlow()


    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LeaveScreenState()
        )

    //fetch for the pending in the starting
    init {
        fetchLeaveReport(
            leaveStatus = LeaveStatusEnum.PENDING,
        )
    }

    fun onAction(action: LeaveScreenAction) {
        when (action) {
            is LeaveScreenAction.OnStatusChange -> {
                _state.update {
                    it.copy(
                        leaveStatus = action.status,
                        currentTapItem = when (action.status) {
                            LeaveStatusEnum.PENDING -> it.pendingTapItem
                            LeaveStatusEnum.APPROVED -> it.approvedTapItem
                            else -> it.cancelTapItem
                        }
                    )
                }
                if (state.value.currentTapItem.result.isEmpty()) {
                    fetchLeaveReport(
                        leaveStatus = state.value.leaveStatus,
                    )
                }
            }

            is LeaveScreenAction.UpdateRequestData -> {
                _state.update {
                    it.copy(
                        leaveRequestDataJson = action.json
                    )
                }

                action.json?.let {
                    val data: LeaveRequestData =
                        Json.decodeFromString<LeaveRequestData>(action.json)
                    val assigneeId =
                        Json.decodeFromString<LeaveAssigneeUi>(data.assignee).value.toInt()
                    val leaveTypeId =
                        Json.decodeFromString<LeaveTypeUi>(data.leaveType).value.toInt()
                    val leaveDuration =
                        Json.decodeFromString<LeaveDurationUi>(data.leaveDuration).value

//                  call the request leave function
                    requestLeave(
                        data = data,
                        assigneeId = assigneeId,
                        leaveTypeId = leaveTypeId,
                        leaveDuration = leaveDuration
                    )
                }
            }

            is LeaveScreenAction.OnLeaveRequest -> {
                _state.update {
                    it.copy(
                        isRequestingLeave = true
                    )
                }


            }
        }
    }


//    fetch leave report
    fun fetchLeaveReport(
        leaveStatus: LeaveStatusEnum,
    ) = viewModelScope.launch {

        _state.update {
            when (leaveStatus) {
                LeaveStatusEnum.PENDING -> {
                    it.copy(
                        pendingTapItem = it.pendingTapItem.copy(isLoading = true)
                    )
                }

                LeaveStatusEnum.APPROVED -> {
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
        leaveReportUseCase(
            leaveStatus = leaveStatus.value,
        ).onSuccess { data ->
            when (leaveStatus) {
                LeaveStatusEnum.PENDING -> {
                    _state.update {
                        it.copy(
                            pendingTapItem = it.pendingTapItem.copy(
                                isLoading = false,
                                result = data
                            )
                        )
                    }
                }

                LeaveStatusEnum.APPROVED -> {
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
                    currentTapItem = when (leaveStatus) {
                        LeaveStatusEnum.PENDING -> it.pendingTapItem
                        LeaveStatusEnum.APPROVED -> it.approvedTapItem
                        else -> it.cancelTapItem
                    }
                )
            }
        }
    }

    private fun requestLeave(
        data: LeaveRequestData,
        assigneeId: Int,
        leaveTypeId: Int,
        leaveDuration: String
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                isRequestingLeave = true
            )
        }
        leaveRequestUseCase(
            startDate = data.startDate,
            endDate = data.endDate,
            leaveDuration = leaveDuration,
            leaveTypeId = leaveTypeId,
            reason = data.reason,
            assigneeId = assigneeId
        ).onSuccess {data ->
            _state.update {
                it.copy(
                    isRequestingLeave = false,
                )
            }

//            refetch the pending data
            fetchLeaveReport(
                leaveStatus = LeaveStatusEnum.PENDING
            )

//            temporary use
            _successChannel.send("Leave Request Successfully")
        }.onError {error->
            _state.update {
                it.copy(
                    isRequestingLeave = false
                )
            }
            _errorChannel.send(error.toErrorMessage())
        }
    }

}

