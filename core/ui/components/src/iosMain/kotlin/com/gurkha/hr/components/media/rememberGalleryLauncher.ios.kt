package com.gurkha.hr.components.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExportObjCClass
import platform.Foundation.NSDate
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.writeToURL
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

@Composable
actual fun rememberGalleryLauncher(
    onImageSelected: (String) -> Unit,
    onError: (Throwable) -> Unit
): () -> Unit {
    val viewController = LocalUIViewController.current

    return remember {
        {
            try {
                val picker = UIImagePickerController().apply {
                    sourceType =
                        UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
                    delegate = GalleryPickerDelegate(onImageSelected, onError)
                }
                viewController?.presentViewController(picker, true, null)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}

@OptIn(BetaInteropApi::class)
@ExportObjCClass
private class GalleryPickerDelegate(
    val onSelected: (String) -> Unit,
    val onError: (Throwable) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        if (image != null) {
            val imageData = UIImagePNGRepresentation(image)
            if (imageData != null) {
                val filePath =
                    NSTemporaryDirectory() + "/gallery_${NSDate().timeIntervalSince1970}.png"
                val fileUrl = NSURL.fileURLWithPath(filePath)
                val success = imageData.writeToURL(fileUrl, true)
                if (success) fileUrl.absoluteString?.let { onSelected(it) }
                else onError(Exception("Failed to save image"))
            } else {
                onError(Exception("Failed to convert image to PNG"))
            }
        } else {
            onError(Exception("No image selected"))
        }
        picker.dismissViewControllerAnimated(true, null)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, null)
    }
}