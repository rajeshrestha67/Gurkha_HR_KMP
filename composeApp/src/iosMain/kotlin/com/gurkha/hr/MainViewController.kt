package com.gurkha.hr

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import com.gurkha.hr.app.App
import com.gurkha.hr.splashscreen.OnBoardingViewModel
import com.gurkha.hr.splashscreen.model.OnBoardingAction
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

    if (!showSplashScreen) {
        App(
            isFirstTime = navigateToOnBoarding
        )
    }
}
