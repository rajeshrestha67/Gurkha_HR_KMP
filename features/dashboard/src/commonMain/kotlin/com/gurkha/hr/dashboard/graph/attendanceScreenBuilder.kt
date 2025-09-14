package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.attendance.AttendanceScreen
import com.gurkha.hr.dashboard.route.DashboardRoute

fun NavGraphBuilder.attendanceScreen(navController: NavController) {
    composable<DashboardRoute.AttendanceRoute> {
        AttendanceScreen()
    }
}