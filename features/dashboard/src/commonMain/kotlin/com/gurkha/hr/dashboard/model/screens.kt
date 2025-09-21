package com.gurkha.hr.dashboard.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class DashboardScreen(
    val name: StringResource,
    val route: DashboardRoute,
    val icon: ImageVector,
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
            Icons.Filled.CalendarViewMonth
        ),
        DashboardScreen(
            SharedRes.Strings.leave,
            DashboardRoute.LeaveRoute,
            Icons.Filled.EditCalendar
        ),
        DashboardScreen(
            SharedRes.Strings.report,
            DashboardRoute.ReportRoute,
            Icons.Filled.Checklist
        ),
        DashboardScreen(
            SharedRes.Strings.profile,
            DashboardRoute.ProfileRoute,
            Icons.Filled.Person
        )
    )
}