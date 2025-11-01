package com.gurkha.hr.components.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.atomicfu.atomic
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSLock
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.writeToURL
import platform.Photos.PHAccessLevelReadWrite
import platform.Photos.PHAsset
import platform.Photos.PHAssetMediaTypeImage
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHContentEditingInputRequestOptions
import platform.Photos.PHPhotoLibrary
import platform.Photos.requestContentEditingInputWithOptions
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberGalleryLoader(
    onLoaded: (List<String>) -> Unit,
    onError: (Throwable) -> Unit
): () -> Unit {
    return remember {
        {
            val status = PHPhotoLibrary.authorizationStatusForAccessLevel(PHAccessLevelReadWrite)
            if (status == PHAuthorizationStatusAuthorized || status == PHAuthorizationStatusLimited) {
                fetchGalleryImages(onLoaded, onError)
            } else {
                PHPhotoLibrary.requestAuthorizationForAccessLevel(PHAccessLevelReadWrite) { newStatus ->
                    if (newStatus == PHAuthorizationStatusAuthorized || newStatus == PHAuthorizationStatusLimited) {
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
        val fetchResult = PHAsset.fetchAssetsWithMediaType(PHAssetMediaTypeImage, null)
        val uris = mutableListOf<String>()
        val count = fetchResult.count.toInt()
        if (count == 0) {
            onLoaded(emptyList())
            return
        }

        val pending = atomic(count)
        val lock = NSLock()

        fetchResult.enumerateObjectsUsingBlock { asset, _, _ ->
            val phAsset = asset as? PHAsset ?: return@enumerateObjectsUsingBlock
            val options = PHContentEditingInputRequestOptions()
            options.canHandleAdjustmentData = { true }

            phAsset.requestContentEditingInputWithOptions(options) { input, _ ->
                input?.fullSizeImageURL?.absoluteString?.let { uri ->
                    lock.lock()
                    uris.add(uri)
                    lock.unlock()
                }

                if (pending.decrementAndGet() == 0) {
                    onLoaded(uris)
                }
            }
        }
    } catch (e: Throwable) {
        onError(e)
    }
}

@ExportObjCClass
class PickerDelegate(
    private val onPicked: (List<String>) -> Unit,
    private val onError: (Throwable) -> Unit
) : NSObject(), PHPickerViewControllerDelegateProtocol {

    // 👇 EXACT selector match for Objective-C bridging
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
        picker.dismissViewControllerAnimated(true, null)

        val uris = mutableListOf<String>()
        val total = didFinishPicking.size
        if (total == 0) {

            onPicked(emptyList())
            return
        }

        didFinishPicking.forEach { result ->
            val pickerResult = result as? PHPickerResult ?: return@forEach
            val itemProvider = pickerResult.itemProvider

            if (itemProvider.hasItemConformingToTypeIdentifier("public.image")) {
                itemProvider.loadFileRepresentationForTypeIdentifier("public.image") { url, error ->
                    val fileManager = NSFileManager.defaultManager()

                    if (error != null) {
                        onError(Throwable(error.localizedDescription))
                        return@loadFileRepresentationForTypeIdentifier
                    }

                    memScoped {
                        url?.let { tmpUrl ->
                            val docDir = NSSearchPathForDirectoriesInDomains(
                                NSDocumentDirectory, NSUserDomainMask, true
                            ).first() as String
                            val destUrl = NSURL.fileURLWithPath(docDir)
                                .URLByAppendingPathComponent(tmpUrl.lastPathComponent!!)

                            val data = NSData.dataWithContentsOfURL(tmpUrl)
                            if (data == null) {
                                onError(Throwable("Failed to read picked image"))
                                return@loadFileRepresentationForTypeIdentifier
                            }
                            destUrl?.let {
                                data.writeToURL(destUrl, true)
                                dispatch_async(dispatch_get_main_queue()) {
                                    uris.add(destUrl.path!!)
                                    // ✅ Only call onPicked when all are processed
                                    if (uris.size == total) {
                                        onPicked(uris)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}