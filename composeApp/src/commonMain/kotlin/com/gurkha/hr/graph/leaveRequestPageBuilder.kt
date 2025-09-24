package com.gurkha.hr.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.leave.leaveRequestPage.LeaveRequestPage
import com.gurkha.hr.route.LeaveRoute

fun NavGraphBuilder.leaveRequestPageBuilder(navController: NavController){
    composable<LeaveRoute.LeaveRequestPageRoute>{
        LeaveRequestPage(
            onBackClicked = {
                navController.popBackStack()
            },
            onSubmitClicked = { startDate, endDate, leaveDuration, leaveType, reason ->
                navController.popBackStack()
            }
        )
    }
}