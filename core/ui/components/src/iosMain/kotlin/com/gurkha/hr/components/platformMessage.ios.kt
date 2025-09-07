package com.gurkha.hr.components

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.NSEC_PER_SEC
import platform.darwin.dispatch_after
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_time

actual class PlatformMessage() {
    @OptIn(ExperimentalForeignApi::class)
    actual fun showToast(message: String) {
//        println("message")
        val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootVC?.let {
            val alert = UIAlertController.alertControllerWithTitle(
                title = null,
                message = message,
                preferredStyle = UIAlertControllerStyleAlert
            )
            it.presentViewController(alert, animated = true, completion = null)

            // dismiss after 2 seconds like a Toast
            val delay = dispatch_time(DISPATCH_TIME_NOW, (2 * NSEC_PER_SEC.toDouble()).toLong())
            dispatch_after(delay, dispatch_get_main_queue()) {
                alert.dismissViewControllerAnimated(true, completion = null)
            }
        }
    }
}