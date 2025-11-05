package com.gurkha.hr.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.paging.model.PagingListState
import com.gurkha.hr.components.paging.model.isLoading
import com.gurkha.hr.components.paging.model.isPaging
import com.gurkha.hr.domain.notification.notificationCount.useCase.NotificationCountUseCase
import com.gurkha.hr.domain.notification.notificationData.useCase.NotificationUseCase
import com.gurkha.hr.model.notification.NotificationAction
import com.gurkha.hr.model.notification.NotificationState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.min

class NotificationViewModel(
    private val notificationUseCase: NotificationUseCase,
    private val notificationCountUseCase: NotificationCountUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())
    var totalCount: Int = 0
    
    var offSet: Int = 0

    val state = _state
        .onStart {
            getAllNotificationsAndMeta(isRefreshing = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationState()
        )

    fun onAction(action: NotificationAction) {
        when (action) {
            is NotificationAction.OnRefresh -> {
                getAllNotificationsAndMeta(isRefreshing = true)
            }

            is NotificationAction.OnPagination -> {
                if (offSet >= totalCount) {
                    return
                }

                if (_state.value.pagingState.isPaging() || _state.value.pagingState.isLoading()) {
                    return
                }

                getNotifications(pagingState = PagingListState.Paging)
            }
        }
    }

    private fun getAllNotificationsAndMeta(isRefreshing: Boolean) = viewModelScope.launch {
        _state.update {
            it.copy(
                isRefreshing = isRefreshing,
                isNotificationLoading = true
            )
        }
        val notificationAsync = async {
            notificationUseCase(offset = 0)
        }

        val notificationCountAsync = async {
            notificationCountUseCase()
        }


        val notificationResult = notificationAsync.await()
        val callResult = notificationCountAsync.await()
        callResult.onSuccess { countData ->
            totalCount = countData.count
            notificationResult.onSuccess { data ->
                val grouped = data.groupByTo(LinkedHashMap()) { it.actionDate }
                updateOffset()
                _state.update {
                    it.copy(
                        isNotificationLoading = false,
                        notificationGrouped = grouped,
                        notifications = data,
                        pagingState = PagingListState.Loaded
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


    private fun updateOffset() {
        offSet += if (offSet != 0) {
            min(totalCount - offSet, OFFSET)
        } else {
            OFFSET
        }
    }

    private fun getNotifications(
        pagingState: PagingListState
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                pagingState = pagingState
            )
        }
        notificationUseCase(offset = offSet).onSuccess { data ->

            val updatedList = _state.value.notifications.toMutableList()
            updatedList.addAll(data)
            val grouped = updatedList.groupByTo(LinkedHashMap()) { it.actionDate }
            updateOffset()
            _state.update {
                it.copy(
                    isNotificationLoading = false,
                    notificationGrouped = grouped,
                    notifications = updatedList,
                    pagingState = PagingListState.Loaded
                )
            }

        }.onError {
            _state.update {
                it.copy(
                    isNotificationLoading = false,
                )
            }
        }
    }

    companion object {
        private const val OFFSET = 10
    }
}