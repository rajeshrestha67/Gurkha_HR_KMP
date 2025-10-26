package com.gurkha.hr.res.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class Dimens(
    val extraSmall: Dp = 0.dp,
    val small1: Dp = 0.dp,
    val small2: Dp = 0.dp,
    val small3: Dp = 0.dp,
    val medium1: Dp = 0.dp,
    val medium2: Dp = 0.dp,
    val medium3: Dp = 0.dp,
    val large: Dp = 0.dp,
    val extraLarge: Dp = 0.dp,
    val borderWidth: Dp = 0.dp,
    val onBoardingIndicatorSelected: Dp = 0.dp,
    val onBoardingIndicatorUnSelected: Dp = 0.dp,
    val loginImageSize: Dp = 0.dp,
    val profileScreenImageSize: Dp = 0.dp,
    val bottomBar: Dp = 0.dp,
    val chartHeight: Dp = 0.dp,
    val leaveBoxHeight: Dp = 0.dp,
    val reasonTextField: Dp = 0.dp,
    val promptDialogSize: Dp = 0.dp,
    val swipeToDismissHeight: Dp = 0.dp,
    val heightForOptionBox: Dp = 0.dp,
    val eventWidth: Dp = 0.dp,
)


val CompactDimens = Dimens(
    extraSmall = 2.dp,
    small1 = 4.dp,
    small2 = 8.dp,
    small3 = 16.dp,
    medium1 = 24.dp,
    medium2 = 32.dp,
    medium3 = 48.dp,
    borderWidth = 2.dp,
    large = 56.dp,
    extraLarge = 64.dp,
    profileScreenImageSize = 90.dp,
    onBoardingIndicatorSelected = 30.dp,
    onBoardingIndicatorUnSelected = 10.dp,
    loginImageSize = 200.dp,
    bottomBar = 80.dp,
    chartHeight = 200.dp,
    leaveBoxHeight = 100.dp,
    reasonTextField = 80.dp,
    promptDialogSize = 120.dp,
    swipeToDismissHeight = 72.dp,
    heightForOptionBox = 100.dp,
    eventWidth= 200.dp
)
