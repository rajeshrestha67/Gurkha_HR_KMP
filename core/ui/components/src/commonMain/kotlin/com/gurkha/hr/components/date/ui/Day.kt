package com.gurkha.hr.components.date.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gurkha.hr.components.date.DaySize
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.date.mapNumbers


@Composable
fun Day(
    modifier: Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    today: Boolean,
    isSaturday: Boolean = false,
    content: @Composable () -> Unit
) {

    val color = if (today && selected) {
        MaterialTheme.colorScheme.primary
    } else if (today) {
        Color.Transparent
    } else if (isSaturday && selected) {
        MaterialTheme.colorScheme.error
    } else if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    val contentColor = if (today && selected) {
        MaterialTheme.colorScheme.onPrimary
    } else if (today) {
        MaterialTheme.erpColors.primaryTextColor
    } else if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else if (isSaturday) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.erpColors.primaryTextColor
    }

    Surface(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = color,
        contentColor = contentColor,
        border = if (selected || today) {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            null
        }
    ) {
        Box(
            modifier = Modifier.height(
                height = DaySize
            ).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun DayItem(
    isBS: Boolean,
    dateInAD: String,
    dateInBS: String,
    isHoliday: Boolean,
    isSelected: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dateInBS.mapNumbers,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )

//        Text(
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(horizontal = MaterialTheme.dimens.small1)
//                .padding(bottom = MaterialTheme.dimens.small1 / 2),
//            text = dateInAD,
//            textAlign = TextAlign.Right,
//            style = MaterialTheme.typography.bodySmall.copy(
//                color = if (isHoliday && isSelected) MaterialTheme.colorScheme.onPrimary else if (isHoliday) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondaryTextColor,
//                fontSize = 10.sp
//            )
//        )
    }
}