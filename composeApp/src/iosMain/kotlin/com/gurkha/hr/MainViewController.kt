package com.gurkha.hr

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import com.gurkha.hr.crypto.CryptoFactory
import com.gurkha.hr.splashscreen.OnBoardingViewModel
import com.gurkha.hr.splashscreen.model.OnBoardingAction
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

fun MainViewController() = ComposeUIViewController {
    var showSplashScreen by remember { mutableStateOf(true) }
    var navigateToOnBoarding by remember { mutableStateOf(true) }
    val viewModel: OnBoardingViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.action(OnBoardingAction.CheckFirstUser)
    }

    LaunchedEffect(Unit) {
        viewModel.navigationChannel.collect { isFirstTime ->
            showSplashScreen = false
            navigateToOnBoarding = isFirstTime
        }
    }

    val value = "tesgasdfasdf"
    scope.launch {
        val encrypted = CryptoFactory.encrypt(value)
        println("CryptoFactory encrypted $encrypted")
        encrypted?.let {
            val decrypted = CryptoFactory.decrypt<String>(encrypted)
            println("CryptoFactory decrypted $decrypted")
        }
    }
    println()
    if (!showSplashScreen) {
        App(
            isFirstTime = navigateToOnBoarding
        )
    }
}
