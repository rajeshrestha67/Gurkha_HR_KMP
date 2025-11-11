package com.gurkha.hr.dashboard.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.dashboard.route.HomeRoute
import com.gurkha.hr.home.HomeScreen
import com.gurkha.hr.notification.NotificationScreen
import com.gurkha.hr.profile.edit_profile.EditProfileScreen
import com.gurkha.hr.viewAllScreen.ViewAllScreen
import com.gurkha.model.chat.ChatTypeEnum

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.homeScreenBuilder(
    navController: NavHostController,
    onChatClick: (chatType: ChatTypeEnum) -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onViewAllClick: (String?, String) -> Unit,
    onGoToFixProfile: () -> Unit,
    onToggleFloatingActionButton: (Boolean) -> Unit,
    onGoToAttendanceRequestScreen: (String?, String?) -> Unit,
    onBirthdayUser:(json: String)-> Unit

) {

    composable<DashboardRoute.HomeRoute> {
        HomeScreen(
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            onChatClick = onChatClick,
            onViewAllClick = onViewAllClick,
            onNotificationClick = {
                navController.navigate(route = HomeRoute.NotificationRoute)
            },
            onGoToFixProfile = onGoToFixProfile,
            onToggleFloatingActionButton = onToggleFloatingActionButton,
            onGoToAttendanceRequestScreen = onGoToAttendanceRequestScreen,
            onBirthdayUser = onBirthdayUser
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
        NotificationScreen(
            onBackClicked = {
                navController.popBackStack()
            }
        )
    }

    composable<HomeRoute.EditProfileRoute> {
        EditProfileScreen(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
}