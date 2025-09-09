package com.gurkha.hr.dashboard.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.gurkha.hr.dashboard.route.DashboardRoute

data class DashboardScreen(
    val name: String,
    val route: DashboardRoute,
    val icon: ImageVector
)

object DashboardScreens {
    val dashboardScreens = listOf(
        DashboardScreen(
            "Home",
            DashboardRoute.HomeRoute,
            Icons.Filled.Home
        ),
        DashboardScreen(
            "My Attendance",
            DashboardRoute.AttendanceRoute,
            Icons.Filled.Call
        ),

        DashboardScreen(
            "Profile",
            DashboardRoute.ProfileRoute,
            Icons.Filled.Person
        )
    )
}