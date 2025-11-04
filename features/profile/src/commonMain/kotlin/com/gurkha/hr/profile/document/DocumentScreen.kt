package com.gurkha.hr.profile.document

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gurkha.hr.components.PlatformMessage
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.components.media.MediaSelectorModalBottomSheet
import com.gurkha.hr.profile.model.document_screen.DocumentList
import com.gurkha.hr.profile.model.document_screen.DocumentScreenAction
import com.gurkha.hr.profile.model.document_screen.DocumentScreenState
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

const val TAG = "DocumentScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScreen(
    onBackPressed: () -> Unit,
) {
    val viewModel: DocumentScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val platformMessage: PlatformMessage = koinInject()

    //show the success toast
    LaunchedEffect(Unit) {
        viewModel.successChannel.collect {
            platformMessage.showToast(it)
        }
    }

    //show the error toast
    LaunchedEffect(Unit) {
        viewModel.errorChannel.collect {
            platformMessage.showToast(it)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(stringResource(SharedRes.Strings.document)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (state.isLongImagePressed) {
                Dialog(
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    ),
                    onDismissRequest = {
                        viewModel.onAction(DocumentScreenAction.OnLongPressedDismiss)
                    },
                    content = {
                        AsyncImage(
                            modifier = Modifier.wrapContentSize(),
                            contentScale = ContentScale.FillWidth,
                            model = state.longPressedImage,
                            contentDescription = "long pressed image"
                        )
                    },
                )
            }
            PullToRefreshBox(
                modifier = Modifier.fillMaxSize(),
                isRefreshing = false,
                onRefresh = {},
                content = {
                    DocumentScreenContainer(
                        state = state,
                        action = viewModel::onAction
                    )
                }
            )
        }

    }
}

@Composable
fun DocumentScreenContainer(
    state: DocumentScreenState,
    action: (DocumentScreenAction) -> Unit
) {
    var showMediaBottomSheet by remember { mutableStateOf(false) }


    if (showMediaBottomSheet) {
        MediaSelectorModalBottomSheet(
            tag = TAG,
            onDismiss = {
                showMediaBottomSheet = false
            },
            onImageReceived = { uri ->
                showMediaBottomSheet = false
                action(DocumentScreenAction.OnReceivedDocumentUri(uri = uri))
            }
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        )
    ) {
        items(
            state.documentList,
            key = { it.toString() },
        ) { item ->
            DocumentItemRow(
                state = state,
                item = item,
                action = action,
                onOpenCamera = {
                    showMediaBottomSheet = true
                }
            )
        }
    }
}

@Composable
fun DocumentItemRow(
    item: DocumentList,
    state: DocumentScreenState,
    onOpenCamera: () -> Unit,
    action: (DocumentScreenAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .clip(
                shape = MaterialTheme.shapes.medium
            ).combinedClickable(
                onClick = {
                    onOpenCamera()
                    action(DocumentScreenAction.OnSelectedDocument(item.imageType.key))
                },
                onLongClick = {
                    item.uploadedImage?.let {
                        action(DocumentScreenAction.OnLongPressed(item.uploadedImage))
                    }
                })
    ) {
        item.uploadedImage?.let {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
                contentAlignment = Alignment.BottomCenter
            ) {
                AsyncImage(
                    contentScale = ContentScale.Crop,
                    model = it,
                    contentDescription = "uploaded Image",
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.3f)
                            )
                        )
                    ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.dimens.small1),
                        textAlign = TextAlign.Center,
                        text = stringResource(item.title),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.erpColors.veryLightGray
                        )
                    )
                }
            }

        } ?: Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.small3),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(item.title),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.erpColors.primaryTextColor
                ),
                textAlign = TextAlign.Center
            )
            Icon(
                imageVector = Icons.Filled.CloudUpload,
                contentDescription = "upload",
                modifier = Modifier.size(MaterialTheme.dimens.medium1),
                tint = MaterialTheme.erpColors.secondaryTextColor
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
            Text(
                text = stringResource(item.uploadText),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.erpColors.secondaryTextColor,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
        }

    }
}

