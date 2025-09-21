package com.gurkha.hr.home.model


sealed interface HomeScreenActions {
    data object OnCheckInClicked : HomeScreenActions
    data object OnCheckOutClicked : HomeScreenActions
    data object OnNotificationClicked : HomeScreenActions
    data object OnSearchedClicked : HomeScreenActions
    data object OnRequestLeave : HomeScreenActions
    data object OnRequestAttendance : HomeScreenActions
    data object OnChatClicked : HomeScreenActions
    data class OnSpecificDayClicked(val date: String) : HomeScreenActions
}