package com.gurkha.hr.components.permissions

import androidx.compose.runtime.Composable

@Composable
expect fun rememberRequestPermission(
    permissions: List<String>,
    onGranted: (String) -> Unit = {},
    onDenied: (String) -> Unit = {},
    onPermanentlyDenied: (String) -> Unit = {},
    onAllGranted: () -> Unit = {}
): () -> Unit