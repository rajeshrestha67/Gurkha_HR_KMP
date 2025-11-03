package com.gurkha.hr.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformWebView(
    modifier: Modifier = Modifier,
    url: String
)