package com.gurkha.hr.attendanceRequestScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.model.attendanceRequestScreen.AttendanceRequestAction
import com.gurkha.hr.model.attendanceRequestScreen.AttendanceRequestState
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.leave.leaveAssignee.model.toUiList
import com.gurkha.hr.domain.leave.leaveAssignee.usecase.AssigneeUseCase
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendanceRequestViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase,
    private val assigneeUseCase: AssigneeUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AttendanceRequestState())
    private val _dataChannel = Channel<AttendanceRequestData?>()
    val dataChannel = _dataChannel.receiveAsFlow()
    val state = _state
        .onStart {
            fetchAssignee()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AttendanceRequestState()
        )

    fun onAction(action: AttendanceRequestAction) {
        when (action) {
            is AttendanceRequestAction.OnDateChange -> {
                _state.update {
                    it.copy(
                        date = action.date,
                        dateError = null
                    )
                }
            }

            is AttendanceRequestAction.OnClockInTimeChange -> {
                _state.update {
                    it.copy(
                        clockInTime = action.clockInTime,
                        clockInTimeError = null
                    )
                }
            }

            is AttendanceRequestAction.OnClockOutTimeChange -> {
                _state.update {
                    it.copy(
                        clockOutTime = action.clockOutTime,
                        clockOutTimeError = null
                    )
                }
            }

            is AttendanceRequestAction.OnAssigneeChange -> {
                _state.update {
                    it.copy(
                        assignee = action.assignee,
                        assigneeError = null
                    )
                }
            }

            is AttendanceRequestAction.OnReasonChange -> {
                _state.update {
                    it.copy(
                        reason = action.reason,
                        reasonError = null
                    )
                }
            }

            is AttendanceRequestAction.OnSubmit -> {
                submit()
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
                    assigneeList = data.toUiList()
                )
            }
        }
    }

    private fun submit() = viewModelScope.launch {
        val dateError = requiredValidationUseCase(state.value.date?.displayValue)
        val clockInError = requiredValidationUseCase(state.value.clockInTime)
        val clockOutError = requiredValidationUseCase(state.value.clockOutTime)
        val assigneeError = requiredValidationUseCase(state.value.assignee?.value)
        val reasonError = requiredValidationUseCase(state.value.reason)

        when {
            dateError != null -> {
                _state.update {
                    it.copy(
                        dateError = dateError
                    )
                }
            }

            clockInError != null -> {
                _state.update {
                    it.copy(
                        clockInTimeError = clockInError
                    )
                }
            }

            clockOutError != null -> {
                _state.update {
                    it.copy(
                        clockOutTimeError = clockOutError
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
                        dateError = null,
                        clockInTimeError = null,
                        clockOutTimeError = null,
                        assigneeError = null,
                        reasonError = null
                    )
                }

                _dataChannel.send(
                    AttendanceRequestData(
                        assignedId = state.value.assignee?.value ?: "",
                        date = state.value.date?.displayValue ?: "",
                        clockInTime = state.value.clockInTime ?: "",
                        clockOutTime = state.value.clockOutTime ?: "",
                        remarks = state.value.reason ?: ""
                    )
                )
                _state.update {
                    it.copy(
                        date = null,
                        clockInTime = null,
                        clockOutTime = null,
                        assignee = null,
                        reason = null
                    )
                }
            }
        }

    }
}
