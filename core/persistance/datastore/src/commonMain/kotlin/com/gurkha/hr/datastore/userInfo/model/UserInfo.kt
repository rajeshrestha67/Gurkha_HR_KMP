package com.gurkha.hr.datastore.userInfo.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val name: String ? = null,
    val email: String ? = null,
    val isFirstTime: Boolean? = null
)
