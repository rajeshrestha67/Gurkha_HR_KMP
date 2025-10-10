package com.gurkha.hr.components.date.horizontalCalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gurkha.hr.components.date.model.rememberCalendarModel
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.stringArrayResource

@Composable
fun HorizontalCalendar(
    modifier: Modifier = Modifier
) {
    val state: LazyListState = rememberLazyListState()
    val calendarModel = rememberCalendarModel()

    var selectedDay by remember {
        mutableStateOf(calendarModel.today.dayOfMonth)
    }

    val months = stringArrayResource(SharedRes.Arrays.months)

    val month = remember {
        months[calendarModel.today.month - 1]
    }

    val weekNames = stringArrayResource(SharedRes.Arrays.weeksDays)

    val days = remember { calendarModel.numberOfDaysInMonth() }
    val dayItemWidthDp = MaterialTheme.dimens.extraLarge


    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.dimens.small2,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(
                horizontal = MaterialTheme.dimens.small3
            ),
            text = month,
            style = MaterialTheme.typography.titleLarge
        )

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val screenWidth = maxWidth
            val density = LocalDensity.current

            val itemSpacing = MaterialTheme.dimens.small3
            LaunchedEffect(selectedDay) {
                val halfScreenPx = with(density) { (screenWidth / 2).toPx() }
                val itemWidthPx = with(density) { dayItemWidthDp.toPx() }
                val offset =
                    with(density) { (halfScreenPx - (itemWidthPx / 2) - itemSpacing.toPx()).toInt() }

                state.animateScrollToItem(
                    index = selectedDay - 1,
                    scrollOffset = -offset
                )
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.dimens.small3,
                    vertical = MaterialTheme.dimens.small2
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = itemSpacing,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                items(items = days, key = { it.day }) { item ->
                    val color = if (selectedDay == item.day)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.background
                    val textColor = if (selectedDay == item.day) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.primaryTextColor
                    }
                    Column(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .background(color = color)
                            .border(
                                width = 0.5.dp,
                                color = MaterialTheme.colorScheme.borderColor.copy(
                                    alpha = 0.5f
                                ),
                                shape = MaterialTheme.shapes.medium
                            )
                            .size(size = dayItemWidthDp)
                            .aspectRatio(ratio = 1f)
                            .clickable(onClick = {
                                println("called")
                                selectedDay = item.day
                            }),
                        verticalArrangement = Arrangement.spacedBy(
                            space = MaterialTheme.dimens.small1,
                            alignment = Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.day.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = if (item.isHoliday) MaterialTheme.colorScheme.error else textColor
                            )
                        )
                        Text(
                            text = weekNames[item.dayOfWeek - 1],
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = if (item.isHoliday) MaterialTheme.colorScheme.error else textColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}