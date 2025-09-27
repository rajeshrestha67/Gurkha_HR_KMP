package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.dashboard.route.LeaveRoute
import com.gurkha.hr.leave.leave.LeaveScreen
import com.gurkha.hr.leave.leaveRequestPage.LeaveRequestScreen


fun NavGraphBuilder.leaveScreenBuilder(
    navController: NavHostController,
    onGoToLeaveRequestPage: (String?) -> Unit
) {

    composable<DashboardRoute.LeaveRoute> {
        LeaveScreen(
            navController = navController,
            onGoToLeaveRequestPage = onGoToLeaveRequestPage
        )
    }
    composable<LeaveRoute.LeaveRequestPageRoute> {
        val json: String? = it.toRoute<LeaveRoute.LeaveRequestPageRoute>().json
        LeaveRequestScreen(
            navController = navController,
            json = json,
            onBackClicked = {
                navController.popBackStack()
            }
        )
    }
}