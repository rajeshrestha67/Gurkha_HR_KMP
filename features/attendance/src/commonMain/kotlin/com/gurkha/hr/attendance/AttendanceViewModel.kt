package com.gurkha.hr.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.attendance.model.AttendanceAction
import com.gurkha.hr.attendance.model.AttendanceScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendanceViewModel : ViewModel() {
    private val _state = MutableStateFlow(AttendanceScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AttendanceScreenState()
    )

    fun onAction(action: AttendanceAction) {
        when(action){
            is AttendanceAction.OnStatusChange->{
                _state.update {
                    it.copy(
                        selectedTab = action.status
                    )
                }
            }
        }
    }



}