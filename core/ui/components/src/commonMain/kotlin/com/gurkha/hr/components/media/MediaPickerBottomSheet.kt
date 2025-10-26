package com.gurkha.hr.components.media

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.permissions.CAMERA_PERMISSION
import com.gurkha.hr.components.permissions.GALLERY_PERMISSION
import com.gurkha.hr.components.permissions.navigateToSettings
import com.gurkha.hr.components.permissions.rememberRequestPermission
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.model.network.DataError


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaSelectorModalBottomSheet(
    tag: String,
    onDismiss: () -> Unit,
    onImageReceived: (String) -> Unit
) {
    var galleryImages by remember { mutableStateOf<List<String>>(emptyList()) }
    val loadGallery = rememberGalleryLoader(
        onLoaded = {
            println("images $it")
            galleryImages = it
        }, onError = {
            AppLogger.e(
                tag = tag,
                message = "Error on gallery launcher",
                error = DataError.LocalError.Custom(it)
            )
        }
    )

    var isCameraPermissionPermanentDenied by remember { mutableStateOf(false) }
    var isGalleryPermissionPermanentDenied by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        loadGallery()
    }

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

    val onPermission = rememberRequestPermission(
        permissions = listOf(
            CAMERA_PERMISSION
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

            when (permission) {
                CAMERA_PERMISSION -> isCameraPermissionPermanentDenied = true
                GALLERY_PERMISSION -> isGalleryPermissionPermanentDenied = true
            }
        },
        onAllGranted = {
            AppLogger.i(
                tag = tag,
                message = "All Permission granted"
            )
        }
    )

    LaunchedEffect(Unit) {
        onPermission()
    }

    val sheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val navigateToSettings = navigateToSettings()
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxSize(),
        sheetState = sheet,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.small3),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
            ) {
                // Camera preview item
                item {
                    Box(
                        modifier = Modifier
                            .aspectRatio(9f / 16f)
                            .background(Color.DarkGray, MaterialTheme.shapes.extraSmall)
                            .clickable {
                                if (isCameraPermissionPermanentDenied) {
                                    onDismiss()
                                    navigateToSettings()
                                } else {
                                    openCamera()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(
                                space = MaterialTheme.dimens.small2,
                                alignment = Alignment.CenterVertically
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Camera,
                                contentDescription = "camera"
                            )
                            if (isCameraPermissionPermanentDenied) {
                                TextButton(
                                    onClick = {
                                        onDismiss()
                                        navigateToSettings()
                                    }
                                ) {
                                    Text(
                                        text = "Camera permission denied.",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }

                            }
                        }

                    }
                }


                // Gallery items
                items(galleryImages.size) { index ->
                    val uri = galleryImages[index]
                    println("uri $uri")
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.extraSmall)
                            .aspectRatio(9f / 16f)
                            .clickable { onImageReceived(uri) }
                    )
                }

            }
            if (isGalleryPermissionPermanentDenied) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = {
                            onDismiss()
                            navigateToSettings()
                        }
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Gallery permission denied.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primaryTextColor
                            )
                        )
                    }
                }
            }
        }
    }
}