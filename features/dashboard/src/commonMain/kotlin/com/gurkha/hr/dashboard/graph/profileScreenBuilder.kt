package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.dashboard.route.ProfileRoute
import com.gurkha.hr.profile.allocated_leave.AllocatedLeaveScreen
import com.gurkha.hr.profile.company_assets.CompanyAssetsScreen
import com.gurkha.hr.profile.document.DocumentScreen
import com.gurkha.hr.profile.history.HistoryScreen
import com.gurkha.hr.profile.model.profile_screen.AccountList
import com.gurkha.hr.profile.model.profile_screen.GeneralList
import com.gurkha.hr.profile.profile_info.ProfileInfoScreen
import com.gurkha.hr.profile.profile_screen.ProfileScreen
import com.gurkha.hr.profile.time_and_attendance.TimeAndAttendanceScreen


fun NavGraphBuilder.profileScreenBuilder(
    onLogout: () -> Unit,
    navController: NavHostController
) {
    composable<DashboardRoute.ProfileRoute> {
        ProfileScreen(
            onLogout = onLogout,
            onAccountClick = { item ->
                when (item) {
                    AccountList.TermsAndServices -> {

                    }

                    AccountList.PrivacyPolicy -> {

                    }

                    AccountList.FAC -> {

                    }

                    AccountList.Support -> {

                    }

                    AccountList.Settings -> {
                        navController.navigate(ProfileRoute.SettingsRoute)
                    }
                }
            },
            onGeneralClick = { item ->
                when (item) {
                    GeneralList.Profile -> {
                        navController.navigate(ProfileRoute.ProfileInfoScreenRoute)
                    }

                    GeneralList.AllocatedLeave -> {
                        navController.navigate(ProfileRoute.AllocatedLeaveScreenRoute)
                    }

                    GeneralList.TimeAndAttendance -> {
                        navController.navigate(ProfileRoute.TimeAndAttendanceScreenRoute)
                    }

                    GeneralList.Document -> {
                        navController.navigate(ProfileRoute.DocumentScreenRoute)
                    }

                    GeneralList.CompanyAssets -> {
                        navController.navigate(ProfileRoute.CompanyAssetsScreenRoute)
                    }

                    GeneralList.History -> {
                        navController.navigate(ProfileRoute.HistoryScreenRoute)
                    }
                }
            }
        )
    }
    composable<ProfileRoute.ProfileInfoScreenRoute> {
        ProfileInfoScreen(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
    composable<ProfileRoute.AllocatedLeaveScreenRoute> {
        AllocatedLeaveScreen(onBackPressed = {
            navController.popBackStack()
        })
    }
    composable<ProfileRoute.TimeAndAttendanceScreenRoute> {
        TimeAndAttendanceScreen(onBackPressed = {
            navController.popBackStack()
        })
    }
    composable<ProfileRoute.DocumentScreenRoute> {
        DocumentScreen(onBackPressed = {
            navController.popBackStack()
        })
    }
    composable<ProfileRoute.CompanyAssetsScreenRoute> {
        CompanyAssetsScreen(onBackPressed = {
            navController.popBackStack()
        })
    }
    composable<ProfileRoute.HistoryScreenRoute> {
        HistoryScreen(onBackPressed = {
            navController.popBackStack()
        })
    }

}
