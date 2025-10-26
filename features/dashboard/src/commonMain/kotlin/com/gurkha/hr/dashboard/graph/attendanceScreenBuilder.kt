package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.attendance.AttendanceScreen
import com.gurkha.hr.attendanceRequestScreen.AttendanceRequestScreen
import com.gurkha.hr.dashboard.route.AttendanceRoute
import com.gurkha.hr.dashboard.route.DashboardRoute

fun NavGraphBuilder.attendanceScreenBuilder(
    navController: NavHostController,
    onGoToAttendanceRequestScreen: () -> Unit
) {
    composable<DashboardRoute.AttendanceRoute> {
        AttendanceScreen(
            navController = navController,
            onGoToAttendanceRequestScreen = onGoToAttendanceRequestScreen
        )
    }

    composable<AttendanceRoute.AttendanceRequestScreen> {
        AttendanceRequestScreen(
            navController = navController,
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
}