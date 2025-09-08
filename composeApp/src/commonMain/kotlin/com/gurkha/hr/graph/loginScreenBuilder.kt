package com.gurkha.hr.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.login.LoginScreen
import com.gurkha.hr.route.AppRoute

fun NavGraphBuilder.loginScreenBuilder(navController: NavHostController) {

    composable<AppRoute.LoginRoute> {
        LoginScreen(onNavigateToDashboard = {
            navController.navigate(AppRoute.DashboardRoute) {
                popUpTo(AppRoute.LoginRoute) { inclusive = true }
            }
        })
    }
}