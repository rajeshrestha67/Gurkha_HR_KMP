package com.gurkha.hr.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.notification.notificationData.useCase.NotificationUseCase
import com.gurkha.hr.model.notification.NotificationAction
import com.gurkha.hr.model.notification.NotificationState
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.DataError
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationUseCase: NotificationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())

    val state = _state
        .onStart {
            getAllNotifications(isRefreshing = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationState()
        )

    fun onAction(action: NotificationAction){
        when (action){
            is NotificationAction.OnRefresh->{
                getAllNotifications(isRefreshing = true)
            }
        }
    }

    private fun getAllNotifications(isRefreshing: Boolean) = viewModelScope.launch {
        _state.update {
            it.copy(
                isRefreshing = isRefreshing,
                isNotificationLoading = true
            )
        }
        val notificationAsync = async {
            notificationUseCase()
        }
        val callAsync = async {
            call()
        }

        val notificationResult = notificationAsync.await()
        val callResult = callAsync.await()
        callResult.onSuccess {
            notificationResult.onSuccess { data ->
                val grouped = data.groupByTo(LinkedHashMap()) { it.actionDate }
                _state.update {
                    it.copy(
                        isNotificationLoading = false,
                        notificationGrouped = grouped
                    )
                }
            }.onError {
                _state.update {
                    it.copy(
                        isNotificationLoading = false,
                    )
                }
            }
        }.onError {
            _state.update {
                it.copy(
                    isNotificationLoading = false,
                )
            }
        }
    }


    suspend fun call(): ERPResult<String, DataError> {
        delay(10000)
        return ERPResult.Error(DataError.LocalError.NoData)
    }

    private fun refresh() = viewModelScope.launch {
        _state.update {
            it.copy(
                isRefreshing = true
            )
        }
        getAllNotifications(isRefreshing = true)
        _state.update {
            it.copy(
                isRefreshing = false
            )
        }
    }
}