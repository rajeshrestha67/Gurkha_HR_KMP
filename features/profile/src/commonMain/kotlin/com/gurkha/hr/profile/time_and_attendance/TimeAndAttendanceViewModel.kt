package com.gurkha.hr.profile.time_and_attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.timeAndAttendance.usecase.TimeAndAttendanceUseCase
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.time_and_attendance_screen.TimeAndAttendanceState
import com.gurkha.hr.profile.model.time_and_attendance_screen.TimeAndAttendanceViewAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimeAndAttendanceViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase,
    private val timeAndAttendanceUseCase: TimeAndAttendanceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TimeAndAttendanceState())
    val state = _state

        .onStart {
            onFetchData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TimeAndAttendanceState()
        )

    private fun onFetchData(
        fromDate: String = "",
        toDate: String = ""
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true,
            )
        }
        timeAndAttendanceUseCase(
            toDate = "",
            fromDate = ""
        ).onSuccess { data ->
            _state.update {
                it.copy(
                    isLoading = false,
                    timeAndAttendanceList = data
                )
            }
        }
    }

    fun onAction(action: TimeAndAttendanceViewAction) {
        when (action) {
            is TimeAndAttendanceViewAction.fromDate -> {
                _state.update {
                    it.copy(
                        fromDate = action.date,
                        fromDateError = null
                    )
                }
            }

            is TimeAndAttendanceViewAction.toDate -> {
                _state.update {
                    it.copy(
                        toDate = action.date,
                        toDateError = null

                    )
                }
            }

            TimeAndAttendanceViewAction.Submit -> {
                submit()
            }
        }
    }

    private fun submit() = viewModelScope.launch {
        val fromDate = state.value.fromDate?.displayValueAD ?: ""
        val toDate = state.value.toDate?.displayValueAD ?: ""

        val fromDateError = requiredValidationUseCase(state.value.fromDate?.displayValueAD)
        val toDateError = requiredValidationUseCase(state.value.toDate?.displayValueAD)

        when {
            fromDateError != null -> {
                _state.update {
                    it.copy(
                        fromDateError = fromDateError
                    )
                }
            }

            toDateError != null -> {
                _state.update {
                    it.copy(
                        toDateError = toDateError
                    )
                }
            }

            else -> {
                _state.update {
                    it.copy(
                        fromDateError = null,
                        toDateError = null
                    )
                }
            }
        }

        onFetchData(
            fromDate = fromDate,
            toDate = toDate
        )
    }
}