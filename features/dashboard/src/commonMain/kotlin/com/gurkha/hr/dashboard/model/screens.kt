package com.gurkha.hr.dashboard.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Notes
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
            Icons.Filled.EventAvailable
        ),
        DashboardScreen(
            SharedRes.Strings.leave,
            DashboardRoute.LeaveRoute,
            Icons.Filled.EventBusy
        ),
        DashboardScreen(
            SharedRes.Strings.notes,
            DashboardRoute.NoteRoute,
            Icons.Filled.NoteAlt
        ),
        DashboardScreen(
            SharedRes.Strings.profile,
            DashboardRoute.ProfileRoute,
            Icons.Filled.Person
        )
    )
}