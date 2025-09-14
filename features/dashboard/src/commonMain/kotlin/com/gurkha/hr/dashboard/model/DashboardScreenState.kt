package com.gurkha.hr.dashboard.model

import com.gurkha.hr.dashboard.route.DashboardRoute

data class DashboardScreenState(
    val currentScreen : DashboardRoute = DashboardRoute.HomeRoute,
    val screens: List<DashboardScreen> = DashboardScreens.dashboardScreens
)
