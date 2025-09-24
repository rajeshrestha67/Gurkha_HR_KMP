package com.gurkha.hr.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.profile.allocated_leave.AllocatedLeaveScreen
import com.gurkha.hr.profile.company_assets.CompanyAssetsScreen
import com.gurkha.hr.profile.document.DocumentScreen
import com.gurkha.hr.profile.history.HistoryScreen
import com.gurkha.hr.profile.profile_info.ProfileInfoScreen
import com.gurkha.hr.profile.time_and_attendance.TimeAndAttendanceScreen
import com.gurkha.hr.route.ProfileRoute

fun NavGraphBuilder.profileInfoScreenBuilder(navController: NavController){
    composable<ProfileRoute.ProfileInfoScreenRoute>{
        ProfileInfoScreen(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
    composable<ProfileRoute.AllocatedLeaveScreenRoute>{
        AllocatedLeaveScreen()
    }
    composable<ProfileRoute.TimeAndAttendanceScreenRoute>{
        TimeAndAttendanceScreen()
    }
    composable<ProfileRoute.DocumentScreenRoute>{
        DocumentScreen()
    }
    composable<ProfileRoute.CompanyAssetsScreenRoute>{
        CompanyAssetsScreen()
    }
    composable<ProfileRoute.HistoryScreenRoute>{
        HistoryScreen()

    }






}