package com.gurkha.hr.profile.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.profile.model.profile_screen.ProfileScreenState
import com.gurkha.hr.profile.model.profile_screen.ProfileViewAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProfileScreenViewModel: ViewModel() {
    private val _state = MutableStateFlow(ProfileScreenState())
    val state = _state
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileScreenState()
    )

    fun action(action: ProfileViewAction){
        when(action){
            is ProfileViewAction.SetCurrentItems -> {
                setCurrentPage(action.page)
            }
        }

    }
    private fun setCurrentPage(page: String){
        _state.update {
            it.copy(selected = page)
        }
    }
}