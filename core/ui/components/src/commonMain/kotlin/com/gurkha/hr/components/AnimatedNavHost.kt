package com.gurkha.hr.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass
import kotlin.reflect.KType


internal const val ANIMATION_DURATION = 300

@Composable
fun AnimatedNavHost(
    navController: NavHostController,
    startDestination: Any,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    route: KClass<*>? = null,
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    builder: NavGraphBuilder.() -> Unit
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        contentAlignment = contentAlignment,
        route = route,
        typeMap = typeMap,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(ANIMATION_DURATION)
            )

        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(ANIMATION_DURATION)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(ANIMATION_DURATION)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(ANIMATION_DURATION)
            )
        },
        builder = builder
    )
}
