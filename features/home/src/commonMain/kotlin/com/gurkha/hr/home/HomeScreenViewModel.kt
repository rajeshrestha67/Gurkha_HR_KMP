package com.gurkha.hr.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.date.data.model.now
import com.gurkha.hr.domain.attendance.attendanceReport.usecase.AttendanceUseCase
import com.gurkha.hr.domain.upComingBirthday.usecase.UpComingBirthdayUseCase
import com.gurkha.hr.domain.upComingWorkAnniversaries.useCase.UpComingWorkAnniversaryUseCase
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.home.model.HomeScreenActions
import com.gurkha.hr.home.model.HomeScreenState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number

class HomeScreenViewModel(
    private val attendanceUseCase: AttendanceUseCase,
    private val userDetailUseCase: FetchUserDetailUseCase,
    private val upComingBirthdayUseCase: UpComingBirthdayUseCase,
    private val upComingWorkAnniversaryUseCase: UpComingWorkAnniversaryUseCase,
    private val calendarModel: CalendarModel
) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState(todayBS = calendarModel.today))
    val state = _state
        .onStart {
            fetchCurrentUser()
            fetchUpComingBirthday()
            fetchUpComingWorkAnniversary()
            fetchAttendance()
            fetchCalendarValue()
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

            is HomeScreenActions.AttendanceFetch -> {
                fetchAttendance()
            }

            is HomeScreenActions.OnFetchCurrentUser -> {
                fetchCurrentUser()
            }

            is HomeScreenActions.OnFetchUpComingBirthday -> {
                fetchUpComingBirthday()
            }

            is HomeScreenActions.OnFetchUpComingWorkAnniversary -> {
                fetchUpComingWorkAnniversary()
            }

            is HomeScreenActions.OnCheckOutClicked -> TODO()
            is HomeScreenActions.OnNotificationClicked -> TODO()
            is HomeScreenActions.OnSearchedClicked -> TODO()
        }
    }


    private fun fetchCalendarValue() {
        _state.update {
            it.copy(
                calendarData = calendarModel.numberOfDaysInMonth(),
                todayBS = calendarModel.today
            )
        }
    }

    //    fetch the attendance report
    private fun fetchAttendance() = viewModelScope.launch {
        _state.update {
            it.copy(isAttendanceLoading = true)
        }

        val todayAD = LocalDate.now()
        val eightDaysAgo = todayAD.minus(DatePeriod(days = 8))

        attendanceUseCase(
//            fromDate = state.value.fromDate,
//            toDate = state.value.toDate,
            fromDate = eightDaysAgo.formatDate(),
            toDate = todayAD.formatDate()
        ).onSuccess { data ->
            _state.update {
                it.copy(
                    isAttendanceLoading = false,
                    attendanceReport = data
                )
            }
        }.onError {
            _state.update {
                it.copy(isAttendanceLoading = false)
            }
        }
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

}