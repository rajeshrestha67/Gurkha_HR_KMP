package com.gurkha.hr

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gurkha.hr.components.AnimatedNavHost
import com.gurkha.hr.graph.dashboardScreenBuilder
import com.gurkha.hr.graph.loginScreenBuilder
import com.gurkha.hr.graph.onBoardingBuilder
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.logger.LocalLogStorage
import com.gurkha.hr.logger.LogEntry
import com.gurkha.hr.res.theme.AppTheme
import com.gurkha.hr.route.AppRoute
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
@Preview
fun App(
    isFirstTime: Boolean
) {
    AppTheme {
        AppScreen(isFirstTime)
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun AppScreen(isFirstTime: Boolean) {

    val navController = rememberNavController()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val localLogStorage = LocalLogStorage()
        AppLogger.enableLocalLogging = true
        AppLogger.init(localLogStorage, ktorRemoteLogger = null, firebaseLogger = null)
        scope.launch {
            localLogStorage.appendLog(
                LogEntry(
                    timestamp = Clock.System.now().toEpochMilliseconds(),
                    level = "INFO",
                    tag = "Startup",
                    message = "App launched successfully"
                )
            )
        }
    }

    AnimatedNavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = if (isFirstTime) AppRoute.OnBoardingRoute else AppRoute.LoginRoute
    ) {
        onBoardingBuilder(navController = navController)
        loginScreenBuilder(navController = navController)
        dashboardScreenBuilder(navController = navController)
    }
}
