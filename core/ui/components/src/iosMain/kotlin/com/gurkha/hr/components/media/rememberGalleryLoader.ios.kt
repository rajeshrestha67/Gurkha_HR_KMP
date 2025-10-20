package com.gurkha.hr.components.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Photos.PHAccessLevelReadWrite
import platform.Photos.PHAsset
import platform.Photos.PHAssetMediaTypeImage
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHContentEditingInputRequestOptions
import platform.Photos.PHPhotoLibrary
import platform.Photos.requestContentEditingInputWithOptions

@Composable
actual fun rememberGalleryLoader(
    onLoaded: (List<String>) -> Unit,
    onError: (Throwable) -> Unit
): () -> Unit {
    return remember {
        {
            val status = PHPhotoLibrary.authorizationStatusForAccessLevel(PHAccessLevelReadWrite)
            if (status == PHAuthorizationStatusAuthorized ||
                status == PHAuthorizationStatusLimited
            ) {
                fetchGalleryImages(onLoaded, onError)
            } else {
                PHPhotoLibrary.requestAuthorizationForAccessLevel(
                    PHAccessLevelReadWrite
                ) { newStatus ->
                    if (newStatus == PHAuthorizationStatusAuthorized ||
                        newStatus == PHAuthorizationStatusLimited
                    ) {
                        fetchGalleryImages(onLoaded, onError)
                    } else {
                        onError(Throwable("Photo access denied"))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun fetchGalleryImages(
    onLoaded: (List<String>) -> Unit,
    onError: (Throwable) -> Unit
) {
    try {
        val result = PHAsset.fetchAssetsWithMediaType(PHAssetMediaTypeImage, null)
        val uris = mutableListOf<String>()

        result.enumerateObjectsUsingBlock { asset, _, _ ->
            val phAsset = asset as? PHAsset ?: return@enumerateObjectsUsingBlock
            val options = PHContentEditingInputRequestOptions()
            options.canHandleAdjustmentData = { true }

            phAsset.requestContentEditingInputWithOptions(options) { input, _ ->
                input?.fullSizeImageURL?.absoluteString?.let { uri ->
                    uris.add(uri)
                }
            }
        }

        onLoaded(uris)
    } catch (e: Throwable) {
        onError(e)
    }
}
