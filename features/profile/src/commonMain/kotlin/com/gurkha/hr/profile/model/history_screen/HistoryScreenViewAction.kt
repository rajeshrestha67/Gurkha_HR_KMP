package com.gurkha.hr.profile.model.history_screen

import com.gurkha.hr.components.date.DateData

interface HistoryScreenViewAction {

    data class fromYear(val year: Int) : HistoryScreenViewAction
    data class fromMonth(val month: Int) : HistoryScreenViewAction

    data object Submit : HistoryScreenViewAction
    
}