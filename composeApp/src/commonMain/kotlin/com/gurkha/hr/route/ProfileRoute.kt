package com.gurkha.hr.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface ProfileRoute{
    @Serializable
    data object  SettingsRoute: AppRoute

    @Serializable
    data object  ChangePasswordRoute: AppRoute
}