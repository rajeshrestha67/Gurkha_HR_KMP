package com.gurkha.hr.components.date.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.gurkha.hr.date.data.CalendarMonth
import com.gurkha.hr.date.months
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor

@Composable
fun MonthNavigation(
    modifier: Modifier = Modifier,
    displayMonth: CalendarMonth,
    nextAvailable: Boolean,
    previousAvailable: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onYearClick: () -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.dimens.small3,
            alignment = Alignment.CenterHorizontally
        )
    ) {

        IconButton(
            onClick = onPreviousClick,
            enabled = previousAvailable
        ) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "Previous",
                tint = MaterialTheme.colorScheme.primaryTextColor
            )
        }


//        Text(
//            modifier = Modifier.wrapContentWidth(),
//            text = "${months[displayMonth.month - 1].second}, ${displayMonth.year}",
//            style = MaterialTheme.typography.titleMedium.copy(
//                color = MaterialTheme.colorScheme.primaryTextColor
//            ),
//            textAlign = TextAlign.Center
//        )

        Row(
            modifier = Modifier.wrapContentWidth().clickable(onClick = onYearClick),
            horizontalArrangement = Arrangement.spacedBy(
                space = MaterialTheme.dimens.small1,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            Text(
                text = "${months[displayMonth.month - 1].second}, ${displayMonth.year}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                ),
                textAlign = TextAlign.Center
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "drop down",
                tint = MaterialTheme.colorScheme.primaryTextColor
            )
        }

        IconButton(
            onClick = onNextClick,
            enabled = nextAvailable
        ) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Next",
                tint = MaterialTheme.colorScheme.primaryTextColor
            )
        }
    }

}