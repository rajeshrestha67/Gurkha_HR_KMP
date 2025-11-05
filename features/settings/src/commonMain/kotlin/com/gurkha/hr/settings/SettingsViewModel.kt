package com.gurkha.hr.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.settings.usecase.UpdateUserLanguageUseCase
import com.gurkha.hr.domain.settings.usecase.UpdateUserThemeUseCase
import com.gurkha.hr.domain.token.usecase.FetchBiometricEnableUseCase
import com.gurkha.hr.domain.token.usecase.UpdateBiometricEnableUseCase
import com.gurkha.hr.res.theme.EPRLanguage
import com.gurkha.hr.res.theme.ThemeMode
import com.gurkha.hr.settings.model.settings.SettingsScreenAction
import com.gurkha.hr.settings.model.settings.SettingsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val updateUserThemeUseCase: UpdateUserThemeUseCase,
    private val updateUserLanguageUseCase: UpdateUserLanguageUseCase,
    private val fetchBiometricEnableUseCase: FetchBiometricEnableUseCase,
    private val updateBiometricEnableUseCase:UpdateBiometricEnableUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsScreenState())
    val state = _state
        .onStart {
            getBioData()
        }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsScreenState()
    )

    fun onAction(action: SettingsScreenAction) {
        when (action) {
            is SettingsScreenAction.OnBiometricStatusChange -> {
                _state.update {
                    it.copy(
                        biometricEnabled = action.enable
                    )
                }
                updateEnabledBiometric(action.enable)
            }

            is SettingsScreenAction.OnThemeSelected -> {
                updateTheme(theme = action.theme)
            }

            is SettingsScreenAction.OnLanguageSelected -> {
                updateLanguage(language = action.language)
            }
        }
    }

    private fun updateTheme(theme: ThemeMode) = viewModelScope.launch {
        updateUserThemeUseCase(theme.value)
    }

    private fun updateLanguage(language: EPRLanguage) = viewModelScope.launch {
        updateUserLanguageUseCase(language.langCode)
    }

    private fun getBioData()=viewModelScope.launch {
        fetchBiometricEnableUseCase().collect { token ->
            _state.update {
                it.copy(

                )
            }
        }
    }

    private fun updateEnabledBiometric(
        isEnable : Boolean
    )=viewModelScope.launch {
        updateBiometricEnableUseCase(isEnable = true)
    }
}