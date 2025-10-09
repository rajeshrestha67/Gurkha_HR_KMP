package com.gurkha.hr.components.date.horizontalCalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gurkha.hr.components.date.model.rememberCalendarModel
import com.gurkha.hr.components.date.state.rememberDateRangePickerState
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor

@Composable
fun HorizontalCalendar(
    modifier: Modifier = Modifier
) {
    val state: LazyListState = rememberLazyListState()
    val calendarModel = rememberCalendarModel()

    val dateState = rememberDateRangePickerState(
        calendarModel = calendarModel
    )

    val selectedDay = remember(calendarModel) {
        calendarModel.today.dayOfMonth
    }



    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.dimens.small2,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(
                horizontal = MaterialTheme.dimens.small3,
                vertical = MaterialTheme.dimens.small2
            ),
            text = "Calendar",
            style = MaterialTheme.typography.titleLarge
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            contentPadding = PaddingValues(
                horizontal = MaterialTheme.dimens.small3,
                vertical = MaterialTheme.dimens.small2
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = MaterialTheme.dimens.small2,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            items(items = calendarModel.numberOfDaysInMonth(), key = { it }) { item ->
                val color = if (selectedDay == item)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.background
                val textColor = if (selectedDay == item) {
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
                        .size(size = MaterialTheme.dimens.extraLarge)
                        .clickable(onClick = {
//                                    send the date to find there activities for that date
                        }),
                    verticalArrangement = Arrangement.spacedBy(
                        space = MaterialTheme.dimens.small1,
                        alignment = Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = item.toString(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = textColor
                        )
                    )
                    Text(
                        text = "Sun",
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