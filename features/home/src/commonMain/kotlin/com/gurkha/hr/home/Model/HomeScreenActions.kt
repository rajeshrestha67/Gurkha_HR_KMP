package com.gurkha.hr.home.Model


sealed interface HomeScreenActions {
    data object OnCheckInClicked : HomeScreenActions
    data object OnCheckOutClicked : HomeScreenActions
    data object OnRequestLeave : HomeScreenActions
    data object OnRequestAttendance : HomeScreenActions
    data class OnSpecificDayClicked(val date: String) : HomeScreenActions

    data object AttendanceFetch : HomeScreenActions

    data object OnFetchCurrentUser : HomeScreenActions
    data object OnFetchUpComingBirthday : HomeScreenActions

    data object OnFetchUpComingWorkAnniversary : HomeScreenActions

}