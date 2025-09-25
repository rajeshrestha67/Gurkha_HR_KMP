package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.dashboard.route.LeaveRoute
import com.gurkha.hr.leave.LeaveScreen
import com.gurkha.hr.leave.leaveRequestPage.LeaveRequestPage


fun NavGraphBuilder.leaveScreenBuilder(
    navController: NavController,
    onGoToLeaveRequestPage: () -> Unit
) {
    composable<DashboardRoute.LeaveRoute> {
        LeaveScreen(
            onGoToLeaveRequestPage = onGoToLeaveRequestPage
        )
    }
    composable<LeaveRoute.LeaveRequestPageRoute> {
        LeaveRequestPage(
            onBackClicked = {
                navController.popBackStack()
            },
            onSubmitClicked = { startDate: String, endDate: String, leaveDuration: String, leaveType: String, reason: String ->
                navController.popBackStack()
            }
        )
    }
}