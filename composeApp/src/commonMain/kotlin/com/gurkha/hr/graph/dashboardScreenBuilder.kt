package com.gurkha.hr.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.DashboardScreen
import com.gurkha.hr.route.AppRoute
import com.gurkha.hr.route.LeaveRoute

fun NavGraphBuilder.dashboardScreenBuilder(navController: NavHostController) {
    composable<AppRoute.DashboardRoute>{
        DashboardScreen(
                onGoToLeaveRequestPage = {
                    navController.navigate(LeaveRoute.LeaveRequestPageRoute)
                }
        )
    }
}