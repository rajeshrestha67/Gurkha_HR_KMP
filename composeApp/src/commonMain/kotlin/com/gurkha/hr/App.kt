package com.gurkha.hr

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gurkha.hr.graph.dashboardScreenBuilder
import com.gurkha.hr.graph.loginScreenBuilder
import com.gurkha.hr.graph.onBoardingBuilder
import com.gurkha.hr.res.theme.AppTheme
import com.gurkha.hr.route.AppRoute
import com.gurkha.hr.splashscreen.SplashscreenViewModel
import com.gurkha.hr.splashscreen.model.SplashScreenAction
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    var isFirstTimeState by remember { mutableStateOf(false) }
    val viewModel: SplashscreenViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        viewModel.action(SplashScreenAction.CheckFirstUser)
    }

    LaunchedEffect(Unit) {
        viewModel.navigationChannel.collect { isFirstTime ->
            isFirstTimeState = isFirstTime
        }
    }

    AppTheme {
        AppScreen(isFirstTimeState)
    }
}

@Composable
fun AppScreen(isFirstTime: Boolean) {

    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = if (isFirstTime) AppRoute.OnBoardingRoute else AppRoute.LoginRoute,

        popExitTransition = {
            scaleOut(
                targetScale = 0.9f,
                transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.5f)
            )
        },
        popEnterTransition = {
            EnterTransition.None
        }
    ) {
        loginScreenBuilder(navController = navController)
        dashboardScreenBuilder(navController = navController)
        onBoardingBuilder(navController = navController)
    }
}
