package com.gurkha.hr.datastore.user_data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val email: String = ""
)
