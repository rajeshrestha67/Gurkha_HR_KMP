package com.gurkha.hr.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.DashboardScreen
import com.gurkha.hr.profile.model.AccountList
import com.gurkha.hr.profile.model.GeneralList
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
                        navController.navigate((ProfileRoute.ProfileInfoScreenRoute))
                    }
                    GeneralList.AllocatedLeave -> {

                    }
                    GeneralList.TimeAndAttendance -> {

                    }
                    GeneralList.Document -> {

                    }
                    GeneralList.CompanyAssets -> {

                    }
                    GeneralList.History -> {

                    }
                }
            },
        )
    }
}