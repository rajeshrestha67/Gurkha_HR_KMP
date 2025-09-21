package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.profile.ProfileScreen
import com.gurkha.hr.profile.model.AccountList

fun NavGraphBuilder.profileScreenBuilder(
    onLogout:() -> Unit,
    onAccountClick:(AccountList) -> Unit) {
    composable<DashboardRoute.ProfileRoute> {
        ProfileScreen(
            onLogout = onLogout,
            onAccountClick = onAccountClick
        )
    }
}
