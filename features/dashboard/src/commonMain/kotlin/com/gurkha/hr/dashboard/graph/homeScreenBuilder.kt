package com.gurkha.hr.dashboard.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.home.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.homeScreenBuilder(
    navController: NavHostController,
    onChatClick: () -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {

    composable<DashboardRoute.HomeRoute> {
        HomeScreen(
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            onChatClick = onChatClick,
            onNotificationClick = {

            }
        )
    }
}