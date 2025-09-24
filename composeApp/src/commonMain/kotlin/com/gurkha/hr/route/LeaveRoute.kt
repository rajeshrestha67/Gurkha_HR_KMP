package com.gurkha.hr.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface LeaveRoute {
    @Serializable
    data object LeaveRequestPageRoute : AppRoute
}