package com.gurkha.hr.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.app.usecase.FetchUserThemeModeUseCase
import com.gurkha.hr.res.theme.ThemeMode
import com.gurkha.hr.splashscreen.model.AppThemeAction
import com.gurkha.hr.splashscreen.model.AppThemeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    fetchUserThemeModeUseCase: FetchUserThemeModeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AppThemeState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = AppThemeState()
    )

    init {
        viewModelScope.launch {
            fetchUserThemeModeUseCase().collect { userInfo ->
                _state.update {
                    it.copy(userThemeMode = ThemeMode.get(userInfo.userThemeMode))
                }
            }
        }
    }

    fun onAction(action: AppThemeAction) {
       
    }

}