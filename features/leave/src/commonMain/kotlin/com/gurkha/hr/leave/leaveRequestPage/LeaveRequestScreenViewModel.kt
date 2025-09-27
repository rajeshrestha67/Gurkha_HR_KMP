package com.gurkha.hr.leave.leaveRequestPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenAction
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LeaveRequestScreenViewModel : ViewModel() {

    private val _state = MutableStateFlow(LeaveRequestScreenState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LeaveRequestScreenState()
    )

    fun onAction(action: LeaveRequestScreenAction) {
        when (action) {
            is LeaveRequestScreenAction.OnEndDateChange -> {
                _state.update {
                    it.copy(endDate = action.date)
                }
            }

            is LeaveRequestScreenAction.OnLeaveDurationChange -> {
                _state.update {
                    it.copy(leaveDuration = action.leaveDuration)
                }
            }

            is LeaveRequestScreenAction.OnLeaveTypeChange -> {
                _state.update {
                    it.copy(leaveType = action.leaveDuration)
                }
            }

            is LeaveRequestScreenAction.OnReasonChange -> {
                _state.update {
                    it.copy(reason = action.leaveDuration)
                }
            }

            is LeaveRequestScreenAction.OnStartDateChange -> {
                _state.update {
                    it.copy(startDate = action.date)
                }
            }


            is LeaveRequestScreenAction.OnEndDateError -> {
                _state.update {
                    it.copy(
                        endDateError = action.error
                    )
                }
            }

            is LeaveRequestScreenAction.OnLeaveDurationError -> {
                _state.update {
                    it.copy(
                        leaveDurationError = action.error
                    )
                }
            }

            is LeaveRequestScreenAction.OnLeaveTypeError -> {
                _state.update {
                    it.copy(
                        leaveTypeError = action.error
                    )
                }
            }

            is LeaveRequestScreenAction.OnReasonError -> {
                _state.update {
                    it.copy(
                        reasonError = action.error
                    )
                }
            }

            is LeaveRequestScreenAction.OnStartDateError -> {
                _state.update {
                    it.copy(
                        startDateError = action.error
                    )
                }
            }

            LeaveRequestScreenAction.Submit -> {

            }
        }
    }
}