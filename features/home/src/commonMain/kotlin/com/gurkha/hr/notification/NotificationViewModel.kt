package com.gurkha.hr.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.notification.notificationData.model.NotificationData
import com.gurkha.hr.domain.notification.notificationData.useCase.NotificationUseCase
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.notification.model.NotificationState
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
            getAllNotifications()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationState()
        )

    private fun getAllNotifications() = viewModelScope.launch {
        _state.update {
            it.copy(
                isNotificationLoading = true
            )
        }
        notificationUseCase().onSuccess { data ->
            val groupedNotifications = data.groupByTo(LinkedHashMap()) { it.actionDate }

            _state.update {
                it.copy(
                    isNotificationLoading = false,
                    notificationGrouped = groupedNotifications
                )
            }
        }.onError {
            _state.update {
                it.copy(
                    isNotificationLoading = false
                )
            }
        }
    }
}