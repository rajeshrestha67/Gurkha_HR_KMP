package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gurkha.hr.attendance.AttendanceScreen
import com.gurkha.hr.attendanceRequestScreen.AttendanceRequestScreen
import com.gurkha.hr.dashboard.route.AttendanceRoute
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.missedAttendanceScreen.MissedAttendanceScreen

fun NavGraphBuilder.attendanceScreenBuilder(
    navController: NavHostController,
    onGoToAttendanceRequestScreen: () -> Unit,
    onGoToMissedAttendanceScreen: () -> Unit
) {
    composable<DashboardRoute.AttendanceRoute> {
        AttendanceScreen(
            navController = navController,
            onGoToAttendanceRequestScreen = onGoToAttendanceRequestScreen,
            onGoToMissedAttendanceScreen = onGoToMissedAttendanceScreen
        )
    }

    composable<AttendanceRoute.AttendanceRequestScreen> {
        val date: String? = it.toRoute<AttendanceRoute.AttendanceRequestScreen>().date
        val clockStatus: String? = it.toRoute<AttendanceRoute.AttendanceRequestScreen>().clockStatus
        AttendanceRequestScreen(
            navController = navController,
            onBackPressed = {
                navController.popBackStack()
            },
            date = date,
            clockStatus = clockStatus
        )
    }

    composable<AttendanceRoute.MissedAttendanceScreen>{
        MissedAttendanceScreen()
    }
}