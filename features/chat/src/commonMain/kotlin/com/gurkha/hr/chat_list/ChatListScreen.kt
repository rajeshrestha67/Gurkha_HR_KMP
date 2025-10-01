package com.gurkha.hr.chat_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gurkha.hr.chat_list.model.ChatListScreenAction
import com.gurkha.hr.chat_list.model.ChatListScreenState
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.components.textField.EPRTextField
import com.gurkha.hr.domain.chat.model.ChatItem
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatListScreen(
    onBackPressed: () -> Unit,
    navigateToChat: (chatUserJsonData: String) -> Unit
) {
    val viewModel = koinViewModel<ChatListViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewModel.navigateChannel.collect { chatUserJsonData ->
            chatUserJsonData?.let {
                navigateToChat(it)
            }
        }
    }

    ChatListScreenContent(
        onBackPressed = onBackPressed,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatListScreenContent(
    onBackPressed: () -> Unit,
    state: ChatListScreenState,
    onAction: (ChatListScreenAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(),
                title = {
                    AnimatedContent(state.showSearch) { showSearch ->
                        if (showSearch) {
                            EPRTextField(
                                text = state.query ?: "",
                                hint = stringResource(SharedRes.Strings.searchUsers),
                                onValueChange = {
                                    onAction(ChatListScreenAction.SearchQueryChanged(it))
                                },
                                onErrorStateChange = {},
                                imeAction = ImeAction.Done,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                backgroundColor = MaterialTheme.colorScheme.background,
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        onAction(ChatListScreenAction.ClearSearch)
                                    }
                                )
                            )

                        } else {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(SharedRes.Strings.chat),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = MaterialTheme.colorScheme.primaryTextColor
                                )
                            )

                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (state.showSearch) {
                                onAction(ChatListScreenAction.ClearSearch)
                            } else {
                                onBackPressed()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    AnimatedContent(state.showSearch) { showSearch ->
                        if (showSearch) {
                            IconButton(
                                onClick = {
                                    onAction(ChatListScreenAction.ClearSearch)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "clear"
                                )
                            }

                        } else {
                            IconButton(
                                enabled = !state.isLoading,
                                onClick = {
                                    onAction(ChatListScreenAction.SearchClicked)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search"
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        ChatListLazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            state = state,
            onAction = onAction
        )
    }
}

@Composable
private fun ChatListLazyColumn(
    modifier: Modifier = Modifier,
    state: ChatListScreenState,
    onAction: (ChatListScreenAction) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        )
    ) {
        if (state.isLoading) {
            items(5) {
                ChatListItemLoading()
            }
        } else {
            items(
                items = state.chatList,
                key = { it.employeeId }
            ) { chatItem ->
                ChatListItem(
                    chatItem = chatItem,
                    onClick = {
                        onAction(ChatListScreenAction.ItemClick(chatItem))
                    }
                )
            }
        }
    }
}

@Composable
private fun ChatListItemLoading() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = MaterialTheme.dimens.small2),
        horizontalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.dimens.small2
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ShimmerView(
            modifier = Modifier
                .size(MaterialTheme.dimens.medium3)
                .aspectRatio(1f)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
        ) {
            ShimmerView(
                modifier = Modifier.fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .height(MaterialTheme.dimens.medium1)
            )
            ShimmerView(
                modifier = Modifier.fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .height(MaterialTheme.dimens.medium1)
            )
        }

    }
}

@Composable
private fun ChatListItem(chatItem: ChatItem, onClick: () -> Unit) {

    Row(
        modifier = Modifier.clickable(onClick = onClick).fillMaxWidth()
            .padding(vertical = MaterialTheme.dimens.small2),
        horizontalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.dimens.small2
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.dimens.medium3)
                .background(color = chatItem.backgroundColor, shape = CircleShape)
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.borderColor,
                    shape = CircleShape
                )
                .aspectRatio(1f)
        ) {
            chatItem.profileImageUrl?.let {
                AsyncImage(
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    model = chatItem.profileImageUrl,
                    contentDescription = chatItem.employeeName,
                    contentScale = ContentScale.Crop
                )
            } ?: Text(
                modifier = Modifier.align(Alignment.Center),
                text = chatItem.nameInitials,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )
        }


        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimens.small1,
                    alignment = Alignment.Start
                )
            ) {

                Box(
                    modifier = Modifier
                        .size(size = MaterialTheme.dimens.medium1)
                        .aspectRatio(ratio = 1f)
                        .background(color = MaterialTheme.colorScheme.error, shape = CircleShape)

                )
                Text(
                    text = chatItem.employeeName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }

            Text(
                text = chatItem.branchName,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.secondaryTextColor
                )
            )
        }
    }
}