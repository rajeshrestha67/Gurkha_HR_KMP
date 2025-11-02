package com.gurkha.hr.profile.time_and_attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.date.getMonthStartAndEndDate
import com.gurkha.hr.domain.attendance.attendanceReport.usecase.AttendanceUseCase
import com.gurkha.hr.domain.form.RequiredValidationUseCase
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
    private val timeAndAttendanceUseCase: AttendanceUseCase,
    private val calendarModel: CalendarModel
) : ViewModel() {
    val datePair = calendarModel.getMonthStartAndEndDate()

    private val _state = MutableStateFlow(TimeAndAttendanceState())
    val state = _state

        .onStart {
            onFetchData(isRefreshing = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TimeAndAttendanceState()
        )

    private fun onFetchData(
        isRefreshing: Boolean,
        fromDate: String = datePair.first,
        toDate: String = datePair.second,
        attendanceStatus: String? = null,
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true,
                isRefreshing = isRefreshing
            )
        }
        timeAndAttendanceUseCase(
            toDate = toDate,
            fromDate = fromDate,
            attendanceStatus =  attendanceStatus,
        ).onSuccess { data ->
            _state.update {
                it.copy(
                    isLoading = false,
                    timeAndAttendanceList = data,
                    isRefreshing = false
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

           is TimeAndAttendanceViewAction.Submit -> {
                _state.update { it.copy(
                    employeeId = action.employeeId
                ) }
                submit()
            }

            TimeAndAttendanceViewAction.OnRefresh -> {
                onFetchData(isRefreshing = true)
            }
        }
    }

    private fun submit() = viewModelScope.launch {
        val fromDate = _state.value.fromDate
        val toDate = _state.value.toDate
        val attendanceStatus = ""
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
            fromDate = fromDate?.displayValueAD ?: "",
            toDate = toDate?.displayValueAD ?: "",
            attendanceStatus = attendanceStatus,
            isRefreshing = true
        )

    }
}