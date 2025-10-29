package com.gurkha.hr.components.loadingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.erpColors.primaryTextColor.copy(
        alpha = 0.3f
    )
) {
    Box(
        modifier = modifier.fillMaxSize().background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(MaterialTheme.dimens.medium3),
            color = MaterialTheme.colorScheme.secondaryContainer
        )
    }
}