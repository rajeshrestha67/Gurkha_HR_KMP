package com.gurkha.hr.components.date.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gurkha.hr.components.date.DaysInWeek
import com.gurkha.hr.components.date.RecommendedSizeForAccessibility
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarMonth
import com.gurkha.hr.date.mapNumbers
import com.gurkha.hr.res.theme.dimens
import kotlin.math.ceil


@Composable
fun Month(
    month: CalendarMonth,
    onDateSelectionChange: (calendarDate: CalendarDate) -> Unit,
    today: CalendarDate,
    selectedDate: CalendarDate?,
) {

    var cellIndex = 0
    val maxNumberOfColumn = ceil(
        (month.numberOfDays + month.daysFromStartOfWeekToFirstOfMonth) / 7.0
    ).toInt() + 1
    Column(
        modifier = Modifier.requiredHeight((RecommendedSizeForAccessibility * maxNumberOfColumn) + MaterialTheme.dimens.small3),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        for (weekIndex in 0 until maxNumberOfColumn) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    2.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (dayIndex in 0 until DaysInWeek) {
                    if (cellIndex < month.daysFromStartOfWeekToFirstOfMonth ||
                        cellIndex >=
                        (month.daysFromStartOfWeekToFirstOfMonth + month.numberOfDays)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .requiredSize(
                                    width = RecommendedSizeForAccessibility,
                                    height = RecommendedSizeForAccessibility
                                )
                                .weight(1f)
                        )
                    } else {
                        val dayNumber = cellIndex - month.daysFromStartOfWeekToFirstOfMonth
                        val date = month.toDate(dayNumber + 1)
                        val isToday = date == today
                        val calculatedDate =
                            dayNumber + month.startDate // Represents a potential date in the calendar

                        val dateInAD = if (calculatedDate <= month.endDate)
                            calculatedDate // Date falls within current month
                        else
                            month.startDate + dayNumber - month.endDate // Wraps to next/previous month

                        val dateInBS = dayNumber + 1
                        val isSaturday = (cellIndex + 1) % 7 == 0
                        val isSelected = date == selectedDate
                        Day(
                            modifier = Modifier.weight(1f),
                            selected = isSelected,
                            onClick = { onDateSelectionChange(date) },
                            isSaturday = isSaturday,
                            today = isToday,
                        ) {
                            DayItem(
                                isBS = false,
                                dateInAD = dateInAD.toString(),
                                dateInBS = dateInBS.toString().mapNumbers,
                                isHoliday = isSaturday,
                                isSelected = isSelected
                            )
                        }
                    }
                    cellIndex++
                }
            }
        }
    }
}
