package com.gurkha.hr.components.media

import androidx.compose.runtime.Composable


@Composable
expect fun rememberGalleryLauncher(
    onImageSelected: (String) -> Unit,
    onError: (Throwable) -> Unit = {}
): () -> Unit