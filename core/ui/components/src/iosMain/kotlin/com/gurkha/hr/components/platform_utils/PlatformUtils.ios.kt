package com.gurkha.hr.components.platform_utils

import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSDictionary
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.dictionary
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIPasteboard

actual class PlatformUtils {
    actual fun copyToClipboard(text: String) {
        UIPasteboard.generalPasteboard().string = text
    }

    @OptIn(BetaInteropApi::class)
    actual fun shareText(text: String, title: String?) {
        val items = listOf(NSString.create(string = text))

        // Create the activity view controller
        val activityViewController = UIActivityViewController(
            activityItems = items,
            applicationActivities = null
        )

        // Get the current root view controller to present the share sheet
        val currentViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        currentViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }

    actual fun callPhoneNumber(phoneNumber: String) {
        // 1. Construct the 'tel:' URL for iOS
        val urlString = "tel:$phoneNumber"
        val url = NSURL(string = urlString)
        

        val application = UIApplication.sharedApplication()

        // 2. Use the modern, non-deprecated open(_:options:completionHandler:) API
        // This function opens the URL and takes an options dictionary and a completion block.
        application.openURL(
            url = url,
            options = NSDictionary.dictionary(), // Pass an empty dictionary for options
            completionHandler = { success ->
                if (success) {
                    println("Successfully opened URL to call: $phoneNumber")
                } else {
                    // This block will be executed if the device cannot open the URL
                    println("Phone calls not supported on this device or action failed.")
                }
            }
        )
    }
}
