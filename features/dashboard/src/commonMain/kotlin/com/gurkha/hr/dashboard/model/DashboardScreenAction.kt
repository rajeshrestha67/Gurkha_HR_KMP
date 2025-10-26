package com.gurkha.hr.dashboard.model

import com.gurkha.hr.dashboard.route.DashboardRoute

sealed interface DashboardScreenAction {
    data class OnChangeScreen(val route: DashboardRoute) : DashboardScreenAction

    data object OnFetchCurrentUser : DashboardScreenAction
    data object Reset : DashboardScreenAction
}