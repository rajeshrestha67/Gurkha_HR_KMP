package com.gurkha.hr.datastore.token.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val jwtToken: String? = null
)
