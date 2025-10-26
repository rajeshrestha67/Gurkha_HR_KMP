package com.gurkha.hr.dashboard.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gurkha.hr.dashboard.route.ChatRoute
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.dashboard.route.HomeRoute
import com.gurkha.hr.home.HomeScreen
import com.gurkha.hr.notification.Notification
import com.gurkha.hr.viewAllScreen.ViewAllScreen

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.homeScreenBuilder(
    navController: NavHostController,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onViewAllClick: (String?, String) -> Unit
) {

    composable<DashboardRoute.HomeRoute> {
        HomeScreen(
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            onChatClick = {
                navController.navigate(ChatRoute.ChatList)
            },
            onViewAllClick = onViewAllClick,
            onNotificationClick = {
                navController.navigate(route = HomeRoute.NotificationRoute)
            }
        )
    }

    composable<HomeRoute.ViewAllRoute> {
        val json: String? = it.toRoute<HomeRoute.ViewAllRoute>().json
        val title: String? = it.toRoute<HomeRoute.ViewAllRoute>().title
        ViewAllScreen(
            json = json,
            title = title,
            onBackClicked = {
                navController.popBackStack()
            }
        )
    }

    composable<HomeRoute.NotificationRoute> {
        Notification(
            onBackClicked = {
                navController.popBackStack()
            }
        )
    }


}