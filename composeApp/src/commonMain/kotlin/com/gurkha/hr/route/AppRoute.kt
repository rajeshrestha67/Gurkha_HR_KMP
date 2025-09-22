package com.gurkha.hr.route

import kotlinx.serialization.Serializable


@Serializable
sealed interface AppRoute{
    @Serializable
    data object LoginRoute: AppRoute
    @Serializable
    data object DashboardRoute: AppRoute

    @Serializable
    data object OnBoardingRoute: AppRoute



}