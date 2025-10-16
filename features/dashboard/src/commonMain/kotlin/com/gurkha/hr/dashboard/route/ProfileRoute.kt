package com.gurkha.hr.dashboard.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface ProfileRoute {
    @Serializable
    data object SettingsRoute : ProfileRoute

    @Serializable
    data object ChangePasswordRoute : ProfileRoute

    @Serializable
    data object ProfileInfoScreenRoute : ProfileRoute

    @Serializable
    data object AllocatedLeaveScreenRoute : ProfileRoute

    @Serializable
    data object TimeAndAttendanceScreenRoute : ProfileRoute

    @Serializable
    data object DocumentScreenRoute : ProfileRoute

    @Serializable
    data object CompanyAssetsScreenRoute : ProfileRoute

    @Serializable
    data object HistoryScreenRoute : ProfileRoute

    @Serializable
    data object ReportScreenRoute: ProfileRoute

}