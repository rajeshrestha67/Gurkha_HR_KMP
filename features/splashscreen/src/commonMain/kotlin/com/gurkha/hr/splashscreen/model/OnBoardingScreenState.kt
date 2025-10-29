package com.gurkha.hr.splashscreen.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class OnBoardingScreenState(
    val currentPage: Int = 0,
    val screens: List<Screen> = ScreenList.screenList,
    val title: StringResource = SharedRes.Strings.next
) {
    val indicators: List<Indicator>
        @Composable get() = screens.mapIndexed { index, _ ->
            val isSelected = index == currentPage
            Indicator(
                color = if (isSelected) MaterialTheme.erpColors.onBoardingIndicatorSelectedColor else MaterialTheme.erpColors.onBoardingIndicatorUnSelectedColor,
                width = if (isSelected) MaterialTheme.dimens.onBoardingIndicatorSelected else MaterialTheme.dimens.onBoardingIndicatorUnSelected,
            )
        }
}


