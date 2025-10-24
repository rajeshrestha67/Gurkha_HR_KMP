package com.gurkha.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val isFirstTime: Boolean? = null,
    val userThemeMode: Int = 2,
    val langCode: String = "en"
)