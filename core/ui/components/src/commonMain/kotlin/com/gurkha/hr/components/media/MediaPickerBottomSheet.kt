package com.gurkha.hr.components.media

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import coil3.compose.AsyncImage
import com.gurkha.hr.components.notificationPermission.CAMERA_PERMISSION
import com.gurkha.hr.components.notificationPermission.GALLERY_PERMISSION
import com.gurkha.hr.components.notificationPermission.rememberRequestPermission
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.res.theme.dimens
import com.gurkha.model.network.DataError


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaSelectorModalBottomSheet(
    tag: String,
    onDismiss: () -> Unit,
    onImageReceived: (String) -> Unit
) {
    var galleryImages by remember { mutableStateOf<List<String>>(emptyList()) }
    val loadGallery = rememberGalleryLoader(onLoaded = { galleryImages = it }, onError = {
        AppLogger.e(
            tag = tag,
            message = "Error on gallery launcher",
            error = DataError.LocalError.Custom(it)
        )
    })

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

    LaunchedEffect(Unit) {
        onPermission()
    }

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxSize(),
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f).fillMaxWidth(),
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
                        .clickable { openCamera() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Camera,
                        contentDescription = "camera"
                    )
                }
            }

            // Gallery items
            items(galleryImages.size) { index ->
                val uri = galleryImages[index]
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

    }
}