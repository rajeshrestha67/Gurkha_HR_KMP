package com.gurkha.hr.leave.model

interface LeaveScreenAction {
    data class OnStatusChange(val status: String) : LeaveScreenAction

}