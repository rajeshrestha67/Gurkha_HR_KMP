package com.gurkha.hr.profile.model.profile_screen

data class ProfileScreenState (
    val generalItems: List<GeneralList> = GeneralList.list,
    val accountItems: List<AccountList> = AccountList.list,
    val selected: String = ""
)
