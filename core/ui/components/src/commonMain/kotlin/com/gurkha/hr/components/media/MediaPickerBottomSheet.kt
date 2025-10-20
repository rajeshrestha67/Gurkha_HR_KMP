package com.gurkha.hr.components.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.gurkha.hr.components.noRippleClickable
import com.gurkha.hr.components.notificationPermission.CAMERA_PERMISSION
import com.gurkha.hr.components.notificationPermission.GALLERY_PERMISSION
import com.gurkha.hr.components.notificationPermission.rememberRequestPermission
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.model.network.DataError
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaSelectorModalBottomSheet(
    tag: String,
    onDismiss: () -> Unit,
    onImageReceived: (String) -> Unit
) {

    val openCamera = rememberCameraLauncher(
        onImageCaptured = { uri ->
            onImageReceived(uri)
            AppLogger.i(
                tag = tag,
                message = "Camera Image captured: $uri"
            )
        },
        onError = { e ->
            AppLogger.e(
                tag = tag,
                message = "Error on camera launcher",
                error = DataError.LocalError.Custom(e)
            )
        }
    )
    val openGallery = rememberGalleryLauncher(
        onImageSelected = { uri ->
            onImageReceived(uri)
            AppLogger.i(
                tag = tag,
                message = "Gallery Image received: $uri"
            )
        },
        onError = { e ->
            AppLogger.e(
                tag = tag,
                message = "Error on gallery launcher",
                error = DataError.LocalError.Custom(e)
            )
        }
    )

    rememberRequestPermission(
        permissions = listOf(
            CAMERA_PERMISSION,
            GALLERY_PERMISSION
        ),
        onGranted = { permission ->
            AppLogger.i(
                tag = tag,
                message = "Permission granted: $permission"
            )
        },
        onDenied = { permission ->
            AppLogger.i(
                tag = tag,
                message = "Permission denied: $permission"
            )
        },
        onPermanentlyDenied = { permission ->
            AppLogger.i(
                tag = tag,
                message = "Permission denied permanent: $permission"
            )
        },
        onAllGranted = {
            AppLogger.i(
                tag = tag,
                message = "All Permission granted"
            )
        }
    )
    val sheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )

    ModalBottomSheet(
        sheetState = sheet,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(MaterialTheme.dimens.small3),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth().noRippleClickable(onClick = openCamera),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
            ) {

                Icon(
                    modifier = Modifier.size(MaterialTheme.dimens.medium2),
                    imageVector = Icons.Filled.Camera,
                    contentDescription = "Camera"
                )

                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = stringResource(SharedRes.Strings.camera),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().noRippleClickable(onClick = openGallery),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
            ) {

                Icon(
                    modifier = Modifier.size(MaterialTheme.dimens.medium2),
                    imageVector = Icons.Filled.BrowseGallery,
                    contentDescription = "gallery"
                )

                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = stringResource(SharedRes.Strings.gallery),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}