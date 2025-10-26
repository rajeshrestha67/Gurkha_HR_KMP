package com.gurkha.hr.components.date.ui


import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.gurkha.hr.components.date.DatePickerHorizontalPadding
import com.gurkha.hr.components.date.MaxCalendarRows
import com.gurkha.hr.components.date.RecommendedSizeForAccessibility
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.noRippleClickable
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarMonth
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.primaryTextColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringArrayResource

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
    var yearPickerExpand by rememberSaveable { mutableStateOf(false) }
    var monthPickerExpand by rememberSaveable { mutableStateOf(false) }

    val yearRange = remember {
        calendarModel.getYearRange().map { it.toString() }
    }
    val monthRange = stringArrayResource(SharedRes.Arrays.months)


    Column(
        modifier = modifier
    ) {
        MonthNavigation(
            modifier = Modifier.fillMaxWidth(),
            displayMonth = displayedMonth,
            nextAvailable = monthsPagerState.canScrollForward,
            previousAvailable = monthsPagerState.canScrollBackward,
            monthPickerExpand = monthPickerExpand,
            yearPickerExpand = yearPickerExpand,
            onPreviousClick = {
                coroutineScope.launch {
                    try {
                        monthsPagerState.animateScrollToPage(
                            page = monthsPagerState.currentPage - 1
                        )
                    } catch (_: IllegalArgumentException) {
                        // Ignore. This may happen if the user clicked the "next" arrow fast while
                        // the list was still animating to the next item.
                    }
                }
            },
            onNextClick = {
                coroutineScope.launch {
                    try {
                        monthsPagerState.animateScrollToPage(
                            page = monthsPagerState.currentPage + 1
                        )
                    } catch (_: IllegalArgumentException) {
                        // Ignore. This may happen if the user clicked the "next" arrow fast while
                        // the list was still animating to the next item.
                    }
                }
            },
            onYearClick = {
                monthPickerExpand = false
                yearPickerExpand = !yearPickerExpand
            },
            onMonthClick = {
                yearPickerExpand = false
                monthPickerExpand = !monthPickerExpand
            }
        )
        Box {
            Column {
                WeekDays()
                HorizontalMonthsList(
                    pagerState = monthsPagerState,
                    selectedDate = selectedDate,
                    onDateSelectionChange = onDateSelectionChange,
                    onDisplayedMonthChange = onDisplayedMonthChange,
                    calendarModel = calendarModel
                )
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = yearPickerExpand || monthPickerExpand,
                modifier = Modifier.clipToBounds(),
                enter = expandVertically() + fadeIn(initialAlpha = 0.6f),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .requiredHeight(
                                height = RecommendedSizeForAccessibility * (MaxCalendarRows + 1) -
                                        DividerDefaults.Thickness - MaterialTheme.dimens.medium1
                            )
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = DatePickerHorizontalPadding),
                        columns = GridCells.Fixed(
                            count = 3
                        )
                    ) {
                        if (yearPickerExpand) {
                            items(items = yearRange, key = { it }) { year ->
                                ExpandItem(
                                    modifier = Modifier.noRippleClickable {
                                        coroutineScope.launch {
                                            monthsPagerState.animateScrollToPage(
                                                page = calendarModel.getPage(
                                                    year.toInt(), displayedMonth.month
                                                )
                                            )
                                            delay(200)
                                            yearPickerExpand = !yearPickerExpand
                                        }
                                    },
                                    selected = year == displayedMonth.year.toString(),
                                    text = year
                                )
                            }
                        } else {
                            items(items = monthRange, key = { it }) { month ->
                                ExpandItem(
                                    modifier = Modifier.noRippleClickable {
                                        coroutineScope.launch {
                                            monthsPagerState.animateScrollToPage(
                                                page = calendarModel.getPage(
                                                    displayedMonth.year,
                                                    monthRange.indexOf(month) + 1
                                                )
                                            )
                                            delay(200)
                                            monthPickerExpand = !monthPickerExpand
                                        }
                                    },
                                    selected = monthRange.indexOf(month) + 1 == displayedMonth.month,
                                    text = month
                                )
                            }
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun ExpandItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    text: String
) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            modifier = Modifier.background(
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.extraLarge
            ).padding(
                vertical = MaterialTheme.dimens.small3,
                horizontal = MaterialTheme.dimens.medium1
            ).wrapContentSize(),
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primaryTextColor,
                fontSize = 16.sp
            ),
            textAlign = TextAlign.Center
        )
    }

}

