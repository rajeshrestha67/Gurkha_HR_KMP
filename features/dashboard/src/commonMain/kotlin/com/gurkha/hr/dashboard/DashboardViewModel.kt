package com.gurkha.hr.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.dashboard.model.DashboardScreenAction
import com.gurkha.hr.dashboard.model.DashboardScreenState
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.domain.userDetail.usecase.FetchRemoteUserDetailUseCase
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userDetailUseCase: FetchRemoteUserDetailUseCase,
    private val authState: AuthState
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardScreenState())

    val sessionExpired: StateFlow<Boolean> = authState.sessionExpired

    val state = _state
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


}