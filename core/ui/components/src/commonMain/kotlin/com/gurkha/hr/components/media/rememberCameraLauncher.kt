package com.gurkha.hr.components.media

import androidx.compose.runtime.Composable

@Composable
expect fun rememberCameraLauncher(
    onImageCaptured: (String) -> Unit,
    onError: (Throwable) -> Unit = {}
): () -> Unit