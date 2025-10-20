package com.gurkha.hr.components.notificationPermission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

@Composable
actual fun rememberRequestPermission(
    permissions: List<String>,
    onGranted: (String) -> Unit,
    onDenied: (String) -> Unit,
    onPermanentlyDenied: (String) -> Unit,
    onAllGranted: () -> Unit
): () -> Unit {
    val context = LocalContext.current
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val deniedPermissions = mutableListOf<String>()
        results.forEach { (permission, granted) ->
            if (granted) {
                onGranted(permission)
            } else {
                val shouldShowRationale =
                    activity?.shouldShowRequestPermissionRationale(permission) ?: false
                if (shouldShowRationale) onDenied(permission)
                else onPermanentlyDenied(permission)
                deniedPermissions.add(permission)
            }
        }

        if (deniedPermissions.isEmpty()) {
            onAllGranted()
        }
    }

    return remember {
        {
            coroutineScope.launch {
                val notGranted = permissions.filter {
                    ContextCompat.checkSelfPermission(context, it) !=
                            PackageManager.PERMISSION_GRANTED
                }

                if (notGranted.isEmpty()) {
                    onAllGranted()
                } else {
                    permissionLauncher.launch(notGranted.toTypedArray())
                }
            }
        }
    }
}