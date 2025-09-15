package com.gurkha.hr.profile.model

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class ProfileScreenState (
    val generalItems: List<ScreenItem> = ScreenList.general,
    val accountItems: List<ScreenItem> = ScreenList.account,
    val selected: String = ""
)
