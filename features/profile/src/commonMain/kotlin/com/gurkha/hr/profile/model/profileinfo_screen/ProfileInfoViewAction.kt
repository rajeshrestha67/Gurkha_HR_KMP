package com.gurkha.hr.profile.model.profileinfo_screen

interface ProfileInfoViewAction {
    data object OnFetchData : ProfileInfoViewAction
    data class OnItemSelected(val index: Int) : ProfileInfoViewAction
}