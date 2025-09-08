package com.gurkha.hr.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.route.AppRoute
import com.gurkha.hr.splashscreen.OnBoardingScreen

fun NavGraphBuilder.onBoardingBuilder(navController: NavHostController) {
    composable<AppRoute.OnBoardingRoute> {
        OnBoardingScreen(onNavigateToLogin = {
            navController.navigate(AppRoute.LoginRoute) {
                popUpTo(AppRoute.OnBoardingRoute) { inclusive = true }
            }
        })
    }
}
