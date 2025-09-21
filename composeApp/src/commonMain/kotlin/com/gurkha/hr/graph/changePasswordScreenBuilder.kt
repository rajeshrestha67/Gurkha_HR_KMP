package com.gurkha.hr.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.route.ProfileRoute
import com.gurkha.hr.settings.ChangePasswordScreen

fun NavGraphBuilder.changePasswordScreenBuilder(navController: NavController){
    composable<ProfileRoute.ChangePasswordRoute> {
        ChangePasswordScreen(onBackPressed = {
            navController.popBackStack()
        })
    }
}