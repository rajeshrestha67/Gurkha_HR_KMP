package com.gurkha.model.leave.ui

import kotlinx.serialization.Serializable

@Serializable
data class LeaveDurationUi(
    val name: String,
    val value: String
){
    override fun toString(): String {
        return name
    }
}
