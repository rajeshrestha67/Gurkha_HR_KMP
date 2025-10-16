package com.gurkha.hr.leave.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.attendance.attendanceStatus.model.AttendanceStatusData
import com.gurkha.hr.domain.leave.leaveReport.model.LeaveReportData
import com.gurkha.hr.domain.leave.leaveReport.useCase.LeaveReportUseCase
import com.gurkha.hr.domain.leave.leaveRequest.usecase.LeaveRequestUseCase
import com.gurkha.hr.domain.leave.leaveSummary.useCase.LeaveSummaryUseCase
import com.gurkha.hr.leave.model.leave.LeaveScreenAction
import com.gurkha.hr.leave.model.leave.LeaveScreenState
import com.gurkha.hr.leave.model.leave.LeaveStatusEnum
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestData
import com.gurkha.model.leave.leave_request.LeaveRequestData
import com.gurkha.model.leave.ui.AssigneeUi
import com.gurkha.model.leave.ui.LeaveDurationUi
import com.gurkha.model.leave.ui.LeaveTypeUi
import com.gurkha.model.network.toErrorMessage
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
    private val leaveReportUseCase: LeaveReportUseCase,
    private val leaveSummaryUseCase: LeaveSummaryUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LeaveScreenState())
    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private val _successChannel = Channel<String>()
    val successChannel = _successChannel.receiveAsFlow()


    val state = _state
        .onStart {
            fetchLeaveSummary()
            fetchLeaveReport(
                leaveStatus = LeaveStatusEnum.PENDING,
            )
        }
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
                        leaveStatus = action.status,
                        currentTapItem = when (action.status) {
                            LeaveStatusEnum.PENDING -> it.pendingTapItem
                            LeaveStatusEnum.APPROVED -> it.approvedTapItem
                            else -> it.rejectedTapItem
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
                    val data: LeaveRequestData =Json.decodeFromString<LeaveRequestData>(action.json)
                    updateLeaveRequestData(
                        data = data
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
                        rejectedTapItem = it.rejectedTapItem.copy(isLoading = true)
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
                            rejectedTapItem = it.rejectedTapItem.copy(
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
                        else -> it.rejectedTapItem
                    }
                )
            }
        }
    }

    private fun fetchLeaveSummary() = viewModelScope.launch {
        _state.update {
            it.copy(
                isLeaveSummaryLoading = true
            )
        }
        leaveSummaryUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isLeaveSummaryLoading = false,
                    leaveItemsList = _state.value.leaveItemsList.mapIndexed { index, item ->
                        when (index) {
                            0 -> {
                                item.copy(
                                    days = data.remainingLeaveCount.toString()
                                )
                            }
                            1 -> {
                                item.copy(
                                    days = data.approvedCount.toString()
                                )
                            }
                            2 -> {
                                item.copy(
                                    days = data.pendingCount.toString()
                                )
                            }
                            3 -> {
                                item.copy(
                                    days = data.rejectedCount.toString()
                                )
                            }
                            else -> {
                                item
                            }
                        }
                    }
                )
            }
        }.onError {
            _state.update {
                it.copy(
                    isLeaveSummaryLoading = false
                )
            }
        }
    }

    private fun updateLeaveRequestData(
        data: LeaveRequestData
    ) = viewModelScope.launch {
        _state.update { currentState ->
            val updatedPendingList = currentState.pendingTapItem.result + LeaveReportData(
                employeeId = 0,
                startDate = data.startDate,
                endDate = data.endDate,
                leaveStatus = LeaveStatusEnum.PENDING.value,
                reason = data.reason,
                leaveDuration = data.leaveDuration,
                assigneeName = data.assignee,
                totalDays = 0.0,
                leaveType = data.leaveType,
                requestedDate = ""
            )

            val updatedPendingTab =
                currentState.pendingTapItem.copy(result = updatedPendingList)

            currentState.copy(
                pendingTapItem = updatedPendingTab,
                currentTapItem = if (currentState.leaveStatus == LeaveStatusEnum.PENDING) updatedPendingTab
                else currentState.currentTapItem,
                isRequestingLeave = false,
                leaveRequestDataJson = null
            )
        }

    }

}

