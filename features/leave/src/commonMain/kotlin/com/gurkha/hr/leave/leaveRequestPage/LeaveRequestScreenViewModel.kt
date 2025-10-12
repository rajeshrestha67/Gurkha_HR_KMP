package com.gurkha.hr.leave.leaveRequestPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.date.DateData
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.leave.leaveAssignee.model.toUiList
import com.gurkha.hr.domain.leave.leaveAssignee.usecase.AssigneeUseCase
import com.gurkha.hr.domain.leave.leaveRequest.usecase.LeaveRequestUseCase
import com.gurkha.hr.domain.leave.leaveType.model.toUiList
import com.gurkha.hr.domain.leave.leaveType.usecase.LeaveTypeUseCase
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenAction
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.leave.leave_request.LeaveRequestData
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

class LeaveRequestScreenViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase,
    private val assigneeUseCase: AssigneeUseCase,
    private val leaveTypeUseCase: LeaveTypeUseCase,
    private val  leaveRequestUseCase : LeaveRequestUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(LeaveRequestScreenState())

    val state = _state
        .onStart {
            fetchAssignee()
            fetchLeaveType()
        }

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LeaveRequestScreenState()
        )
    private val _dataChannel = Channel<LeaveRequestData?>()
    val dataChannel = _dataChannel.receiveAsFlow()

    private val _successChannel = Channel<String>()
    val successChannel =_successChannel.receiveAsFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel =_errorChannel.receiveAsFlow()

    fun onAction(action: LeaveRequestScreenAction) {
        when (action) {
            is LeaveRequestScreenAction.UpdateLeaveRequestData -> {
                _state.update {
                    it.copy(
                        leaveRequestData = action.data
                    )
                }
            }

            is LeaveRequestScreenAction.OnEndDateChange -> {
                _state.update {
                    it.copy(endDate = action.date, endDateError = null)
                }
            }

            is LeaveRequestScreenAction.OnLeaveDurationChange -> {
                _state.update {
                    it.copy(leaveDuration = action.leaveDuration, leaveDurationError = null)
                }
            }

            is LeaveRequestScreenAction.OnLeaveTypeChange -> {
                _state.update {
                    it.copy(leaveType = action.leaveType, leaveTypeError = null)
                }
            }

            is LeaveRequestScreenAction.OnAssigneeChange -> {
                _state.update {
                    it.copy(assignee = action.assignee, assigneeError = null)
                }
            }

            is LeaveRequestScreenAction.OnAssigneeError -> {
                _state.update {
                    it.copy(
                        assigneeError = action.error
                    )
                }
            }

            is LeaveRequestScreenAction.OnReasonChange -> {
                _state.update {
                    it.copy(reason = action.reason, reasonError = null)
                }
            }

            is LeaveRequestScreenAction.OnStartDateChange -> {
                _state.update {
                    it.copy(startDate = action.date, startDateError = null)
                }
            }

            is LeaveRequestScreenAction.OnReasonError -> {
                _state.update {
                    it.copy(
                        reasonError = action.error,
                    )
                }
            }

            LeaveRequestScreenAction.Submit -> {
                submit()
            }

            LeaveRequestScreenAction.OnRefetchAssignee -> {
                _state.update {
                    it.copy(
                        isAssigneeFetchingError = false
                    )
                }
                fetchAssignee()
            }

            LeaveRequestScreenAction.OnRefetchLeaveType -> {
                _state.update {
                    it.copy(
                        isLeaveTypeFetchingError = false
                    )
                }
                fetchLeaveType()
            }
        }
    }

    private fun submit() = viewModelScope.launch {
        val startDateError = requiredValidationUseCase(state.value.startDate?.displayValueAD)
        val endDateError = requiredValidationUseCase(state.value.endDate?.displayValueAD)
        val leaveDurationError = requiredValidationUseCase(state.value.leaveDuration?.value)
        val leaveTypeError = requiredValidationUseCase(state.value.leaveType?.value)
        val reasonError = requiredValidationUseCase(state.value.reason)
        val assigneeError = requiredValidationUseCase(state.value.assignee?.value)

        when {
            startDateError != null -> {
                _state.update {
                    it.copy(
                        startDateError = startDateError
                    )
                }
            }

            endDateError != null -> {
                _state.update {
                    it.copy(
                        endDateError = endDateError
                    )
                }
            }

            assigneeError != null -> {
                _state.update {
                    it.copy(
                        assigneeError = assigneeError
                    )
                }
            }

            leaveDurationError != null -> {
                _state.update {
                    it.copy(
                        leaveDurationError = leaveDurationError
                    )
                }
            }

            leaveTypeError != null -> {
                _state.update {
                    it.copy(
                        leaveTypeError = leaveTypeError
                    )
                }
            }

            reasonError != null -> {
                _state.update {
                    it.copy(
                        reasonError = reasonError
                    )
                }
            }


            else -> {
                _state.update {
                    it.copy(
                        startDateError = null,
                        endDateError = null,
                        leaveDurationError = null,
                        leaveTypeError = null,
                        reasonError = null,
                        assigneeError = null
                    )
                }

                _dataChannel.send(
                    LeaveRequestData(
                        startDate = state.value.startDate?.displayValueAD ?: "",
                        endDate = state.value.endDate?.displayValueAD ?: "",
                        leaveDuration = state.value.leaveDuration?.value ?: "",
                        leaveType = state.value.leaveType?.name ?: "",
                        assignee = state.value.assignee?.name ?: "",
                        reason = state.value.reason,
                    ),
                )

                requestLeave(
                    startDate = state.value.startDate?.displayValueAD ?: "",
                    endDate = state.value.endDate?.displayValueAD ?: "",
                    leaveDuration = state.value.leaveDuration?.value ?: "",
                    leaveTypeId = state.value.leaveType?.value?.toInt() ?: 0,
                    assigneeId = state.value.assignee?.value?.toInt() ?: 0,
                    reason = state.value.reason,
                )

                _state.update {
                    it.copy(
                        startDate = null,
                        endDate = null,
                        leaveDuration = null,
                        leaveType = null,
                        assignee = null,
                        reason = ""
                    )
                }
            }
        }
    }

    private fun fetchAssignee() = viewModelScope.launch {
        _state.update {
            it.copy(
                isAssigneeLoading = true
            )
        }
        assigneeUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isAssigneeLoading = false,
                    leaveAssigneeList = data.toUiList()
                )
            }
        }.onError {
            _state.update {
                it.copy(
                    isAssigneeFetchingError = true
                )
            }
        }
    }

    private fun fetchLeaveType() = viewModelScope.launch {
        _state.update {
            it.copy(
                isLeaveTypeLoading = true
            )
        }
        leaveTypeUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isLeaveTypeLoading = false,
                    leaveTypeList = data.toUiList()
                )
            }
        }.onError {
            _state.update {
                it.copy(
                    isLeaveTypeFetchingError = true
                )
            }
        }
    }

    private fun requestLeave(
        startDate: String,
        endDate: String,
        assigneeId: Int,
        leaveTypeId: Int,
        leaveDuration: String,
        reason: String
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                isRequestingLeave = true
            )
        }
        leaveRequestUseCase(
            startDate = startDate,
            endDate = endDate,
            leaveDuration = leaveDuration,
            leaveTypeId = leaveTypeId,
            reason = reason,
            assigneeId = assigneeId
        ).onSuccess {data ->
            _state.update {
                it.copy(
                    isRequestingLeave = false,
                )
            }
            _successChannel.send(data.message)
        }.onError { error ->
            _state.update {
                it.copy(
                    isRequestingLeave = false,
                )
            }
            _errorChannel.send(error.toErrorMessage())
        }
    }
}