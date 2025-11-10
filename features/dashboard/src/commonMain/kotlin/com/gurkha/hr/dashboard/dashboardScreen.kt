package com.gurkha.hr.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Support
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gurkha.hr.components.AnimatedNavHost
import com.gurkha.hr.components.PlatformMessage
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.navigationBar.ERPNavigationBar
import com.gurkha.hr.components.permissions.POST_NOTIFICATIONS_PERMISSION
import com.gurkha.hr.components.permissions.rememberRequestPermission
import com.gurkha.hr.dashboard.graph.attendanceScreenBuilder
import com.gurkha.hr.dashboard.graph.homeScreenBuilder
import com.gurkha.hr.dashboard.graph.leaveScreenBuilder
import com.gurkha.hr.dashboard.graph.noteScreenBuilder
import com.gurkha.hr.dashboard.graph.profileScreenBuilder
import com.gurkha.hr.dashboard.graph.settingsScreenBuilder
import com.gurkha.hr.dashboard.model.DashboardScreenAction
import com.gurkha.hr.dashboard.model.DashboardScreenState
import com.gurkha.hr.dashboard.route.AttendanceRoute
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.dashboard.route.HomeRoute
import com.gurkha.hr.dashboard.route.LeaveRoute
import com.gurkha.hr.dashboard.route.NoteRoute
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.res.SharedRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val TAG = "DashboardScreen"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    onChatClick: () -> Unit
) {
    val viewModel: DashboardViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


    val showPlatform: PlatformMessage = koinInject()


    LaunchedEffect(Unit) {
        viewModel.sessionExpired.collect { isExpired ->
            if (isExpired) {
                withContext(Dispatchers.Main.immediate) {
                    showPlatform.showToast(getString(SharedRes.Strings.log_out))
                    onLogout()
                    viewModel.action(DashboardScreenAction.Reset)
                }
            }
        }
    }



    DashboardScreenContent(
        state = state,
        onLogout = onLogout,
        onAction = viewModel::action,
        onChatClick = onChatClick
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DashboardScreenContent(
    state: DashboardScreenState,
    onChatClick: () -> Unit,
    onLogout: () -> Unit,
    onAction: (DashboardScreenAction) -> Unit
) {


    var showFloatingButton by remember { mutableStateOf(false) }

    var needExtraPaddingForFloatingButton by remember { mutableStateOf(true) }
    var bottomBarState by remember {
        mutableStateOf(true)
    }
    val dashboardNavController = rememberNavController()
    LaunchedEffect(dashboardNavController) {
        dashboardNavController.addOnDestinationChangedListener { _, destination, _ ->
            bottomBarState = when (destination.route) {
                DashboardRoute.HomeRoute::class.qualifiedName,
                DashboardRoute.ProfileRoute::class.qualifiedName,
                DashboardRoute.AttendanceRoute::class.qualifiedName,
                DashboardRoute.LeaveRoute::class.qualifiedName,
                DashboardRoute.NoteRoute::class.qualifiedName -> true // show bottom bar
                else -> false // hide bottom bar
            }
        }
    }
    val navBackStackEntry by dashboardNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val onPermission = rememberRequestPermission(
        permissions = listOf(
            POST_NOTIFICATIONS_PERMISSION
        ),
        onGranted = { permission ->
            AppLogger.i(
                tag = TAG,
                message = "Permission granted: $permission"
            )
        },
        onDenied = { permission ->
            AppLogger.i(
                tag = TAG,
                message = "Permission denied: $permission"
            )
        },
        onPermanentlyDenied = { permission ->
            AppLogger.i(
                tag = TAG,
                message = "Permission denied permanent: $permission"
            )
        },
        onAllGranted = {
            AppLogger.i(
                tag = TAG,
                message = "All Permission granted"
            )
        }
    )
    LaunchedEffect(Unit) {
        onPermission()
    }
    LaunchedEffect(currentRoute) {
        if (currentRoute != null) {
            val destination = when (currentRoute) {
                DashboardRoute.HomeRoute::class.qualifiedName -> {
                    needExtraPaddingForFloatingButton = true
                    DashboardRoute.HomeRoute
                }

                DashboardRoute.ProfileRoute::class.qualifiedName -> {
                    needExtraPaddingForFloatingButton = false
                    DashboardRoute.ProfileRoute
                }

                DashboardRoute.AttendanceRoute::class.qualifiedName -> {
                    needExtraPaddingForFloatingButton = true
                    DashboardRoute.AttendanceRoute
                }

                DashboardRoute.LeaveRoute::class.qualifiedName -> {
                    needExtraPaddingForFloatingButton = true
                    DashboardRoute.LeaveRoute
                }

                DashboardRoute.NoteRoute::class.qualifiedName -> {
                    needExtraPaddingForFloatingButton = true
                    DashboardRoute.NoteRoute
                }

                else -> {
                    needExtraPaddingForFloatingButton = true
                    DashboardRoute.HomeRoute
                }
            }
            onAction(DashboardScreenAction.OnChangeScreen(destination))
        }
    }
    val topScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AnimatedContent(
                targetState = bottomBarState, transitionSpec = {
                    slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 200)
                    ) togetherWith slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 200)
                    )
                }) { visible ->
                if (visible) {
                    ERPNavigationBar(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        state.screens.forEach { item ->
                            NavigationBarItem(
                                selected = item.route == state.currentScreen,
                                onClick = {
                                    if (item.route == state.currentScreen) return@NavigationBarItem
                                    onAction(
                                        DashboardScreenAction.OnChangeScreen(
                                            item.route
                                        )
                                    )
                                    dashboardNavController.navigate(item.route) {
                                        popUpTo(DashboardRoute.HomeRoute) { inclusive = false }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = stringResource(item.name),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->


        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .animateContentSize(),
        ) {
            AnimatedNavHost(
                modifier = Modifier.fillMaxSize(),
                navController = dashboardNavController,
                startDestination = DashboardRoute.HomeRoute,
            ) {
                homeScreenBuilder(
                    navController = dashboardNavController,
                    topAppBarScrollBehavior = topScrollBehavior,
                    onChatClick = onChatClick,
                    onViewAllClick = { eventsJson, title ->
                        eventsJson?.let {
                            dashboardNavController.navigate(
                                HomeRoute.ViewAllRoute(
                                    json = eventsJson,
                                    title = title
                                )
                            )
                        }
                    },
                    onGoToFixProfile = {
                        dashboardNavController.navigate(HomeRoute.EditProfileRoute)
                    },
                    onToggleFloatingActionButton = { show ->
                        showFloatingButton = show
                    },
                    onGoToAttendanceRequestScreen = { date, clockStatus ->
                        dashboardNavController.navigate(
                            AttendanceRoute.AttendanceRequestScreen(
                                date = date,
                                clockStatus = clockStatus
                            )
                        )
                    }
                )
                profileScreenBuilder(
                    onLogout = onLogout,
                    navController = dashboardNavController
                )
                attendanceScreenBuilder(
                    navController = dashboardNavController,
                    onGoToAttendanceRequestScreen = {
                        dashboardNavController.navigate(
                            AttendanceRoute.AttendanceRequestScreen(
                                date = null,
                                clockStatus = null
                            )
                        )
                    }
                )
                leaveScreenBuilder(
                    navController = dashboardNavController,
                    onGoToLeaveRequestPage = {
                        dashboardNavController.navigate(LeaveRoute.LeaveRequestPageRoute)
                    }
                )

                noteScreenBuilder(
                    navController = dashboardNavController,
                    onGoToAddNotesScreen = { noteJson ->
                        dashboardNavController.navigate(NoteRoute.AddNoteRoute(json = noteJson))
                    },
                    onGoToDetailNotesScreen = { noteJson ->
                        noteJson?.let {
                            dashboardNavController.navigate(NoteRoute.DetailNoteRoute(json = noteJson))
                        }
                    },
                )

                settingsScreenBuilder(
                    navController = dashboardNavController
                )
            }

            AnimatedVisibility(
                visible = showFloatingButton,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .padding(MaterialTheme.dimens.small3)
                    .animateContentSize()
                    .padding(
                        bottom = if (needExtraPaddingForFloatingButton) MaterialTheme.dimens.swipeToDismissHeight else MaterialTheme.dimens.small3
                    )
                    .align(Alignment.BottomEnd)
            ) {
                FloatingActionButton(
                    onClick = {
                        //onChatClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Support,
                        contentDescription = stringResource(SharedRes.Strings.support)
                    )
                }
            }


        }
    }
}


