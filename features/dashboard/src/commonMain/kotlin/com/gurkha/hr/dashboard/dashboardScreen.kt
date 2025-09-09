package com.gurkha.hr.dashboard

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gurkha.hr.dashboard.graph.attendanceScreen
import com.gurkha.hr.dashboard.graph.homeScreenBuilder
import com.gurkha.hr.dashboard.graph.profileScreenBuilder
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                state.screens.forEach { item ->
                    NavigationBarItem(
                        selected = item.route == state.currentScreen,
                        onClick = {
                            viewModel.navigateTo(item.route)
                        },
                        icon = {
                            Icon(item.icon, contentDescription = item.name)
                        },
                        label = {
                            Text(item.name)
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = state.currentScreen
        ) {
            homeScreenBuilder(navController = navController)
            profileScreenBuilder(navController = navController)
            attendanceScreen(navController = navController)
        }
    }
}

