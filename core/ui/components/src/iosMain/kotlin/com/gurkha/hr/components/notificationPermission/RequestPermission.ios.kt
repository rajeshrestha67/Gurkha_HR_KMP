package com.gurkha.hr.components.notificationPermission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.requestAccessForMediaType
import platform.Photos.PHPhotoLibrary
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume

@Composable
actual fun RequestPermission(
    permissions: List<String>,
    onGranted: (String) -> Unit,
    onDenied: (String) -> Unit,
    onPermanentlyDenied: (String) -> Unit,
    onAllGranted: () -> Unit
) {
    LaunchedEffect(Unit) {
        for (permission in permissions) {
            val granted = when (permission) {
                POST_NOTIFICATIONS_PERMISSION -> requestNotificationPermission()
                CAMERA_PERMISSION -> requestCameraPermission()
                GALLERY_PERMISSION -> requestGalleryPermission()
                else -> false
            }

            if (granted) onGranted(permission)
            else onDenied(permission)
        }

        delay(100) // optional small delay
        onAllGranted()
    }
}
// -------- Helper suspend functions -------- //

private suspend fun requestNotificationPermission(): Boolean =
    suspendCancellableCoroutine { cont ->
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or
                    UNAuthorizationOptionSound or
                    UNAuthorizationOptionBadge
        ) { granted, _ ->
            cont.resume(granted)
        }
    }

private suspend fun requestCameraPermission(): Boolean =
    suspendCancellableCoroutine { cont ->
        AVCaptureDevice.requestAccessForMediaType(
            mediaType = AVMediaTypeVideo,
            completionHandler = { granted ->
                cont.resume(granted)
            }
        )
    }

private suspend fun requestGalleryPermission(): Boolean =
    suspendCancellableCoroutine { cont ->
        PHPhotoLibrary.requestAuthorization { status ->
            cont.resume(status == 3L) // 3 = authorized
        }
    }