package com.gurkha.hr

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gurkha.hr.dashboard.DashboardScreen
import com.gurkha.hr.dashboard.graph.profileScreenBuilder
import com.gurkha.hr.graph.changePasswordScreenBuilder
import com.gurkha.hr.graph.dashboardScreenBuilder
import com.gurkha.hr.graph.loginScreenBuilder
import com.gurkha.hr.graph.onBoardingBuilder
import com.gurkha.hr.graph.settingsScreenBuilder
import com.gurkha.hr.res.theme.AppTheme
import com.gurkha.hr.route.AppRoute
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    isFirstTime: Boolean
) {
    AppTheme {
        AppScreen(isFirstTime)
    }
}

@Composable
fun AppScreen(isFirstTime: Boolean) {

    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = if (isFirstTime) AppRoute.OnBoardingRoute else AppRoute.LoginRoute,

        popExitTransition = {
            scaleOut(
                targetScale = 0.9f,
                transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.5f)
            )
        },
        popEnterTransition = {
            EnterTransition.None
        }
    ) {
        onBoardingBuilder(navController = navController)
        loginScreenBuilder(navController = navController)
        dashboardScreenBuilder(navController = navController)
        settingsScreenBuilder(navController = navController)
        changePasswordScreenBuilder(navController = navController)
    }
}
