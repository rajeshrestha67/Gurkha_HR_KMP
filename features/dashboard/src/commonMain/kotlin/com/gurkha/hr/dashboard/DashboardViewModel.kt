package com.gurkha.hr.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.device_info.getDeviceUniqueIdentifier
import com.gurkha.hr.dashboard.model.DashboardScreenAction
import com.gurkha.hr.dashboard.model.DashboardScreenState
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.domain.token.usecase.FetchTokenAllValueUseCase
import com.gurkha.hr.domain.token.usecase.PostFcmTokenUseCase
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val authState: AuthState,
    private val postFcmTokenUseCase: PostFcmTokenUseCase,
    private val fetchTokenAllValueUseCase: FetchTokenAllValueUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardScreenState())

    var sessionExpired: StateFlow<Boolean> = authState.sessionExpired
    val state = _state
        .onStart {
            postFcmToken()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardScreenState()
        )

    fun action(action: DashboardScreenAction) {
        when (action) {
            is DashboardScreenAction.OnChangeScreen -> {
                navigateTo(action.route)
            }

            is DashboardScreenAction.Reset -> {
                authState.reset()
            }
        }
    }

    private fun navigateTo(route: DashboardRoute) {
        _state.update {
            it.copy(currentScreen = route)
        }
    }

    private fun postFcmToken() = viewModelScope.launch {
        val fcmToken = fetchTokenAllValueUseCase().firstOrNull()?.fcmToken

        postFcmTokenUseCase(
            fcmToken = fcmToken ?: "",
            uid = getDeviceUniqueIdentifier()
        ).onSuccess {
            AppLogger.d(tag = TAG, message = "successFully posted the fcm token")
        }.onError { error ->
            AppLogger.e(tag = TAG, message = "Error posted the fcm token", error)

        }
    }

    companion object {
        const val TAG = "DashboardViewModel"
    }


}