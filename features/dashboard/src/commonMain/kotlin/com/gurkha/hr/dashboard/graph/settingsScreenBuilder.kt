package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.route.ProfileRoute
import com.gurkha.hr.settings.ChangePasswordScreen
import com.gurkha.hr.settings.SettingScreen

fun NavGraphBuilder.settingsScreenBuilder(
    navController: NavHostController
) {
    composable<ProfileRoute.SettingsRoute> {
        SettingScreen(
            onBackPressed = {
                navController.popBackStack()
            },
            navigateToChangePassword = {
                navController.navigate(ProfileRoute.ChangePasswordRoute)
            },
            navigateToNotificationSettings = {

            }
        )
    }
    composable<ProfileRoute.ChangePasswordRoute> {
        ChangePasswordScreen(onBackPressed = {
            navController.popBackStack()
        })
    }
}