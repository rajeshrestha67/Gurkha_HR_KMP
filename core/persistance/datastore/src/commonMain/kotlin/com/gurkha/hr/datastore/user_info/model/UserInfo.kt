package com.gurkha.hr.datastore.user_info.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val isFirstTime: Boolean? = null
)
