package com.gurkha.hr.attendance

//import com.gurkha.hr.domain.attendance.attendanceRequest.useCase.AttendanceRequestUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.domain.attendance.attendanceStatus.model.AttendanceStatusData
import com.gurkha.hr.domain.attendance.attendanceStatus.useCase.AttendanceStatusUseCase
import com.gurkha.hr.domain.attendance.attendanceSummary.useCase.AttendanceSummaryUseCase
import com.gurkha.hr.model.attendanceScreen.AttendanceAction
import com.gurkha.hr.model.attendanceScreen.AttendanceScreenState
import com.gurkha.hr.model.attendanceScreen.TabItemsEnums
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class AttendanceViewModel(
    private val attendanceStatusUseCase: AttendanceStatusUseCase,
    private val attendanceSummaryUseCase: AttendanceSummaryUseCase,
    private val calendarModel: CalendarModel,
) : ViewModel() {
    private val _state = MutableStateFlow(AttendanceScreenState())

    private val _successChannel = Channel<String>()
    val successChannel = _successChannel.receiveAsFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    val state = _state
        .onStart {
            fetchAttendanceSummary()
            fetchAttendance(
                attendanceStatus = TabItemsEnums.PENDING,
                employeeName = "",
                isSelf = "Y"
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AttendanceScreenState()
        )

    fun onAction(action: AttendanceAction) {
        when (action) {
            is AttendanceAction.OnUpdateAttendanceJsonData -> {
                action.json?.let {
                    val data: AttendanceRequestData =
                        Json.decodeFromString<AttendanceRequestData>(action.json)

                    updateAttendanceRequestData(data = data)
                }
            }

            is AttendanceAction.OnStatusChange -> {
                _state.update {
                    it.copy(
                        selectedTab = action.status,
                        currentTapItem = when (action.status) {
                            TabItemsEnums.PENDING -> it.pendingTapItem
                            TabItemsEnums.APPROVED -> it.approvedTapItem
                            else -> it.rejectedTapItem
                        }
                    )
                }
                if (state.value.currentTapItem.result.isEmpty()) {
                    fetchAttendance(
                        attendanceStatus = state.value.selectedTab,
                        employeeName = state.value.employeeName,
                        isSelf = state.value.isSelf
                    )
                }
            }
        }
    }

    private fun fetchAttendance(
        attendanceStatus: TabItemsEnums,
        employeeName: String,
        isSelf: String
    ) = viewModelScope.launch {
        _state.update {
            when (attendanceStatus) {
                TabItemsEnums.PENDING -> {
                    it.copy(
                        pendingTapItem = it.pendingTapItem.copy(isLoading = true)
                    )
                }

                TabItemsEnums.APPROVED -> {
                    it.copy(
                        approvedTapItem = it.approvedTapItem.copy(isLoading = true)
                    )
                }

                TabItemsEnums.REJECTED -> {
                    it.copy(
                        rejectedTapItem = it.rejectedTapItem.copy(isLoading = true)
                    )
                }
            }
        }
        attendanceStatusUseCase(
            attendanceStatus = attendanceStatus.value,
            employeeName = employeeName,
            isSelf = isSelf
        ).onSuccess { data ->
            when (attendanceStatus) {
                TabItemsEnums.PENDING -> {
                    _state.update {
                        it.copy(
                            pendingTapItem = it.pendingTapItem.copy(
                                isLoading = false,
                                result = data
                            )
                        )
                    }
                }

                TabItemsEnums.APPROVED -> {
                    _state.update {
                        it.copy(
                            approvedTapItem = it.approvedTapItem.copy(
                                isLoading = false,
                                result = data
                            )
                        )
                    }
                }

                else -> {
                    _state.update {
                        it.copy(
                            rejectedTapItem = it.rejectedTapItem.copy(
                                isLoading = false,
                                result = data
                            )
                        )
                    }
                }
            }
            _state.update {
                it.copy(
                    currentTapItem = when (attendanceStatus) {
                        TabItemsEnums.PENDING -> it.pendingTapItem
                        TabItemsEnums.APPROVED -> it.approvedTapItem
                        else -> it.rejectedTapItem
                    }
                )
            }
        }.onError {
            when (attendanceStatus) {
                TabItemsEnums.PENDING -> {
                    _state.update {
                        it.copy(
                            pendingTapItem = it.pendingTapItem.copy(
                                isLoading = false,
                            )
                        )
                    }
                }

                TabItemsEnums.APPROVED -> {
                    _state.update {
                        it.copy(
                            approvedTapItem = it.approvedTapItem.copy(
                                isLoading = false,
                            )
                        )
                    }
                }

                else -> {
                    _state.update {
                        it.copy(
                            rejectedTapItem = it.rejectedTapItem.copy(
                                isLoading = false,
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updateAttendanceRequestData(
        data: AttendanceRequestData
    ) = viewModelScope.launch {
        _state.update { currentState ->
            val updatedPendingList = currentState.pendingTapItem.result + AttendanceStatusData(
                requestedDate = data.date,
                clockInTime = data.clockInTime,
                clockOutTime = data.clockOutTime,
                requestRemarks = data.remarks,
                assignedTo = data.assigneeId,
                approvedRemarks = "",
                lastModifiedBy = "",
                lastModifiedDate = "",
                attendanceStatus = TabItemsEnums.PENDING.value
            )

            val updatedPendingTab =
                currentState.pendingTapItem.copy(result = updatedPendingList)

            currentState.copy(
                pendingTapItem = updatedPendingTab,
                currentTapItem = if (currentState.selectedTab == TabItemsEnums.PENDING) updatedPendingTab
                else currentState.currentTapItem,
                isRequestingAttendance = false,
            )
        }
    }

    private fun fetchAttendanceSummary() = viewModelScope.launch {
        _state.update {
            it.copy(
                isFetchingAttendanceSummary = true
            )
        }

        attendanceSummaryUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isFetchingAttendanceSummary = false,
                    attendanceGridOptions = _state.value.attendanceGridOptions.mapIndexed { index, item ->
                        when (index) {
                            0 -> {
                                item.copy(
                                    days = data.forgottenAttendanceDaysCount.toString()
                                )
                            }
                            1 -> {
                                item.copy(
                                    days = data.approvedAttendanceCount.toString()
                                )
                            }
                            2 -> {
                                item.copy(
                                    days = data.pendingAttendanceCount.toString()
                                )
                            }
                            3 -> {
                                item.copy(
                                    days = data.rejectedAttendanceCount.toString()
                                )
                            }
                            else -> {
                                item
                            }
                        }
                    }
                )
            }
        }
    }
}