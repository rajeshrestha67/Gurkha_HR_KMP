package com.gurkha.hr.missedAttendanceScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.stringResource

@Composable
fun MissedAttendanceScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(SharedRes.Strings.coming_soon),
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.erpColors.primaryTextColor
                )
            )
        }
    }
}