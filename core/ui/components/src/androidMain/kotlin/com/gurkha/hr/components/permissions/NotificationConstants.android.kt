package com.gurkha.hr.components.permissions

import android.os.Build

actual val POST_NOTIFICATIONS_PERMISSION: String
    get() = "android.permission.POST_NOTIFICATIONS"
actual val CAMERA_PERMISSION: String
    get() = android.Manifest.permission.CAMERA
actual val GALLERY_PERMISSION: String
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        android.Manifest.permission.READ_MEDIA_IMAGES
    else
        android.Manifest.permission.READ_EXTERNAL_STORAGE
actual val GALLERY_PERMISSION_LIMITED: String
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
    } else {
        ""
    }