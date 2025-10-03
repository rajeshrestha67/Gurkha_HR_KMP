package com.gurkha.hr.chat_room

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.gurkha.hr.chat_room.model.ChatRoomScreenAction
import com.gurkha.hr.chat_room.model.ChatRoomScreenState
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.chatTopBarColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
import com.gurkha.model.chat.ChatUserData
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatRoomScreen(
    onBackPressed: () -> Unit,
    chatUserJsonData: String
) {

    val viewModel = koinViewModel<ChatRoomViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = chatUserJsonData) {
        viewModel.onAction(action = ChatRoomScreenAction.UpdateChatData(json = chatUserJsonData))
    }
    
    ChatRoomScreenContent(
        onBackPressed = onBackPressed,
        state = state,
        onAction = viewModel::onAction
    )
}


@Composable
private fun ChatRoomScreenContent(
    onBackPressed: () -> Unit,
    state: ChatRoomScreenState,
    onAction: (ChatRoomScreenAction) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(),
        containerColor = MaterialTheme.colorScheme.chatTopBarColor,
        topBar = {
            state.chatUserData?.let {
                ChatTopBar(
                    onBackPressed = onBackPressed,
                    userData = it
                )
            }
        },
        bottomBar = {
            ChatBottomBar(
                message = state.message,
                onAction = onAction
            )
        }

    ) { contentPadding ->
        ChatRoomLazyColumn(
            modifier = Modifier.padding(paddingValues = contentPadding).fillMaxSize()
        )
    }

}

@Composable
private fun ChatBottomBar(
    message: String,
    onAction: (ChatRoomScreenAction) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = MaterialTheme.dimens.small3,
                end = MaterialTheme.dimens.small1,
                top = MaterialTheme.dimens.small2,
                bottom = MaterialTheme.dimens.small2
            ),
        horizontalArrangement = Arrangement.spacedBy(space = MaterialTheme.dimens.small2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ERPTextField(
            modifier = Modifier.weight(weight = 1f),
            text = message,
            hint = stringResource(resource = SharedRes.Strings.type_here),
            onValueChange = {
                onAction(ChatRoomScreenAction.SearchQueryChanged(message = it))
            },
            onErrorStateChange = {},
            imeAction = ImeAction.Send,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            keyboardActions = KeyboardActions(
                onSend = {
                    if (message.isNotEmpty()) {
                        onAction(ChatRoomScreenAction.Send)
                    }
                }
            )
        )

        IconButton(
            enabled = message.isNotEmpty(),
            onClick = {
                onAction(ChatRoomScreenAction.Send)
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "SEND",
                tint = MaterialTheme.colorScheme.primary
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar(
    onBackPressed: () -> Unit,
    userData: ChatUserData
) {
    TopAppBar(
        windowInsets = WindowInsets(),
        title = {

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimens.small2),
                horizontalArrangement = Arrangement.spacedBy(
                    space = MaterialTheme.dimens.small2
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(size = MaterialTheme.dimens.medium3)
                        .background(
                            color = Color(value = userData.backgroundColor),
                            shape = CircleShape
                        )
                        .border(
                            width = 0.5.dp,
                            color = MaterialTheme.colorScheme.borderColor,
                            shape = CircleShape
                        )
                        .aspectRatio(1f)
                ) {
                    userData.profileImageUrl?.let {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            model = userData.profileImageUrl,
                            contentDescription = userData.employeeName,
                            contentScale = ContentScale.Crop
                        )
                    } ?: Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = userData.nameInitials,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primaryTextColor
                        )
                    )
                }


                Column(
                    modifier = Modifier.weight(weight = 1f)
                ) {
                    Text(
                        text = userData.employeeName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.primaryTextColor
                        )
                    )
                    Text(
                        text = userData.branchName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.secondaryTextColor
                        )
                    )
                }
            }

        },
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

@Composable
private fun ChatRoomLazyColumn(
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        ),
        reverseLayout = true
    ) {

        items(100) {
            Text("text $it")
        }
    }

}