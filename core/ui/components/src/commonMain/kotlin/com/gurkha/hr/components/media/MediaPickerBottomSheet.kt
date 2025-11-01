package com.gurkha.hr.components.media

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import coil3.compose.AsyncImage
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.PlatformMessage
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.permissions.CAMERA_PERMISSION
import com.gurkha.hr.components.permissions.GALLERY_PERMISSION
import com.gurkha.hr.components.permissions.GALLERY_PERMISSION_LIMITED
import com.gurkha.hr.components.permissions.navigateToSettings
import com.gurkha.hr.components.permissions.rememberRequestPermission
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.res.SharedRes
import com.gurkha.model.network.DataError
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaSelectorModalBottomSheet(
    tag: String,
    onDismiss: () -> Unit,
    onImageReceived: (String) -> Unit
) {
    val platformMessage: PlatformMessage = koinInject()

    var galleryImages by remember { mutableStateOf<List<String>>(emptyList()) }
    var isCameraPermissionPermanentDenied by remember { mutableStateOf(false) }
    var isGalleryPermissionPermanentDenied by remember { mutableStateOf(false) }
    var isPermissionChecked by remember { mutableStateOf(false) }

    var galleryFullAccess by remember { mutableStateOf(true) }

    val loadGallery = rememberGalleryLoader(
        onLoaded = { newImages ->
            galleryImages = (galleryImages + newImages).distinct()
        },
        onError = {
            AppLogger.e(
                tag = "GalleryLoader",
                message = "Error on gallery launcher",
                error = DataError.LocalError.Custom(it)
            )
            platformMessage.showToast("Error on gallery launcher")
        }
    )


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
                message = "Camera Image captured: $uri"
            )
        },
        onError = { e ->
            AppLogger.e(
                tag = tag,
                message = "Error on camera launcher",
                error = DataError.LocalError.Custom(e)
            )
            e.message?.let { message ->
                platformMessage.showToast(message)
            }
        }
    )

    val onCameraPermission = rememberRequestPermission(
        permissions = listOf(
            CAMERA_PERMISSION
        ).filter { it.isNotEmpty() },
        onGranted = { permission ->
            AppLogger.i(
                tag = tag,
                message = "Permission granted: $permission"
            )
            openCamera()
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
                CAMERA_PERMISSION -> {
                    isCameraPermissionPermanentDenied = true
                }
            }
        },
        onAllGranted = {
            AppLogger.i(
                tag = tag,
                message = "All Permission granted"
            )
        }
    )
    val onGalleryPermission = rememberRequestPermission(
        permissions = listOf(
            GALLERY_PERMISSION,
            GALLERY_PERMISSION_LIMITED
        ).filter { it.isNotEmpty() },
        onGranted = { permission ->
            AppLogger.i(
                tag = tag,
                message = "Permission granted: $permission"
            )
            isPermissionChecked = true
            if (permission == GALLERY_PERMISSION) {
                galleryFullAccess = true
                loadGallery()
            }
        },
        onDenied = { permission ->
            AppLogger.i(
                tag = tag,
                message = "Permission denied: $permission"
            )
            isPermissionChecked = true
            if (permission == GALLERY_PERMISSION) {
                galleryFullAccess = false
            }
        },
        onPermanentlyDenied = { permission ->
            AppLogger.i(
                tag = tag,
                message = "Permission denied permanent: $permission"
            )

            isPermissionChecked = true
            when (permission) {
                GALLERY_PERMISSION_LIMITED -> {
                    isGalleryPermissionPermanentDenied = true
                }
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
        val fullAccess = checkGalleryFullAccess()
        val limitedAccess = checkGalleryLimitAccess()

        if (!fullAccess && !limitedAccess) {
            onGalleryPermission()
        } else {
            galleryFullAccess = fullAccess
        }
    }
    LifecycleResumeEffect(Unit) {
        loadGallery()
        if (isPermissionChecked) {
            galleryFullAccess = checkGalleryFullAccess()
        }
        onPauseOrDispose {

        }
    }

    val sheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { state ->
            state != SheetValue.Hidden
        }
    )

    val navigateToSettings = navigateToSettings()
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        sheetState = sheet,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = MaterialTheme.dimens.small2),
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimens.small2,
                    alignment = Alignment.Start
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close"
                    )
                }
                Text(
                    text = stringResource(SharedRes.Strings.photos),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

        }
    ) {

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.small3),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                item(
                    key = "options",
                    span = {
                        GridItemSpan(maxLineSpan)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1.5f),
                            shape = MaterialTheme.shapes.medium,
                            tonalElevation = 4.dp
                        ) {

                            Column(
                                modifier = Modifier
                                    .clickable {
                                        if (isCameraPermissionPermanentDenied) {
                                            onDismiss()
                                            navigateToSettings()
                                        } else {
                                            onCameraPermission()
                                        }
                                    },
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

                                Text(
                                    text = stringResource(if (isCameraPermissionPermanentDenied) SharedRes.Strings.cameraPermissionDenied else SharedRes.Strings.camera),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1.5f)
                                .clickable {
                                    openGallery()
                                },
                            shape = MaterialTheme.shapes.medium,
                            tonalElevation = 4.dp
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(
                                    space = MaterialTheme.dimens.small2,
                                    alignment = Alignment.CenterVertically
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.BrowseGallery,
                                    contentDescription = "gallery"
                                )

                                Text(
                                    text = stringResource(SharedRes.Strings.gallery),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    }
                }
                if (!galleryFullAccess) {
                    item(
                        key = "settings",
                        span = {
                            GridItemSpan(maxLineSpan)
                        }
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.dimens.small2),
                            shape = MaterialTheme.shapes.medium,
                            tonalElevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(MaterialTheme.dimens.small2),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                            ) {
                                Text(
                                    text = stringResource(SharedRes.Strings.toAccessAllPhotos),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                                ) {
                                    ERPButton(
                                        modifier = Modifier.weight(1f),
                                        text = stringResource(SharedRes.Strings.addPhotos),
                                        onClick = if (isGalleryPermissionPermanentDenied) navigateToSettings else onGalleryPermission
                                    )
                                    ERPButton(
                                        modifier = Modifier.weight(1f),
                                        text = stringResource(SharedRes.Strings.go_to_setting),
                                        backgroundColor = MaterialTheme.colorScheme.error,
                                        onClick = navigateToSettings
                                    )
                                }
                            }


                        }
                    }
                }


                items(galleryImages.size, key = { it }) { index ->
                    val uri = galleryImages[index]
                    Surface(
                        modifier = Modifier
                            .aspectRatio(9f / 16f)
                            .clickable { onImageReceived(uri) },
                        shape = MaterialTheme.shapes.extraSmall,
                        tonalElevation = 4.dp
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}