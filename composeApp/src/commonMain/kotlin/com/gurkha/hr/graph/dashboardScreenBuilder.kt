package com.gurkha.hr.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.DashboardScreen
import com.gurkha.hr.profile.model.profile_screen.AccountList
import com.gurkha.hr.profile.model.profile_screen.GeneralList
import com.gurkha.hr.route.AppRoute
import com.gurkha.hr.route.ProfileRoute

fun NavGraphBuilder.dashboardScreenBuilder(navController: NavHostController) {
    composable<AppRoute.DashboardRoute>{
        DashboardScreen(
            onLogout = {
                navController.navigate(AppRoute.LoginRoute) {
                    popUpTo(AppRoute.DashboardRoute) { inclusive = true }
                }
            },
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
                when(item){
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
            },
        )
    }
}