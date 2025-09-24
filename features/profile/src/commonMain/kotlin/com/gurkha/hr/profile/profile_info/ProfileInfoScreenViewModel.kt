package com.gurkha.hr.profile.profile_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfoScreenState
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfoViewAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProfileInfoScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProfileInfoScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileInfoScreenState()
    )

    fun action(action: ProfileInfoViewAction) {
        when (action) {
            is ProfileInfoViewAction.OnFetchData -> {
//                fetchProfileInfo()
            }
            is ProfileInfoViewAction.OnItemSelected -> {
                _state.update {
                    it.copy(
                        selectedTab = action.index
                    )
                }
            }
        }
    }

}