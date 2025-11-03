package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.dashboard.route.ProfileRoute
import com.gurkha.hr.profile.allocated_leave.AllocatedLeaveScreen
import com.gurkha.hr.profile.company_assets.CompanyAssetsScreen
import com.gurkha.hr.profile.document.DocumentScreen
import com.gurkha.hr.profile.edit_profile.EditProfileScreen
import com.gurkha.hr.profile.history.HistoryScreen
import com.gurkha.hr.profile.model.profile_screen.AccountList
import com.gurkha.hr.profile.model.profile_screen.GeneralList
import com.gurkha.hr.profile.profile_info.ProfileInfoScreen
import com.gurkha.hr.profile.profile_screen.ProfileScreen
import com.gurkha.hr.profile.report_screen.ReportScreen
import com.gurkha.hr.profile.time_and_attendance.TimeAndAttendanceScreen
import com.gurkha.hr.profile.webview.WebviewScreen


fun NavGraphBuilder.profileScreenBuilder(
    onLogout: () -> Unit,
    navController: NavHostController
) {
    composable<DashboardRoute.ProfileRoute> {
        ProfileScreen(
            onLogout = onLogout,
            onAccountClick = { item ->
                when (item) {
                    AccountList.Settings -> {
                        navController.navigate(ProfileRoute.SettingsRoute)
                    }

                    else -> {
                        navController.navigate(ProfileRoute.WebviewRoute(item.url))
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

                    GeneralList.Report ->
                        navController.navigate(ProfileRoute.ReportScreenRoute)
                }
            }
        )
    }
    composable<ProfileRoute.ProfileInfoScreenRoute> {
        ProfileInfoScreen(
            onBackPressed = {
                navController.popBackStack()
            }, onGotoEditProfile = {
                navController.navigate(ProfileRoute.EditProfileRoute)
            }
        )
    }
    composable<ProfileRoute.EditProfileRoute> {
        EditProfileScreen(
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
    composable<ProfileRoute.ReportScreenRoute> {
        ReportScreen(onBackPressed = {
            navController.popBackStack()
        })
    }

    composable<ProfileRoute.WebviewRoute> {
        val url = it.toRoute<ProfileRoute.WebviewRoute>().url
        WebviewScreen(
            url = url,
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }

}
