package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gurkha.hr.attendance.AttendanceScreen
import com.gurkha.hr.attendanceRequestScreen.AttendanceRequestScreen
import com.gurkha.hr.dashboard.route.AttendanceRoute
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.dashboard.route.LeaveRoute

fun NavGraphBuilder.attendanceScreenBuilder(
    navController: NavHostController,
    onGoToAttendanceRequestScreen: (String?) -> Unit
) {
    composable<DashboardRoute.AttendanceRoute> {
        AttendanceScreen(
            navController = navController,
            onGoToAttendanceRequestScreen = onGoToAttendanceRequestScreen
        )
    }

    composable<AttendanceRoute.AttendanceRequestScreen>{
        AttendanceRequestScreen(
            navController = navController,
            onBackClicked={
                navController.popBackStack()
            }
        )
    }
}