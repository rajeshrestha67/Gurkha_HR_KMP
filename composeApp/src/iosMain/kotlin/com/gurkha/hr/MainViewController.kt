package com.gurkha.hr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ComposeUIViewController
import com.gurkha.hr.app.App
import com.gurkha.hr.splashscreen.OnBoardingViewModel
import com.gurkha.hr.splashscreen.model.OnBoardingAction
import gurkhahr.composeapp.generated.resources.Res
import gurkhahr.composeapp.generated.resources.splash_theme
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

fun MainViewController() = ComposeUIViewController {
    var showSplashScreen by remember { mutableStateOf(true) }
    var navigateToOnBoarding by remember { mutableStateOf(true) }
    val viewModel: OnBoardingViewModel = koinViewModel()
    LaunchedEffect(Unit) {
        viewModel.action(OnBoardingAction.CheckFirstUser)
    }

    LaunchedEffect(Unit) {
        viewModel.navigationChannel.collect { isFirstTime ->
            showSplashScreen = false
            navigateToOnBoarding = isFirstTime
        }
    }

    if (showSplashScreen) {
        SplashScreenView()
    } else {
        App(
            isFirstTime = navigateToOnBoarding
        )
    }
}

@Composable
fun SplashScreenView() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0XFFF8F9CC)),
        contentAlignment = Alignment.Center
    ) {

        Icon(

            painter = painterResource(resource = Res.drawable.splash_theme),
            contentDescription = "AppIcon"
        )

    }
}
