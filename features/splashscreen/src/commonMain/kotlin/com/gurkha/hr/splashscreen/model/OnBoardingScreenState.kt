package com.gurkha.hr.splashscreen.model

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class OnBoardingScreenState(
    val currentPage: Int = 0,
    val screens: List<Screen> = ScreenList.screenList,
    val title: StringResource = SharedRes.Strings.next
)
