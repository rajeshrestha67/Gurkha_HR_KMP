package com.gurkha.hr.components.media

import platform.Photos.PHAccessLevelReadWrite
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHPhotoLibrary

actual fun checkGalleryFullAccess(): Boolean {
    val status = PHPhotoLibrary.authorizationStatusForAccessLevel(PHAccessLevelReadWrite)
    return status == PHAuthorizationStatusAuthorized
}

actual fun checkGalleryLimitAccess(): Boolean {
    val status = PHPhotoLibrary.authorizationStatusForAccessLevel(PHAccessLevelReadWrite)
    return status == PHAuthorizationStatusLimited
}