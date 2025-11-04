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
    val disabledTextFieldBorderColor: Color = Color.Unspecified,
    val box1OutlineColor: Color = Color.Unspecified,
    val box2OutlineColor: Color = Color.Unspecified,
    val box3OutlineColor: Color = Color.Unspecified,
    val box4OutlineColor: Color = Color.Unspecified,
    val box1BackgroundColor: Color = Color.Unspecified,
    val box2BackgroundColor: Color = Color.Unspecified,
    val box3BackgroundColor: Color = Color.Unspecified,
    val box4BackgroundColor: Color = Color.Unspecified

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
    attendanceHoliday = Color(0xFFFFA000),
    box1OutlineColor = Color(0xFF81D4FA),
    box2OutlineColor = Color(0xFFA5D6A7),
    box3OutlineColor = Color(0xFFC5E1A5),
    box4OutlineColor = Color(0xFFEF9A9A),
    box1BackgroundColor = Color(0xFFE1F5FE),
    box2BackgroundColor = Color(0xFFE8F5E9),
    box3BackgroundColor = Color(0xFFF1F8E9),
    box4BackgroundColor = Color(0xFFFFEBEE),



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
    attendanceHoliday = Color(0xFFBF6F00),
    box1OutlineColor = Color(0xFF0288D1),
    box2OutlineColor = Color(0xFF2E7D32),
    box3OutlineColor = Color(0xFF558B2F),
    box4OutlineColor = Color(0xFFC62828),
    box1BackgroundColor = Color(0xFF0B3D91),
    box2BackgroundColor = Color(0xFF174D1F),
    box3BackgroundColor = Color(0xFF2B3E16),
    box4BackgroundColor = Color(0xFF7F1A1A)
)
