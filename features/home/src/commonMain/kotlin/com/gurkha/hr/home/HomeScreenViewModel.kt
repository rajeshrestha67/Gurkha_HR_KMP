package com.gurkha.hr.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.attendance.usecase.AttendanceUseCase
import com.gurkha.hr.domain.upComingBirthday.usecase.UpComingBirthdayUseCase
import com.gurkha.hr.domain.upComingWorkAnniversaries.useCase.UpComingWorkAnniversaryUseCase
import com.gurkha.hr.domain.userDetail.usecase.UserDetailUseCase
import com.gurkha.hr.home.Model.HomeScreenActions
import com.gurkha.hr.home.Model.HomeScreenState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val attendanceUseCase: AttendanceUseCase,
    private val userDetailUseCase: UserDetailUseCase,
    private val upComingBirthdayUseCase: UpComingBirthdayUseCase,
    private val upComingWorkAnniversaryUseCase: UpComingWorkAnniversaryUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeScreenState()
        )

    fun onAction(action: HomeScreenActions) {
        when (action) {
            is HomeScreenActions.OnRequestLeave -> {
                TODO()
            }

            is HomeScreenActions.OnCheckInClicked -> {
                TODO()
            }

            is HomeScreenActions.OnCheckOutClicked -> {
                TODO()
            }

            is HomeScreenActions.OnRequestAttendance -> {
                TODO()
            }

            is HomeScreenActions.OnSpecificDayClicked -> {
                TODO()
            }

            is HomeScreenActions.AttendanceFetch -> {
                fetch()
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
        }
    }

    //    fetch the attendance report
    private fun fetch(
    ) = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        attendanceUseCase(
//            fromDate = state.value.fromDate,
//            toDate = state.value.toDate,
            fromDate = "2025-09-16",
            toDate = "2025-09-18",
        ).onSuccess { data ->
            _state.update {
                it.copy(
                    isLoading = false,
                    attendanceReport = data
                )
            }
        }.onError {
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    //    fetch the current user details
    private fun fetchCurrentUser() = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        userDetailUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isLoading = false,
                    fullName = data.fullName,
                    levelName = data.levelName,
                    email = data.email,
                    userProfileUrl = data.userProfileUrl,
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

    //    fetch the upcoming birthday
    private fun fetchUpComingBirthday() = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        upComingBirthdayUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isLoading = false,
                    upComingBirthday = data
                )
            }
        }.onError {
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    //    fetch the upcoming work anniversary
    private fun fetchUpComingWorkAnniversary() = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        upComingWorkAnniversaryUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isLoading = false,
                    upComingWorkAnniversary = data
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