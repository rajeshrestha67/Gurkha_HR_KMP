package com.gurkha.hr.profile.profile_screen.model

sealed interface ProfileScreenAction {
    data class OnProfileImageReceived(val url: String) : ProfileScreenAction
}