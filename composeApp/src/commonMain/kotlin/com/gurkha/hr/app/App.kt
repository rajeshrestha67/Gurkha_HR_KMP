package com.gurkha.hr.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.gurkha.hr.components.AnimatedNavHost
import com.gurkha.hr.components.AppTheme
import com.gurkha.hr.components.locale.erpAppLocale
import com.gurkha.hr.components.statusBar.StatusBarView
import com.gurkha.hr.dashboard.graph.chatScreenBuilder
import com.gurkha.hr.dashboard.route.ChatRoute
import com.gurkha.model.chat.ChatTypeEnum
import com.gurkha.hr.graph.dashboardScreenBuilder
import com.gurkha.hr.graph.loginScreenBuilder
import com.gurkha.hr.graph.onBoardingBuilder
import com.gurkha.hr.route.AppRoute
import com.gurkha.hr.splashscreen.AppViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@Composable
@Preview
fun App(
    isFirstTime: Boolean
) {

    val appViewModel: AppViewModel = koinViewModel<AppViewModel>()
    val state by appViewModel.state.collectAsStateWithLifecycle()



    AppTheme(
        selectedThemeMode = state.userThemeMode
    ) {
        StatusBarView()
        AppScreen(
            isFirstTime = isFirstTime
        )

    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun AppScreen(isFirstTime: Boolean) {

    val navController = rememberNavController()

    key(erpAppLocale) {
        AnimatedNavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = if (isFirstTime) AppRoute.OnBoardingRoute else AppRoute.LoginRoute
        ) {
            onBoardingBuilder(navController = navController)
            loginScreenBuilder(navController = navController)
            dashboardScreenBuilder(
                navController = navController,
                onChatClick = {chatType->
                    navController.navigate(ChatRoute.ChatList(chatType = chatType))
                },
                onBirthdayUser = {json ->
                    navController.navigate(ChatRoute.ChatRoom(json = json))
                }
            )

            chatScreenBuilder(
                navController = navController
            )
        }
    }
}
