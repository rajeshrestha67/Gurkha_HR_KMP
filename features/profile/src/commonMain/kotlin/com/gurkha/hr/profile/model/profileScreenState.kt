package com.gurkha.hr.profile.model

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class ProfileScreenState (
    val generalItems: List<GeneralList> = GeneralList.list,
    val accountItems: List<AccountList> = AccountList.list,
    val selected: String = ""
)
