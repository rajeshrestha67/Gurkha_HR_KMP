package com.gurkha.hr.profile.model.profile_screen

sealed interface ProfileViewAction {
    data class SetCurrentItems( val page: String): ProfileViewAction
}