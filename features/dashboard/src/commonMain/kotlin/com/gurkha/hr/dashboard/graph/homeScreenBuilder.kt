package com.gurkha.hr.dashboard.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.route.ChatRoute
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.home.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.homeScreenBuilder(
    navController: NavHostController,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {

    composable<DashboardRoute.HomeRoute> {
        HomeScreen(
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            onChatClick = {
                navController.navigate(ChatRoute.ChatList)
            },
            onNotificationClick = {

            }
        )
    }
}