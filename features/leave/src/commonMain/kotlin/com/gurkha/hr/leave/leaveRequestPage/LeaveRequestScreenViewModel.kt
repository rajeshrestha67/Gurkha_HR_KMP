package com.gurkha.hr.leave.leaveRequestPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.textField.DateData
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.leave.leaveAssignee.usecase.LeaveAssigneeUseCase
import com.gurkha.hr.domain.leave.leaveType.usecase.LeaveTypeUseCase
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenAction
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
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

class LeaveRequestScreenViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase,
    private val leaveAssigneeUseCase: LeaveAssigneeUseCase,
    private val leaveTypeUseCase: LeaveTypeUseCase
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

    fun onAction(action: LeaveRequestScreenAction) {
        when (action) {
            is LeaveRequestScreenAction.UpdateLeaveRequestData -> {
                action.data?.let { safeData ->
                    _state.update {
                        it.copy(
                            leaveRequestData = safeData,
                            startDate = DateData.fromDisplay(safeData.startDate),
                            endDate = DateData.fromDisplay(safeData.endDate),
                            leaveDuration = LeaveDurationUi(
                                name = safeData.leaveDuration,
                                value = safeData.leaveDuration
                            ),
                            leaveType = LeaveTypeUi(
                                name = safeData.leaveType,
                                value = safeData.leaveType
                            ),
                            reason = safeData.reason,
                            assignee = LeaveAssigneeUi(
                                name = safeData.assignee,
                                value = safeData.assignee
                            )
                        )
                    }
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
        val startDateError = requiredValidationUseCase(state.value.startDate?.displayValue)
        val endDateError = requiredValidationUseCase(state.value.endDate?.displayValue)
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
                        startDate = state.value.startDate?.displayValue ?: "",
                        endDate = state.value.endDate?.displayValue ?: "",
                        leaveDuration = state.value.leaveDuration?.value ?: "",
                        leaveType = state.value.leaveType?.value ?: "",
                        reason = state.value.reason,
                        assignee = state.value.assignee?.value ?: ""
                    )
                )
            }
        }
    }

    private fun fetchAssignee() = viewModelScope.launch {
        _state.update {
            it.copy(
                isAssigneeLoading = true
            )
        }
        leaveAssigneeUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isAssigneeLoading = false,
                    leaveAssigneeList = data.map { it ->
                        LeaveAssigneeUi(
                            name = it.fullName,
                            value = it.fullName
                        )
                    }
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
                    leaveTypeList = data.map { it ->
                        LeaveTypeUi(
                            name = it.typeName,
                            value = it.typeName
                        )
                    }
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
}