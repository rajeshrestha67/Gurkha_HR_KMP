package com.gurkha.hr.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface ProfileRoute{
    @Serializable
    data object  SettingsRoute: AppRoute

    @Serializable
    data object  ChangePasswordRoute: AppRoute

    @Serializable
    data object ProfileInfoScreenRoute: AppRoute

    @Serializable
    data object PersonalInfoScreenRoute: AppRoute
    @Serializable
    data object AllocatedLeaveScreenRoute: AppRoute
    @Serializable
    data object TimeAndAttendanceScreenRoute: AppRoute
    @Serializable
    data object DocumentScreenRoute: AppRoute
    @Serializable
    data object CompanyAssetsScreenRoute: AppRoute
    @Serializable
    data object HistoryScreenRoute: AppRoute

}