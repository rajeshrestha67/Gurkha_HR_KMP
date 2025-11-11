package com.gurkha.hr.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.DashboardScreen
import com.gurkha.hr.route.AppRoute
import com.gurkha.model.chat.ChatTypeEnum

fun NavGraphBuilder.dashboardScreenBuilder(
    navController: NavHostController,
    onChatClick: (chatType: ChatTypeEnum) -> Unit,
    onBirthdayUser:(json: String)-> Unit
) {
    composable<AppRoute.DashboardRoute> {
        DashboardScreen(
            onLogout = {
                navController.navigate(AppRoute.LoginRoute) {
                    popUpTo(AppRoute.DashboardRoute) { inclusive = true }
                }
            },
            onChatClick = onChatClick,
            onBirthdayUser = onBirthdayUser
        )
    }
}