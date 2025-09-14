package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.report.ReportScreen


fun NavGraphBuilder.reportScreenBuilder(navController: NavController) {
    composable<DashboardRoute.ReportRoute> {
        ReportScreen()
    }
}