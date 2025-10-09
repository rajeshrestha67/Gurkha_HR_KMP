package com.gurkha.hr.components.date.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.gurkha.hr.components.date.RecommendedSizeForAccessibility
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor


@Composable
fun WeekDays(calendarModel: CalendarModel) {

    Row(
        modifier = Modifier
            .defaultMinSize(
                minHeight = RecommendedSizeForAccessibility
            )
            .fillMaxWidth()
            .padding(end = MaterialTheme.dimens.small2),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        calendarModel.weekdayNames.forEachIndexed { index, name ->
            Box(
                modifier = Modifier
                    .size(
                        width = RecommendedSizeForAccessibility,
                        height = RecommendedSizeForAccessibility
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name,
                    modifier = Modifier,
                    color = if (index == 6) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primaryTextColor,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
