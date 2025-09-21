package com.gurkha.hr.profile.model

sealed interface ProfileViewAction {
    data class SetCurrentItems( val page: String): ProfileViewAction
}