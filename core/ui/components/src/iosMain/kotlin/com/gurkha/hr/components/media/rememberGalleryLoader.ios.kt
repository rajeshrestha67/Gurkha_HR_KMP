package com.gurkha.hr.components.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Photos.PHPhotoLibrary
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun rememberGalleryLoader(
    onLoaded: (List<String>) -> Unit,
    onError: (Throwable) -> Unit
): () -> Unit {
    val delegate = remember { PickerDelegate(onLoaded, onError) }

    return remember {
        {
            try {
                val configuration =
                    PHPickerConfiguration(PHPhotoLibrary.sharedPhotoLibrary()).apply {
                        filter = PHPickerFilter.imagesFilter() // ✅ images only
                        selectionLimit = 0                     // ✅ unlimited selection
                    }

                val picker = PHPickerViewController(configuration)
                picker.delegate = delegate

                val rootVC = UIApplication.sharedApplication
                    .keyWindow?.rootViewController
                rootVC?.presentViewController(picker, true, null)
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }
}

//
//@OptIn(ExperimentalForeignApi::class)
//private fun fetchGalleryImages(
//    onLoaded: (List<String>) -> Unit,
//    onError: (Throwable) -> Unit
//) {
//    try {
//        val result = PHAsset.fetchAssetsWithMediaType(PHAssetMediaTypeImage, null)
//        val uris = mutableListOf<String>()
//
//        result.enumerateObjectsUsingBlock { asset, _, _ ->
//            val phAsset = asset as? PHAsset ?: return@enumerateObjectsUsingBlock
//            val options = PHContentEditingInputRequestOptions()
//            options.canHandleAdjustmentData = { true }
//
//            phAsset.requestContentEditingInputWithOptions(options) { input, _ ->
//                input?.fullSizeImageURL?.absoluteString?.let { uri ->
//                    uris.add(uri)
//                }
//            }
//        }
//
//        onLoaded(uris)
//    } catch (e: Throwable) {
//        onError(e)
//    }
//}
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
        println("images total ")
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

                            destUrl?.let {
                                try {
                                    val errorPtr = alloc<ObjCObjectVar<NSError?>>()
                                    val success =
                                        fileManager.copyItemAtURL(tmpUrl, destUrl, errorPtr.ptr)


                                    if (!success) {
                                        val nsError = errorPtr.value
                                        onError(
                                            Throwable(
                                                nsError?.localizedDescription
                                                    ?: "Unknown copy error"
                                            )
                                        )
                                    } else {
                                        dispatch_async(dispatch_get_main_queue()) {
                                            uris.add(destUrl.absoluteString ?: "")
                                            if (uris.size == total) onPicked(uris)
                                        }
                                    }
                                } catch (e: Exception) {
                                    onError(Throwable(e.message))
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}