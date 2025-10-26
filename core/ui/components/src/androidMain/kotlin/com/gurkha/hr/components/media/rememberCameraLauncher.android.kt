package com.gurkha.hr.components.media

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

@Composable
actual fun rememberCameraLauncher(
    onImageCaptured: (String) -> Unit,
    onError: (Throwable) -> Unit
): () -> Unit {
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = photoUri
        if (success && uri != null) {
            onImageCaptured(uri.toString())
        } else {
            onError(Exception("Failed to capture image"))
        }
    }

    return remember {
        {
            try {
                val imageFile = createImageFile(context)
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    imageFile
                )
                photoUri = uri
                takePictureLauncher.launch(uri)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}

private fun createImageFile(context: Context): File {
    val dir = File(context.cacheDir, "images")
    if (!dir.exists()) dir.mkdirs()
    return File.createTempFile("captured_", ".jpg", dir)
}