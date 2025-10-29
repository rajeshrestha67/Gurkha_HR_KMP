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
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject


@Composable
actual fun rememberCameraLauncher(
    onImageCaptured: (String) -> Unit,
    onError: (Throwable) -> Unit
): () -> Unit {
    val viewController = LocalUIViewController.current
    val delegate = remember { ImagePickerDelegate(onImageCaptured, onError) }

    return remember {
        {
            val picker = UIImagePickerController().apply {
                sourceType =
                    UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                this.delegate = delegate
            }
            viewController.presentViewController(picker, true, null)
        }
    }
}

@OptIn(BetaInteropApi::class)
@ExportObjCClass
private class ImagePickerDelegate(
    val onCaptured: (String) -> Unit,
    val onError: (Throwable) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        if (image != null) {
            val imageData = UIImageJPEGRepresentation(image, 0.8)
            val filePath =
                NSTemporaryDirectory() + "/captured_${NSDate().timeIntervalSince1970}.jpg"
            val fileUrl = NSURL.fileURLWithPath(filePath)
            val success = imageData?.writeToURL(fileUrl, true) ?: false

            if (success) {
                fileUrl.path?.let { onCaptured(it) }
            } else {
                onError(Exception("Failed to write image to temp file"))
            }

        } else {
            onError(Exception("No image found"))
        }
        picker.dismissViewControllerAnimated(true, null)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        onError(Exception("User Cancelled"))
        picker.dismissViewControllerAnimated(true, null)
    }
}