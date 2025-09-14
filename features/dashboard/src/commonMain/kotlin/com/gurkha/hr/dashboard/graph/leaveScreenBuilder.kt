package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.leave.LeaveScreen


fun NavGraphBuilder.leaveScreenBuilder(navController: NavController) {
    composable<DashboardRoute.LeaveRoute> {
        LeaveScreen()
    }
}