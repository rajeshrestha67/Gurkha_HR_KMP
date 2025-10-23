package com.gurkha.hr.components

import androidx.compose.runtime.Composable
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun ChangeStatusBarColor(
    darkIcons: Boolean
) {
    dispatch_async(dispatch_get_main_queue()) {
        val style = if (darkIcons) {
            // Dark content (text/icons) for a light-colored status bar
            UIStatusBarStyleDarkContent
        } else {
            // Light content (text/icons) for a dark-colored status bar
            UIStatusBarStyleLightContent
        }

        // Note: Changing the actual *color* of the status bar in iOS
        // is typically achieved by making the status bar transparent
        // (via .ignoresSafeArea() in SwiftUI/UIKit) and letting
        // the Compose background color fill the area.

        UIApplication.sharedApplication.setStatusBarStyle(style, animated = true)
    }
}