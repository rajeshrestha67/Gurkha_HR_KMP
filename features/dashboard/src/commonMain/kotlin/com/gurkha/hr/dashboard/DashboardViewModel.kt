package com.gurkha.hr.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.device_info.getDeviceInfo
import com.gurkha.hr.dashboard.model.DashboardScreenAction
import com.gurkha.hr.dashboard.model.DashboardScreenState
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.domain.token.usecase.FetchTokenAllValueUseCase
import com.gurkha.hr.domain.token.usecase.FetchTokenUseCase
import com.gurkha.hr.domain.token.usecase.PostFcmTokenUseCase
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userDetailUseCase: FetchUserDetailUseCase,
    private val authState: AuthState,
    private val fetchTokenUseCase: FetchTokenUseCase,
    private val postFcmTokenUseCase: PostFcmTokenUseCase,
    private val fetchTokenAllValueUseCase: FetchTokenAllValueUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardScreenState())

    private val uid = getDeviceInfo().uid

    var sessionExpired: StateFlow<Boolean> = authState.sessionExpired
    val state = _state
        .onStart {
            postFcmToken()
            println("repository.token ${fetchTokenUseCase()}")
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

            is DashboardScreenAction.OnFetchCurrentUser -> {
//                currentUserDetailFetch()
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

    private fun currentUserDetailFetch() = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        userDetailUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    userDetail = data,
                    isLoading = false,
                )
            }
        }.onError {
            _state.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun postFcmToken()=viewModelScope.launch {
        val fcmToken = fetchTokenAllValueUseCase().firstOrNull()?.fcmToken
        postFcmTokenUseCase(
            fcmToken = fcmToken ?: "",
            uid = uid
        ).onSuccess {
            AppLogger.d(tag = TAG, message = "successFully posted the fcm token")
        }.onError {
            AppLogger.e(tag = TAG, message = "successFully posted the fcm token")

        }
    }

    companion object{
        const val TAG = "DashboardViewModel"
    }


}