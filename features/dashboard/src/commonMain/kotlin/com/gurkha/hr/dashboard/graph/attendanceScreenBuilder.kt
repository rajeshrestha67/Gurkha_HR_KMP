package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.attendance.AttendanceScreen
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.dashboard.route.LeaveRoute
import com.gurkha.hr.leave.LeaveRequestPage

fun NavGraphBuilder.attendanceScreen(navController: NavController) {
    composable<DashboardRoute.AttendanceRoute> {
        AttendanceScreen()
    }
    composable<LeaveRoute.LeaveRequestPageRoute> {
        LeaveRequestPage()
    }
}