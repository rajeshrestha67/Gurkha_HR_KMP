package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.home.HomeScreen

fun NavGraphBuilder.homeScreenBuilder(
    navController: NavHostController
) {

    composable<DashboardRoute.HomeRoute> {
        HomeScreen()
    }
}