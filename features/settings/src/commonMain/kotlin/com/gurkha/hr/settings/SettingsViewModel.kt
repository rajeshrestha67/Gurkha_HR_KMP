package com.gurkha.hr.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.device_info.getDeviceUniqueIdentifier
import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.domain.biometric.useCase.BiometricRequestUseCase
import com.gurkha.hr.domain.settings.usecase.UpdateUserLanguageUseCase
import com.gurkha.hr.domain.settings.usecase.UpdateUserThemeUseCase
import com.gurkha.hr.domain.token.usecase.FetchTokenAllValueUseCase
import com.gurkha.hr.domain.token.usecase.UpdateBiometricEnableUseCase
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.res.theme.EPRLanguage
import com.gurkha.hr.res.theme.ThemeMode
import com.gurkha.hr.settings.model.settings.SettingList
import com.gurkha.hr.settings.model.settings.SettingsScreenAction
import com.gurkha.hr.settings.model.settings.SettingsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SettingsViewModel(
    private val updateUserThemeUseCase: UpdateUserThemeUseCase,
    private val updateUserLanguageUseCase: UpdateUserLanguageUseCase,
    private val fetchTokenAllValueUseCase: FetchTokenAllValueUseCase,
    private val updateBiometricEnableUseCase: UpdateBiometricEnableUseCase,
    private val biometricRequestUseCase: BiometricRequestUseCase
) : ViewModel() {

    var bioToken: String? = null

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
                if (action.enable) {
                    updateEnabledBiometric(true)
                } else {
                    println("elseExecuted")
                    bioToken = null
                    updateEnabledBiometric(false)
                }
            }

            is SettingsScreenAction.OnThemeSelected -> {
                updateTheme(theme = action.theme)
            }

            is SettingsScreenAction.OnLanguageSelected -> {
                updateLanguage(language = action.language)
            }

            is SettingsScreenAction.OnIsAvailableCheck -> {
                _state.update {
                    it.copy(
                        items = _state.value.items.filter { item ->
                            if (item == SettingList.Biometric)
                                action.isAvailable
                            else
                                true
                        },
                        isAvailable = action.isAvailable
                    )
                }
            }
        }
    }

    private fun updateTheme(theme: ThemeMode) = viewModelScope.launch {
        updateUserThemeUseCase(theme.value)
    }

    private fun updateLanguage(language: EPRLanguage) = viewModelScope.launch {
        updateUserLanguageUseCase(language.langCode)
    }

    private fun getBioData() = viewModelScope.launch {
        fetchTokenAllValueUseCase().collect { token ->
            _state.update {
                it.copy(
                    biometricEnabled = token.isBiometricEnable,
                )
            }
            bioToken = token.biometricToken ?: generateRandomUUid()
        }
    }

    private fun updateEnabledBiometric(
        isEnable: Boolean
    ) = viewModelScope.launch {
        if (isEnable) {
            //first make the api call first and update the value in the local
            biometricRequestUseCase(
                uid = getDeviceUniqueIdentifier(),
                biometricToken = bioToken ?: ""
            ).onSuccess {
                val token = fetchTokenAllValueUseCase().firstOrNull() ?: Token()
                updateBiometricEnableUseCase(
                    token.copy(
                        isBiometricEnable = isEnable,
                        biometricToken = bioToken
                    )
                )
            }
        } else {
            println("falase $isEnable")
            //if disable then only update the value in the local
            val token = fetchTokenAllValueUseCase().firstOrNull() ?: Token()
            updateBiometricEnableUseCase(
                token.copy(
                    isBiometricEnable = isEnable,
                    biometricToken = bioToken
                )
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun generateRandomUUid(): String {
        val randomUuid = Uuid.random().toString()
        return randomUuid
    }

}