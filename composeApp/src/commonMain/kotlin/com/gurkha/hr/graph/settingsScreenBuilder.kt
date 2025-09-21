package com.gurkha.hr.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.route.AppRoute
import com.gurkha.hr.route.ProfileRoute
import com.gurkha.hr.settings.ChangePasswordScreen
import com.gurkha.hr.settings.SettingScreen


fun NavGraphBuilder.settingsScreenBuilder(navController: NavController){

    composable< ProfileRoute.SettingsRoute>{
        SettingScreen(
            onBackPressed = {
                navController.popBackStack()
            },
            onButtonPressed = {
                navController.navigate(ProfileRoute.ChangePasswordRoute)
            }
        )
    }

}

