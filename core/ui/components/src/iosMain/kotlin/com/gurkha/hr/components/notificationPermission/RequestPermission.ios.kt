package com.gurkha.hr.components.notificationPermission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume

@Composable
actual fun rememberRequestPermission(
    permissions: List<String>,
    onGranted: (String) -> Unit,
    onDenied: (String) -> Unit,
    onPermanentlyDenied: (String) -> Unit,
    onAllGranted: () -> Unit
): () -> Unit {
    val coroutineScope = rememberCoroutineScope()

    return remember {
        {
            coroutineScope.launch {
                val deniedPermissions = mutableListOf<String>()

                for (permission in permissions) {
                    val (granted, permanentlyDenied) = when (permission) {
                        POST_NOTIFICATIONS_PERMISSION -> requestNotificationPermission()
                        CAMERA_PERMISSION -> requestCameraPermission()
                        GALLERY_PERMISSION -> requestGalleryPermission()
                        else -> Pair(true, false)
                    }

                    when {
                        granted -> onGranted(permission)
                        permanentlyDenied -> {
                            onPermanentlyDenied(permission)
                            deniedPermissions.add(permission)
                        }

                        else -> {
                            // iOS has no true temporary denial, but we keep this for consistency
                            onDenied(permission)
                            deniedPermissions.add(permission)
                        }
                    }
                }

                if (deniedPermissions.isEmpty()) {
                    onAllGranted()
                }
            }
        }
    }
}

private suspend fun requestNotificationPermission(): Pair<Boolean, Boolean> =
    suspendCancellableCoroutine { cont ->
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.getNotificationSettingsWithCompletionHandler { settings ->
            when (settings?.authorizationStatus) {
                UNAuthorizationStatusAuthorized -> cont.resume(Pair(true, false))
                UNAuthorizationStatusDenied -> cont.resume(Pair(false, true))
                else -> {
                    // Ask user
                    center.requestAuthorizationWithOptions(
                        UNAuthorizationOptionAlert or
                                UNAuthorizationOptionSound or
                                UNAuthorizationOptionBadge
                    ) { granted, _ ->
                        cont.resume(Pair(granted, !granted))
                    }
                }
            }
        }
    }

private suspend fun requestCameraPermission(): Pair<Boolean, Boolean> =
    suspendCancellableCoroutine { cont ->
        val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
        when (status) {
            AVAuthorizationStatusAuthorized -> cont.resume(Pair(true, false))
            AVAuthorizationStatusDenied -> cont.resume(Pair(false, true))
            AVAuthorizationStatusRestricted -> cont.resume(Pair(false, true))
            else -> {
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                    cont.resume(Pair(granted, !granted))
                }
            }
        }
    }

private suspend fun requestGalleryPermission(): Pair<Boolean, Boolean> =
    suspendCancellableCoroutine { cont ->
        val status = PHPhotoLibrary.authorizationStatus()
        when (status) {
            PHAuthorizationStatusAuthorized -> cont.resume(Pair(true, false))
            PHAuthorizationStatusLimited -> cont.resume(Pair(true, false))
            PHAuthorizationStatusDenied -> cont.resume(Pair(false, true))
            PHAuthorizationStatusRestricted -> cont.resume(Pair(false, true))
            else -> {
                PHPhotoLibrary.requestAuthorization { newStatus ->
                    cont.resume(
                        when (newStatus) {
                            PHAuthorizationStatusAuthorized,
                            PHAuthorizationStatusLimited -> Pair(true, false)

                            else -> Pair(false, true)
                        }
                    )
                }
            }
        }
    }