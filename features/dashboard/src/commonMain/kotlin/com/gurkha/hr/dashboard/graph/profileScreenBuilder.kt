package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.profile.profile_screen.ProfileScreen
import com.gurkha.hr.profile.model.profile_screen.AccountList
import com.gurkha.hr.profile.model.profile_screen.GeneralList


fun NavGraphBuilder.profileScreenBuilder(
    onLogout: () -> Unit,
    onGeneralClick: (GeneralList) -> Unit,
    onAccountClick: (AccountList) -> Unit
) {
    composable<DashboardRoute.ProfileRoute> {
        ProfileScreen(
            onLogout = onLogout,
            onAccountClick = onAccountClick,
            onGeneralClick = onGeneralClick,
        )
    }
}
