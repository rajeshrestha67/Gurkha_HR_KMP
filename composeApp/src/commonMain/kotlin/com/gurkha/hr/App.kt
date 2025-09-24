package com.gurkha.hr

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gurkha.hr.dashboard.DashboardScreen
import com.gurkha.hr.dashboard.graph.profileScreenBuilder
import com.gurkha.hr.graph.changePasswordScreenBuilder
import com.gurkha.hr.graph.dashboardScreenBuilder
import com.gurkha.hr.graph.loginScreenBuilder
import com.gurkha.hr.graph.onBoardingBuilder
import com.gurkha.hr.graph.profileInfoScreenBuilder
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
    AnimatedNavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = if (isFirstTime) AppRoute.OnBoardingRoute else AppRoute.LoginRoute
    ) {
        onBoardingBuilder(navController = navController)
        loginScreenBuilder(navController = navController)
        dashboardScreenBuilder(navController = navController)
        settingsScreenBuilder(navController = navController)
        changePasswordScreenBuilder(navController = navController)
        profileInfoScreenBuilder(navController = navController)

    }
}
