package com.gurkha.hr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.res.theme.primaryTextColor

@Composable
fun ProfilePicture(
    imageUrl: String?,
    employeeName: String,
    nameInitials: String,
    size: Dp,
    shape: Shape,
    background: Color,
    borderWidth: Dp,
    borderColor: Color,
    ratio: Float,
    onClick: () -> Unit = {}
) {

    val context = LocalPlatformContext.current
    val imageRequest = remember(context, imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build()
    }
    val painter = rememberAsyncImagePainter(
        model = imageRequest
    )

    val currentState by painter.state.collectAsStateWithLifecycle()
    val imageState = remember(currentState) {
        when (currentState) {
            is AsyncImagePainter.State.Loading -> ImageState.Loading
            is AsyncImagePainter.State.Success -> ImageState.Success
            is AsyncImagePainter.State.Error -> ImageState.Error
            else -> ImageState.Loading
        }
    }
    Box(
        Modifier.size(size = size)
            .noRippleClickable(onClick)
    ) {

        when (imageState) {
            is ImageState.Loading -> {
                ShimmerView(
                    modifier = Modifier.size(size = size).clip(shape)
                )
            }

            is ImageState.Error -> {
                ProfilePictureInitialsText(
                    nameInitials = nameInitials,
                    size = size,
                    background = background,
                    shape = shape,
                    borderWidth = borderWidth,
                    borderColor = borderColor
                )
            }

            is ImageState.Success -> {
                AsyncImage(
                    modifier = Modifier
                        .size(size = size)
                        .border(
                            width = borderWidth,
                            color = borderColor,
                            shape = shape
                        )
                        .aspectRatio(ratio = ratio)
                        .clip(shape = shape),
                    model = imageRequest,
                    contentDescription = employeeName,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
//    AnimatedContent(
//        modifier = Modifier.size(size = size)
//            .noRippleClickable(onClick),
//        targetState = imageUrl,
//        label = "ProfilePicture"
//    ) { state ->


//        when (state) {
//            is ImageState.Loading -> {
//                ShimmerView(
//                    modifier = Modifier.size(size = size).clip(shape)
//                )
//            }
//
//            is ImageState.Error -> {
//                ProfilePictureInitialsText(
//                    nameInitials = nameInitials,
//                    size = size,
//                    background = background,
//                    shape = shape,
//                    borderWidth = borderWidth,
//                    borderColor = borderColor
//                )
//            }
//
//            is ImageState.Success -> {
//                AsyncImage(
//                    modifier = Modifier
//                        .size(size = size)
//                        .border(
//                            width = borderWidth,
//                            color = borderColor,
//                            shape = shape
//                        )
//                        .aspectRatio(ratio = ratio)
//                        .clip(shape = shape),
//                    model = imageRequest,
//                    contentDescription = employeeName,
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
    //}
}

@Composable
private fun ProfilePictureInitialsText(
    nameInitials: String,
    size: Dp,
    background: Color,
    shape: Shape,
    borderWidth: Dp,
    borderColor: Color
) {
    Box(
        modifier = Modifier
            .size(size = size)
            .clip(shape = shape)
            .background(color = background)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = nameInitials,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primaryTextColor
            )
        )
    }
}

sealed interface ImageState {
    data object Loading : ImageState
    data object Error : ImageState
    data object Success : ImageState
}