package com.gurkha.hr

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gurkha.hr.graph.loginScreenBuilder
import com.gurkha.hr.route.AppRoute
import com.gurkha.hr.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    AppTheme {
        AppScreen()
    }
}

@Composable
fun AppScreen() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppRoute.LoginRoute,
        popExitTransition = {
            scaleOut(
                targetScale = 0.9f,
                transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.5f)
            )
        },
        popEnterTransition = {
            EnterTransition.None
        }
    ){
        loginScreenBuilder(navController = navController)

    }
}
