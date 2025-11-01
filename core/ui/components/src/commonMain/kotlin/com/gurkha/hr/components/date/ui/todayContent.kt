package com.gurkha.hr.components.date.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.stringResource

@Composable
fun TodayContent(
    modifier: Modifier = Modifier,
    bsDate: String,
    adDate: String,
    onTodayClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            Text(
                text = bsDate,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.erpColors.primaryTextColor
                )
            )
            Text(
                text = adDate,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.erpColors.secondaryTextColor
                )
            )

        }

        ERPButton(
            text = stringResource(SharedRes.Strings.today),
            onClick = onTodayClick
        )
    }
}