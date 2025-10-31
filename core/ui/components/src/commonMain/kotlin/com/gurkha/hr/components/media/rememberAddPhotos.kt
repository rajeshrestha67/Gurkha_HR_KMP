package com.gurkha.hr.components.media

import androidx.compose.runtime.Composable


@Composable
expect fun rememberAddPhotos(
    onLoaded: (List<String>) -> Unit,
    onError: (Throwable) -> Unit
): () -> Unit
