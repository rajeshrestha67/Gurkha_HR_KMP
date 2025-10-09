package com.gurkha.hr.profile.time_and_attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.timeAndAttendance.usecase.TimeAndAttendanceUseCase
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.time_and_attendance_screen.TimeAndAttendanceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimeAndAttendanceViewModel(
    private val timeAndAttendanceUseCase: TimeAndAttendanceUseCase
): ViewModel() {

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

    private fun onFetchData() = viewModelScope.launch {
        _state.update { it.copy(
            isLoading = true,
            )}
        timeAndAttendanceUseCase(
            toDate = "",
            fromDate = ""
        ).onSuccess { data ->
            println("time_and_attendance_data $data")
            _state.update { it.copy(
                isLoading = false,
                timeAndAttendanceList = data
            ) }
        }

    }
}