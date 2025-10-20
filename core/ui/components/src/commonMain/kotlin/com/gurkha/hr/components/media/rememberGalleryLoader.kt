package com.gurkha.hr.components.media

import androidx.compose.runtime.Composable

@Composable
expect fun rememberGalleryLoader(
    onLoaded: (List<String>) -> Unit,
    onError: (Throwable) -> Unit
): () -> Unit
