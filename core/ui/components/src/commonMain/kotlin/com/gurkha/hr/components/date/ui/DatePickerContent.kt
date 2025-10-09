package com.gurkha.hr.components.date.ui


import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.gurkha.hr.components.date.DatePickerHorizontalPadding
import com.gurkha.hr.components.date.MaxCalendarRows
import com.gurkha.hr.components.date.RecommendedSizeForAccessibility
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarMonth
import com.gurkha.hr.date.data.model.CalendarModel
import kotlinx.coroutines.launch

@Composable
fun DatePickerContent(
    modifier: Modifier,
    monthsPagerState: PagerState,
    selectedDate: CalendarDate?,
    displayedMonth: CalendarMonth,
    onDateSelectionChange: (calendarDate: CalendarDate) -> Unit,
    onDisplayedMonthChange: (calendarMonth: CalendarMonth) -> Unit,
    calendarModel: CalendarModel
) {


    val coroutineScope = rememberCoroutineScope()
    var yearPickerVisible by rememberSaveable { mutableStateOf(false) }
    Box(modifier = modifier) {
        Column {
            MonthNavigation(
                modifier = Modifier.fillMaxWidth(),
                displayMonth = displayedMonth,
                nextAvailable = monthsPagerState.canScrollForward,
                previousAvailable = monthsPagerState.canScrollBackward,
                onPreviousClick = {
                    coroutineScope.launch {
                        monthsPagerState.animateScrollToPage(
                            page = monthsPagerState.currentPage - 1
                        )
                    }
                },
                onNextClick = {
                    coroutineScope.launch {
                        monthsPagerState.animateScrollToPage(
                            page = monthsPagerState.currentPage + 1
                        )
                    }
                },
                onYearClick = {
                    yearPickerVisible = true
                }
            )
            WeekDays(
                calendarModel = calendarModel
            )
            HorizontalMonthsList(
                pagerState = monthsPagerState,
                selectedDate = selectedDate,
                onDateSelectionChange = onDateSelectionChange,
                onDisplayedMonthChange = onDisplayedMonthChange,
                calendarModel = calendarModel
            )
        }
        androidx.compose.animation.AnimatedVisibility(
            visible = yearPickerVisible,
            modifier = Modifier.clipToBounds(),
            enter = expandVertically() + fadeIn(initialAlpha = 0.6f),
            exit = shrinkVertically() + fadeOut()
        ) {

            LazyColumn(
                modifier = Modifier
                    .requiredHeight(
                        RecommendedSizeForAccessibility * (MaxCalendarRows + 1) -
                                DividerDefaults.Thickness
                    )
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = DatePickerHorizontalPadding)
            ) {

                items(100) {
                    Text(
                        text = "text $it"
                    )
                }
            }
        }
    }
}

