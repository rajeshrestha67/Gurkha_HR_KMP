package com.gurkha.hr.components.date.horizontalCalendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarDay
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.highLightColor
import com.gurkha.hr.res.theme.linkColor
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HorizontalCalendar(
    modifier: Modifier = Modifier,
    days: List<CalendarDay> = listOf(),
    today: CalendarDate,
    selectedDay: Int,
    isLoading: Boolean,
    onDaySelected: (Int) -> Unit
) {
    val listState: LazyListState = rememberLazyListState()

    val months = stringArrayResource(SharedRes.Arrays.months)

    val month = remember(today) {
        months[(today.month - 1).coerceAtLeast(0)]
    }

    val weekNames = stringArrayResource(SharedRes.Arrays.weeksDays)

    val dayItemWidthDp = MaterialTheme.dimens.extraLarge


    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.dimens.small2,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().height(MaterialTheme.dimens.medium3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f).padding(
                    horizontal = MaterialTheme.dimens.small3
                ),
                text = month,
                style = MaterialTheme.typography.titleLarge
            )

            AnimatedVisibility(selectedDay != today.dayOfMonth) {
                TextButton(
                    modifier = Modifier,
                    onClick = {
                        onDaySelected(today.dayOfMonth)
                    }
                ) {
                    Text(
                        text = stringResource(SharedRes.Strings.today),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.linkColor
                        )
                    )
                }
            }

        }


        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val screenWidth = maxWidth
            val density = LocalDensity.current

            val itemSpacing = MaterialTheme.dimens.small3
            LaunchedEffect(selectedDay, isLoading) {
                if (isLoading) {
                    return@LaunchedEffect
                }
                val halfScreenPx = with(density) { (screenWidth / 2).toPx() }
                val itemWidthPx = with(density) { dayItemWidthDp.toPx() }
                val offset =
                    with(density) { (halfScreenPx - (itemWidthPx / 2) - itemSpacing.toPx()).toInt() }

                listState.animateScrollToItem(
                    index = selectedDay - 1,
                    scrollOffset = -offset
                )
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = listState,
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

                if (isLoading) {
                    items(10) {
                        ShimmerView(
                            modifier = Modifier.clip(MaterialTheme.shapes.medium)
                                .size(size = dayItemWidthDp)
                                .aspectRatio(ratio = 1f)
                        )
                    }
                } else {
                    items(items = days, key = { it.day }) { item ->

                        val color = if (selectedDay == item.day)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.highLightColor

                        val textColor = if (selectedDay == item.day) {
                            MaterialTheme.colorScheme.onPrimary
                        } else if (item.isHoliday && selectedDay == item.day) {
                            MaterialTheme.colorScheme.onPrimary
                        } else if (item.isHoliday) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primaryTextColor
                        }

                        val borderModifier = if (item.day == today.dayOfMonth) Modifier.border(
                            width = 0.5.dp,
                            color = MaterialTheme.colorScheme.borderColor.copy(
                                alpha = 0.5f
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) else Modifier

                        Column(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(color = color)
                                .then(borderModifier)
                                .size(size = dayItemWidthDp)
                                .aspectRatio(ratio = 1f)
                                .clickable(
                                    onClick = {
                                        onDaySelected(item.day)
                                    }
                                ),
                            verticalArrangement = Arrangement.spacedBy(
                                space = MaterialTheme.dimens.small1,
                                alignment = Alignment.CenterVertically
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = item.day.toString(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = textColor
                                )
                            )
                            Text(
                                text = weekNames[item.dayOfWeek - 1],
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = textColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

            }
        }
    }
}