package com.gurkha.hr.dashboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gurkha.hr.dashboard.graph.attendanceScreen
import com.gurkha.hr.dashboard.graph.homeScreenBuilder
import com.gurkha.hr.dashboard.graph.leaveScreenBuilder
import com.gurkha.hr.dashboard.graph.profileScreenBuilder
import com.gurkha.hr.dashboard.graph.reportScreenBuilder
import com.gurkha.hr.dashboard.model.DashboardScreenAction
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val navController = rememberNavController()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar {
                state.screens.forEach { item ->
                    NavigationBarItem(
                        selected = item.route == state.currentScreen,
                        onClick = {
                            viewModel.action(action = DashboardScreenAction.OnChangeScreen(item.route))
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = stringResource(item.name),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
//                        label = {
//                            Text(
//                                text = stringResource(item.name),
//                                style = MaterialTheme.typography.bodyMedium.copy(
//                                    fontSize = 12.sp
//                                )
//                            )
//                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            navController = navController,
            startDestination = state.currentScreen
        ) {
            homeScreenBuilder(navController = navController)
            profileScreenBuilder(navController = navController)
            attendanceScreen(navController = navController)
            leaveScreenBuilder(navController = navController)
            reportScreenBuilder(navController = navController)
        }
    }
}

