package com.gurkha.hr.leave.leaveRequestPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.textField.DateData
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenAction
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenState
import com.gurkha.model.leave_request.LeaveRequestData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaveRequestScreenViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LeaveRequestScreenState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LeaveRequestScreenState()
    )

    //startDate: String, endDate: String, leaveDuration: String, leaveType: String, reason: String
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
                            leaveDuration = safeData.leaveDuration,
                            leaveType = safeData.leaveType,
                            reason = safeData.reason
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
        }
    }

    private fun submit() = viewModelScope.launch {
        val startDateError = requiredValidationUseCase(state.value.startDate?.displayValue)
        val endDateError = requiredValidationUseCase(state.value.endDate?.displayValue)
        val leaveDurationError = requiredValidationUseCase(state.value.leaveDuration)
        val leaveTypeError = requiredValidationUseCase(state.value.leaveType)
        val reasonError = requiredValidationUseCase(state.value.reason)

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
                        reasonError = null
                    )
                }

                _dataChannel.send(
                    LeaveRequestData(
                        startDate = state.value.startDate?.displayValue ?: "",
                        endDate = state.value.endDate?.displayValue ?: "",
                        leaveDuration = state.value.leaveDuration,
                        leaveType = state.value.leaveType,
                        reason = state.value.reason
                    )
                )
            }
        }
    }
}