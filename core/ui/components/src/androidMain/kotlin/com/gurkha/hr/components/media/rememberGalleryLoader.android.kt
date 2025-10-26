package com.gurkha.hr.components.media

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberGalleryLoader(
    onLoaded: (List<String>) -> Unit,
    onError: (Throwable) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(500) // up to 50 images
    ) { uris ->
        try {
            onLoaded(uris.map { it.toString() })
        } catch (e: Exception) {
            onError(e)
        }
    }

    return remember {
        {
            launcher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }
}