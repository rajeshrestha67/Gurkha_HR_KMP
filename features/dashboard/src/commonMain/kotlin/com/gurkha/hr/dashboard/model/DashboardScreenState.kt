package com.gurkha.hr.dashboard.model

import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.domain.userDetail.model.UserDetailData

data class DashboardScreenState(
    val currentScreen : DashboardRoute = DashboardRoute.HomeRoute,
    val screens: List<DashboardScreen> = DashboardScreens.dashboardScreens,
    val userDetail: UserDetailData? = null,
    val isLoading: Boolean = false
)
