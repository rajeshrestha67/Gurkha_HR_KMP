package com.gurkha.hr.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.route.ProfileRoute
import com.gurkha.hr.profile.allocated_leave.AllocatedLeaveScreen
import com.gurkha.hr.profile.company_assets.CompanyAssetsScreen
import com.gurkha.hr.profile.document.DocumentScreen
import com.gurkha.hr.profile.history.HistoryScreen
import com.gurkha.hr.profile.profile_info.ProfileInfoScreen
import com.gurkha.hr.profile.time_and_attendance.TimeAndAttendanceScreen


fun NavGraphBuilder.profileInfoScreenBuilder(navController: NavController) {
    composable<ProfileRoute.ProfileInfoScreenRoute> {
        ProfileInfoScreen(

            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
    composable<ProfileRoute.AllocatedLeaveScreenRoute> {
        AllocatedLeaveScreen(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
    composable<ProfileRoute.TimeAndAttendanceScreenRoute> {
        TimeAndAttendanceScreen(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
    composable<ProfileRoute.DocumentScreenRoute> {
        DocumentScreen(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
    composable<ProfileRoute.CompanyAssetsScreenRoute> {
        CompanyAssetsScreen(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
    composable<ProfileRoute.HistoryScreenRoute> {
        HistoryScreen(
            onBackPressed = {
                navController.popBackStack()
            }
        )

    }


}