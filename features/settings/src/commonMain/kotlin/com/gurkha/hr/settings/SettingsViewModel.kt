package com.gurkha.hr.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.settings.usecase.UpdateUserThemeUseCase
import com.gurkha.hr.res.theme.ThemeMode
import com.gurkha.hr.settings.model.settings.SettingsScreenAction
import com.gurkha.hr.settings.model.settings.SettingsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val updateUserThemeUseCase: UpdateUserThemeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = SettingsScreenState()
    )

    fun onAction(action: SettingsScreenAction) {
        when (action) {
            is SettingsScreenAction.OnNotificationStatusChange -> {
                _state.update {
                    it.copy(
                        notificationEnabled = action.enable
                    )
                }
            }

            is SettingsScreenAction.OnThemeSelected -> {
                updateTheme(theme = action.theme)
            }
        }
    }

    private fun updateTheme(theme: ThemeMode) = viewModelScope.launch {
        updateUserThemeUseCase(theme.value)
    }
}