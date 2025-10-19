package com.gurkha.hr.components.media

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberGalleryLauncher(
    onImageSelected: (String) -> Unit,
    onError: (Throwable) -> Unit
): () -> Unit {

    // Launcher for picking a single image from gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onImageSelected(uri.toString())
        } else {
            onError(Exception("No image selected"))
        }
    }

    return remember {
        {
            try {
                // Launch picker for images only
                galleryLauncher.launch("image/*")
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}