package com.gurkha.hr.attendanceRequestScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.attendanceRequestScreen.model.AttendanceRequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AttendanceRequestViewModel: ViewModel() {
    private val _state = MutableStateFlow(AttendanceRequestState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AttendanceRequestState()
    )
}