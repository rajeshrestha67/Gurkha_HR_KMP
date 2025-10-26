package com.gurkha.hr.model.viewAll

import com.gurkha.model.upComingBirthday.ui.ViewAllUi

data class ViewAllScreenState(
    val data : List<ViewAllUi>? = emptyList(),
    val title : String? = ""
)