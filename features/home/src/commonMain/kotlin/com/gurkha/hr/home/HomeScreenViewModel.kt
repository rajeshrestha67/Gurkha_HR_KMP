package com.gurkha.hr.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.date.BSPointer
import com.gurkha.hr.date.DateConverter
import com.gurkha.hr.date.Year
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceData
import com.gurkha.hr.domain.attendance.attendanceReport.usecase.AttendanceUseCase
import com.gurkha.hr.domain.notification.notificationCount.useCase.NotificationCountUseCase
import com.gurkha.hr.domain.notification.notificationData.useCase.NotificationUseCase
import com.gurkha.hr.domain.upComingBirthday.usecase.UpComingBirthdayUseCase
import com.gurkha.hr.domain.upComingEvent.useCase.EventUseCase
import com.gurkha.hr.domain.upComingWorkAnniversaries.useCase.UpComingWorkAnniversaryUseCase
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.model.home.HomeScreenActions
import com.gurkha.hr.model.home.HomeScreenState
import com.gurkha.hr.model.home.RequestItem
import com.gurkha.hr.model.home.RequestType
import com.gurkha.hr.model.home.toUI
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.toErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

class HomeScreenViewModel(
    private val attendanceUseCase: AttendanceUseCase,
    private val userDetailUseCase: FetchUserDetailUseCase,
    private val upComingBirthdayUseCase: UpComingBirthdayUseCase,
    private val upComingWorkAnniversaryUseCase: UpComingWorkAnniversaryUseCase,
    private val eventUseCase: EventUseCase,
    private val calendarModel: CalendarModel,
    private val notificationCountUseCase : NotificationCountUseCase,
    private val notificationUseCase: NotificationUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState(todayBS = calendarModel.today))
    val state = _state
        .onStart {
            fetchCurrentUser()
            fetchUpComingBirthday()
            fetchUpComingWorkAnniversary()
            fetchAttendance()
            fetchCalendarValue()
            fetchUpComingEvents()
            getTotalNotificationCount()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeScreenState(todayBS = calendarModel.today)
        )

    fun onAction(action: HomeScreenActions) {
        when (action) {
            is HomeScreenActions.OnRequestLeave -> {
                TODO()
            }

            is HomeScreenActions.OnCheckInClicked -> {
                TODO()
            }

            is HomeScreenActions.OnRequestAttendance -> {
                TODO()
            }

            is HomeScreenActions.OnSpecificDayClicked -> {
                TODO()
            }

            is HomeScreenActions.OnCheckOutClicked -> TODO()
            is HomeScreenActions.OnNotificationClicked -> {

            }
            is HomeScreenActions.OnSearchedClicked -> TODO()
            is HomeScreenActions.OnDateSelected -> {
                val attendanceData = state.value.attendanceReport.find { data ->
                    val day = getDayFromDate(date = data.date)?.toInt()
                    day == action.day
                }
                _state.update {
                    it.copy(
                        selectedDay = action.day,
                        requests = state.value.requests.updateDuration(
                            attendanceData = attendanceData
                        )
                    )
                }
            }
        }
    }

    private fun List<RequestItem>.updateDuration(attendanceData: AttendanceData?): List<RequestItem> {
        return map { item ->
            attendanceData?.let {
                when (item.type) {
                    RequestType.CheckIn -> item.copy(duration = attendanceData.clockInTime)
                    RequestType.CheckOut -> item.copy(duration = attendanceData.clockOutTime)
                    else -> item
                }
            } ?: item
        }
    }


    private fun fetchCalendarValue() {
        _state.update {
            it.copy(
                calendarData = calendarModel.numberOfDaysInMonth(),
                todayBS = calendarModel.today,
                selectedDay = calendarModel.today.dayOfMonth
            )
        }
    }

    //    fetch the attendance report
    private fun fetchAttendance() = viewModelScope.launch {
        _state.update {
            it.copy(isAttendanceLoading = true)
        }

        val todayDays =
            BSPointer.getNumOfDaysInMonth(
                Year.ofValue(calendarModel.today.year),
                calendarModel.today.month
            )
        val fromDate = DateConverter.bsToAd(
            year = calendarModel.today.year,
            month = calendarModel.today.month,
            day = 1
        ).run {
            "$year-$month-$day"
        }
        val toDate = DateConverter.bsToAd(
            year = calendarModel.today.year,
            month = calendarModel.today.month,
            day = todayDays
        ).run {
            "$year-$month-$day"
        }

        attendanceUseCase(
            fromDate = fromDate,
            toDate = toDate
        ).onSuccess { data ->

            val attendanceData = data.find { data ->
                val day = getDayFromDate(date = data.date)?.toInt()
                day == calendarModel.today.dayOfMonth
            }
            _state.update {
                it.copy(
                    isAttendanceLoading = false,
                    attendanceReport = data,
                    attendanceReportHistory = data.filter { mData ->
                        try {
                            val day = getDayFromDate(date = mData.date)?.toInt()
                            day?.let {
                                val today = calendarModel.today.dayOfMonth
                                it in (today - 7..today)
                            } ?: false
                        } catch (_: Exception) {
                            AppLogger.e(
                                "HomeScreenViewModel",
                                "fetchAttendance date filter: ${mData.date}"
                            )
                            false
                        }
                    }.map { data ->
                        data.toUI()
                    },
                    requests = _state.value.requests.updateDuration(
                        attendanceData = attendanceData
                    )
                )
            }
        }.onError { error ->
            AppLogger.e(
                "HomeScreenViewModel",
                "fetchAttendance date filter: ${error.toErrorMessage()}"
            )
            _state.update {
                it.copy(isAttendanceLoading = false)
            }
        }
    }

    private fun getDayFromDate(date: String): String? {
        if (date.isEmpty()) return null
        val split = date.split("-")
        if (split.size < 3) return null
        return split[2]
    }

    private fun LocalDate.formatDate(): String {
        val day = this.day.toString().padStart(2, '0')
        val month = this.month.number.toString().padStart(2, '0')
        val year = this.year
        return "$year-$month-$day"
    }


    //    fetch the current user details
    private fun fetchCurrentUser() = viewModelScope.launch {
        _state.update {
            it.copy(
                isProfileLoading = true
            )
        }
        userDetailUseCase(true).onSuccess { data ->
            _state.update {
                it.copy(
                    isProfileLoading = false,
                    fullName = data.fullName,
                    initials = data.initials,
                    levelName = data.levelName,
                    email = data.email,
                    userProfileUrl = data.userProfileUrl,
                )
            }
        }.onError {
            _state.update {
                it.copy(
                    isProfileLoading = false
                )
            }
        }
    }

    //    fetch the upcoming birthday
    private fun fetchUpComingBirthday() = viewModelScope.launch {
        _state.update {
            it.copy(isBirthDayLoading = true)
        }
        upComingBirthdayUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isBirthDayLoading = false,
                    upComingBirthday = data
                )
            }
        }.onError {
            _state.update {
                it.copy(isBirthDayLoading = false)
            }
        }
    }

    //    fetch the upcoming work anniversary
    private fun fetchUpComingWorkAnniversary() = viewModelScope.launch {
        _state.update {
            it.copy(
                isAnniversaryLoading = true
            )
        }
        upComingWorkAnniversaryUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isAnniversaryLoading = false,
                    upComingWorkAnniversary = data
                )
            }
        }.onError {
            _state.update {
                it.copy(
                    isAnniversaryLoading = false
                )
            }
        }
    }

    private fun fetchUpComingEvents()=viewModelScope.launch {
        _state.update {
            it.copy(
                isEventLoading = true
            )
        }

        eventUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isEventLoading = false,
                    upComingEvent = data
                )
            }
        }.onError {
            _state.update {
                it.copy(
                    isEventLoading = false
                )
            }
        }

    }

    private fun getTotalNotificationCount()=viewModelScope.launch {
        _state.update {
            it.copy(
                isNotificationCountLoading = true
            )
        }
        notificationCountUseCase(force = true).onSuccess {data ->
           _state.update {
               it.copy(
                   totalNotificationCount = data.count
               )
           }

        }.onError {
            _state.update {
                it.copy(
                    isNotificationCountLoading = false
                )
            }
        }
    }



}