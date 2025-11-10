package com.gurkha.hr.chat_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.gurkha.hr.components.ProfilePicture
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.domain.chat.model.EmployChatItem
import com.gurkha.hr.model.ChatScreenAction
import com.gurkha.hr.model.ChatScreenState
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatListScreen(
    state: ChatScreenState,
    onAction: (ChatScreenAction) -> Unit,
    onBackPressed: () -> Unit
) {
//    val viewModel = koinViewModel<ChatListViewModel>()
//    val state by viewModel.state.collectAsStateWithLifecycle()
    ChatListScreenContent(
        onBackPressed = onBackPressed,
        state = state,
        onAction = onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatListScreenContent(
    onBackPressed: () -> Unit,
    state: ChatScreenState,
    onAction: (ChatScreenAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize().imePadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    AnimatedContent(state.showSearch) { showSearch ->
                        if (showSearch) {
                            ERPTextField(
                                text = state.query ?: "",
                                hint = stringResource(SharedRes.Strings.searchUsers),
                                onValueChange = {
                                    onAction(ChatScreenAction.SearchQueryChanged(it))
                                },
                                onErrorStateChange = {},
                                imeAction = ImeAction.Done,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                backgroundColor = MaterialTheme.colorScheme.background,
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        onAction(ChatScreenAction.ClearSearch)
                                    }
                                )
                            )

                        } else {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(SharedRes.Strings.chat),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = MaterialTheme.erpColors.primaryTextColor
                                )
                            )

                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (state.showSearch) {
                                onAction(ChatScreenAction.ClearSearch)
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
                                    onAction(ChatScreenAction.ClearSearch)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "clear"
                                )
                            }

                        } else {
                            IconButton(
                                enabled = !state.isEmployListLoading,
                                onClick = {
                                    onAction(ChatScreenAction.SearchClicked)
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
        PullToRefreshBox(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            isRefreshing = state.isEmployListRefreshing,
            onRefresh = { onAction(ChatScreenAction.OnEmployeeRefresh) },
            content = {
                ChatListLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    onAction = onAction
                )
            }
        )
    }
}

@Composable
private fun ChatListLazyColumn(
    modifier: Modifier = Modifier,
    state: ChatScreenState,
    onAction: (ChatScreenAction) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        )
    ) {
        if (state.isEmployListLoading) {
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
                        onAction(ChatScreenAction.ItemClick(chatItem))
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
private fun ChatListItem(chatItem: EmployChatItem, onClick: () -> Unit) {

    Row(
        modifier = Modifier.clickable(onClick = onClick).fillMaxWidth()
            .padding(vertical = MaterialTheme.dimens.small2),
        horizontalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.dimens.small2
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ProfilePicture(
            imageUrl = chatItem.profileImageUrl,
            employeeName = chatItem.employeeName,
            nameInitials = chatItem.nameInitials,
            size = MaterialTheme.dimens.medium3,
            shape = CircleShape,
            background = chatItem.backgroundColor,
            borderWidth = 0.5.dp,
            borderColor = MaterialTheme.colorScheme.outline,
            ratio = 1f
        )


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
                        .size(size = MaterialTheme.dimens.small3)
                        .aspectRatio(ratio = 1f)
                        .background(
                            color = if (chatItem.isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            shape = CircleShape
                        )

                )
                Text(
                    text = chatItem.employeeName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.outline
                    )
                )
            }

            Text(
                text = chatItem.branchName,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.erpColors.secondaryTextColor
                )
            )
        }
    }
}