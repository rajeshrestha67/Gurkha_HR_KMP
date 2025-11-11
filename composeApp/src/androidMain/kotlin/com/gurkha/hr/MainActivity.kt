package com.gurkha.hr

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import com.gurkha.hr.app.App
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.splashscreen.OnBoardingViewModel
import com.gurkha.hr.splashscreen.model.OnBoardingAction
import org.koin.compose.viewmodel.koinViewModel


class MainActivity : FragmentActivity() {


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
            val data = intent.getIntentValue()
            println("first ${data.first}, second ${data.second}")
            if (!showSplashScreen) {
                App(
                    isFirstTime = navigateToOnBoarding,
                    navigateToLeave = data.first,
                    isApproved = data.second
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun Intent.getIntentValue(): Pair<Boolean?, Boolean?> {
        val type = getStringExtra(FirebaseService.TYPE)
        val action = getStringExtra(FirebaseService.ACTION)
        AppLogger.i(TAG, "push notification received with type:$type, action:$action")
        //APPROVED
        return Pair(
            first = type?.equals("leave", true),
            second = action?.toLowerCase(Locale.current)?.contains("approved", true)
        )
    }

    companion object {
        private const val TAG = "MainActivity"

    }
}
