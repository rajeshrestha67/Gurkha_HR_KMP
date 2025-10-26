package com.gurkha.hr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gurkha.hr.app.App
import com.gurkha.hr.splashscreen.OnBoardingViewModel
import com.gurkha.hr.splashscreen.model.OnBoardingAction
import org.koin.compose.viewmodel.koinViewModel


class MainActivity : ComponentActivity() {


    var showSplashScreen by mutableStateOf(true)
    var navigateToOnBoarding by mutableStateOf(true)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                showSplashScreen
            }
        }

        setContent {

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
    }
}
