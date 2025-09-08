package com.gurkha.hr.datastore.userInfo.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val isFirstTime: Boolean? = null
)
