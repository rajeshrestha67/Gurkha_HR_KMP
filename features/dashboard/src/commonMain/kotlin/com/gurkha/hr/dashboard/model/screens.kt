package com.gurkha.hr.dashboard.model

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class DashboardScreen(
    val name: StringResource,
    val route: DashboardRoute,
    val icon: ImageVector
)

object DashboardScreens {
    val dashboardScreens = listOf(
        DashboardScreen(
            SharedRes.Strings.home,
            DashboardRoute.HomeRoute,
            Icons.Filled.Home
        ),
        DashboardScreen(
            SharedRes.Strings.my_attendance,
            DashboardRoute.AttendanceRoute,
            Icons.Filled.Call
        ),
        DashboardScreen(
            SharedRes.Strings.leave,
            DashboardRoute.LeaveRoute,
            Icons.Filled.Person
        ),
        DashboardScreen(
            SharedRes.Strings.report,
            DashboardRoute.ReportRoute,
            Icons.Filled.Home
        ),
        DashboardScreen(
            SharedRes.Strings.profile,
            DashboardRoute.ProfileRoute,
            Icons.Filled.Person
        )
    )
}