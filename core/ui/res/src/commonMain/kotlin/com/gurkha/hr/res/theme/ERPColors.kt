package com.gurkha.hr.res.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color


@Immutable
data class ERPColorPalette(
    val imageBackgroundColor: Color = Color.Unspecified,
    val lightGreenColor: Color = Color.Unspecified,
    val primaryTextColor: Color = Color.Unspecified,
    val secondaryTextColor: Color = Color.Unspecified,
    val onBoardingIndicatorSelectedColor: Color = Color.Unspecified,
    val onBoardingIndicatorUnSelectedColor: Color = Color.Unspecified,
    val linkColor: Color = Color.Unspecified,
    val highLightColor: Color = Color.Unspecified,
    val logOutTextColor: Color = Color.Unspecified,
    val lightRedColor: Color = Color.Unspecified,
    val darkPrimaryTextColor: Color = Color.Unspecified,
    val chatBackgroundColor: Color = Color.Unspecified,
    val inComingBubbleColor: Color = Color.Unspecified,
    val outGoingBubbleColor: Color = Color.Unspecified,
    val holidayBlueColor: Color = Color.Unspecified,
    val inComingTextColor: Color = Color.Unspecified,
    val chatSecondaryTextColor: Color = Color.Unspecified,
    val attendanceHoliday: Color = Color.Unspecified,
    val veryLightGray: Color = Color.Unspecified,
    val disabledTextFieldBorderColor: Color = Color.Unspecified

)

val lightERPPalette = ERPColorPalette(
    imageBackgroundColor = Color(0xFFF5F5F5),
    lightGreenColor = Color(0xFF4CAF50),
    primaryTextColor = Color(0xFF646884),
    secondaryTextColor = Color(0xFFa2a5b9),
    onBoardingIndicatorSelectedColor = Color(0xFFBA1A1A),
    onBoardingIndicatorUnSelectedColor = Color(0xFFB0B0B0),
    linkColor = Color(color = 0xFF0288D1),
    highLightColor = Color(0xB2FFFFFF),
    logOutTextColor = Color(0xFFC62828),
    lightRedColor = Color(0xFFEF5350),
    darkPrimaryTextColor = Color(0xFF555975),
    veryLightGray = Color(0xFFF3F6F4),
    disabledTextFieldBorderColor = Color(0xFFCCCCCC),
    chatBackgroundColor = Color(0xFFFAFAFA),
    inComingBubbleColor = Color(0x604B662C),
    outGoingBubbleColor = Color(0xFFDDDDDD),
    holidayBlueColor = Color(0xFF03A9F4),
    inComingTextColor = Color(0xFF111111),
    chatSecondaryTextColor = Color(0xFF757575),
    attendanceHoliday = Color(0xFFFFA000)
)

val darkERPPalette = ERPColorPalette(
    imageBackgroundColor = Color(0xFF212121),
    lightGreenColor = Color(0xFF90EE90),
    primaryTextColor = Color(0XFFcbcdd9),
    secondaryTextColor = Color(0xFFA2A5B9),
    onBoardingIndicatorSelectedColor = Color(0xFFFFB4AB),
    onBoardingIndicatorUnSelectedColor = Color(0xFFB0B0B0),
    linkColor = Color(color = 0xFF0288D1),
    highLightColor = Color(0xB2FFFFFF),
    logOutTextColor = Color(0xFFE53935),
    lightRedColor = Color(0xFFF44336),
    darkPrimaryTextColor = Color(0xFFF2F2F2),
    veryLightGray = Color(0xFFF3F6F4),
    disabledTextFieldBorderColor = Color(0xFF444444),
    chatBackgroundColor = Color(0xFF121212),
    inComingBubbleColor = Color(0x60B1D18A),
    outGoingBubbleColor = Color(0xFF3A3A3A),
    holidayBlueColor = Color(0xFF2196F3),
    inComingTextColor = Color(0xFFE0E0E0),
    chatSecondaryTextColor = Color(0xFFAAAAAA),
    attendanceHoliday = Color(0xFFBF6F00)
)
