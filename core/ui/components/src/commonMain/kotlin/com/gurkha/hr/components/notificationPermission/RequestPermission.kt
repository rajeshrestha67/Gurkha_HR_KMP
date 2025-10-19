package com.gurkha.hr.components.notificationPermission

import androidx.compose.runtime.Composable

//@Composable
//expect fun RequestPermission(
//    permissions: List<String>,
//    onGranted: (String) -> Unit = {},
//    onDenied: (String) -> Unit = {},
//    onPermanentlyDenied: (String) -> Unit = {},
//    onAllGranted: () -> Unit = {}
//)

@Composable
expect fun rememberRequestPermission(
    permissions: List<String>,
    onGranted: (String) -> Unit = {},
    onDenied: (String) -> Unit = {},
    onPermanentlyDenied: (String) -> Unit = {},
    onAllGranted: () -> Unit = {}
): () -> Unit