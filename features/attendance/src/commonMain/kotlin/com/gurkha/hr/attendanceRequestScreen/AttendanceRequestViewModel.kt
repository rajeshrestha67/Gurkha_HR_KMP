package com.gurkha.hr.attendanceRequestScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.attendance.attendanceRequest.useCase.AttendanceRequestUseCase
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.leave.leaveAssignee.model.toUiList
import com.gurkha.hr.domain.leave.leaveAssignee.usecase.AssigneeUseCase
import com.gurkha.hr.model.attendanceRequestScreen.AttendanceRequestAction
import com.gurkha.hr.model.attendanceRequestScreen.AttendanceRequestState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestData
import com.gurkha.model.network.toErrorMessage
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
    private val attendanceRequestUseCase: AttendanceRequestUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AttendanceRequestState())
    private val _dataChannel = Channel<AttendanceRequestData?>()
    val dataChannel = _dataChannel.receiveAsFlow()

    private val _successChannel = Channel<String?>()
    val successChannel = _successChannel.receiveAsFlow()

    private val _errorChannel = Channel<String?>()
    val errorChannel = _errorChannel.receiveAsFlow()
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
                        clockInOutError = null
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

            is AttendanceRequestAction.OnUpdateAttendanceRequestData -> {
                _state.update {
                    it.copy(
                        attendanceRequestData = action.data
                    )
                }
            }

            is AttendanceRequestAction.OnRadioOptionChange -> {
                _state.update {
                    it.copy(
                        selectedOption = action.option
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
                    assigneeList = data.toUiList()
                )
            }
        }
    }

    private fun submit() = viewModelScope.launch {
        val dateError = requiredValidationUseCase(state.value.date?.displayValueAD)
        val assigneeError = requiredValidationUseCase(state.value.assignee?.value)
        val reasonError = requiredValidationUseCase(state.value.reason)

        val hasClockInOrOut =
            !state.value.clockInTime.isNullOrBlank() || !state.value.clockOutTime.isNullOrBlank()
        val clockInOutError = if (hasClockInOrOut) null else requiredValidationUseCase(null)


        when {
            dateError != null -> {
                _state.update {
                    it.copy(
                        dateError = dateError
                    )
                }
            }

            clockInOutError != null -> {
                _state.update {
                    it.copy(
                        clockInOutError = clockInOutError
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
                        clockInOutError = null,
                        clockOutTimeError = null,
                        assigneeError = null,
                        reasonError = null,
                    )

                }
                val data = AttendanceRequestData(
                    assigneeId = state.value.assignee?.value.toString(),
                    clockInTime = state.value.clockInTime ?: "",
                    clockOutTime = state.value.clockOutTime ?: "",
                    date = state.value.date?.displayValueAD ?: "",
                    remarks = state.value.reason.toString()
                )
                _dataChannel.send(data)
                requestAttendance()
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

    private fun requestAttendance() = viewModelScope.launch {
        _state.update {
            it.copy(
                isRequestingAttendance = true
            )
        }
        attendanceRequestUseCase(
            assigneeId = state.value.assignee?.value?.toInt() ?: 0,
            clockInTime = state.value.clockInTime,
            clockOutTime = state.value.clockOutTime,
            date = state.value.date?.displayValueAD ?: "",
            remarks = state.value.reason.toString()
        ).onSuccess { data ->
            _state.update {
                it.copy(
                    isRequestingAttendance = false
                )
            }
            _successChannel.send(data.message)
            _state.update {
                it.copy(
                    date = null,
                    clockInTime = null,
                    clockOutTime = null,
                    assignee = null,
                    reason = null
                )
            }
        }.onError { error ->
            _state.update {
                it.copy(
                    isRequestingAttendance = false
                )
            }
            _errorChannel.send(error.toErrorMessage())
        }

    }
}
