package com.gurkha.hr.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.DashboardScreen
import com.gurkha.hr.route.AppRoute

fun NavGraphBuilder.dashboardScreenBuilder(
    navController: NavHostController,
    onChatClick: () -> Unit
) {
    composable<AppRoute.DashboardRoute> {
        DashboardScreen(
            onLogout = {
                navController.navigate(AppRoute.LoginRoute) {
                    popUpTo(AppRoute.DashboardRoute) { inclusive = true }
                }
            },
            onChatClick = onChatClick
        )
    }
}