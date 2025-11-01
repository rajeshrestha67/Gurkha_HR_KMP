package com.gurkha.model.upComingBirthday.ui

import kotlinx.serialization.Serializable

@Serializable
data class ViewAllUi(
    val fullName: String,
    val designationName : String,
    val branchName : String,
    val imageUrl : String,
    val initials : String,
    val date : String
)