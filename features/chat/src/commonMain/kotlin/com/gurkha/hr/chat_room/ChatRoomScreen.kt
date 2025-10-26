package com.gurkha.hr.chat_room

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gurkha.hr.chat_room.components.triangle.Triangle
import com.gurkha.hr.chat_room.model.ChatMessage
import com.gurkha.hr.chat_room.model.ChatRoomScreenAction
import com.gurkha.hr.chat_room.model.ChatRoomScreenState
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.isKeyboardVisible
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.chatBackgroundColor
import com.gurkha.hr.res.theme.chatSecondaryTextColor
import com.gurkha.hr.res.theme.inComingBubbleColor
import com.gurkha.hr.res.theme.inComingTextColor
import com.gurkha.hr.res.theme.outGoingBubbleColor
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
import com.gurkha.model.chat.ChatUserData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
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
    val keyboardController = LocalSoftwareKeyboardController.current
    val isKeyboardOpen by isKeyboardVisible()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = MaterialTheme.colorScheme.chatBackgroundColor,
        topBar = {
            state.chatUserData?.let {
                ChatTopBar(
                    onBackPressed = {
                        if (isKeyboardOpen) {
                            keyboardController?.hide()
                        } else {
                            onBackPressed()
                        }
                    },
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
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            isKeyboardOpen = isKeyboardOpen,
            state = state
        )
    }
}


@Composable
private fun ChatBottomBar(
    message: String,
    onAction: (ChatRoomScreenAction) -> Unit
) {
    var typingJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = MaterialTheme.dimens.small3,
                end = MaterialTheme.dimens.small1,
                top = MaterialTheme.dimens.small1,
                bottom = MaterialTheme.dimens.small1
            ).navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ERPTextField(
            modifier = Modifier.weight(1f),
            text = message,
            hint = stringResource(resource = SharedRes.Strings.type_here),
            onValueChange = {
                onAction(ChatRoomScreenAction.MessageChanged(it))
                typingJob?.cancel()
                onAction(ChatRoomScreenAction.OnTyping(isTyping = it.isNotEmpty()))
                typingJob = coroutineScope.launch {
                    delay(600)
                    onAction(ChatRoomScreenAction.OnTyping(isTyping = false))
                }
            },
            onErrorStateChange = {},
            imeAction = ImeAction.Send,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            keyboardActions = KeyboardActions(onSend = {
                onAction(ChatRoomScreenAction.Send)
            })
        )

        IconButton(
            enabled = message.isNotEmpty(),
            onClick = { onAction(ChatRoomScreenAction.Send) }
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
    modifier: Modifier = Modifier,
    isKeyboardOpen: Boolean,
    state: ChatRoomScreenState
) {

    val listState = rememberLazyListState()
    LaunchedEffect(state.messages) {
        if (state.messages.isNotEmpty()) {
            val allMessages = state.messages.values.flatten()

            if (allMessages.isNotEmpty()) {
                // Scroll to the last item
                listState.animateScrollToItem(allMessages.lastIndex)
            }
        }
    }
    LaunchedEffect(isKeyboardOpen) {
        if (isKeyboardOpen && state.messages.isNotEmpty()) {
            delay(100)
            val allMessages = state.messages.values.flatten()

            if (allMessages.isNotEmpty()) {
                listState.animateScrollToItem(allMessages.lastIndex)
            }
        }
    }


    Column(
        modifier = modifier
    ) {
        AnimatedContent(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            targetState = state.isLoading
        ) { isLoading ->
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(
                        start = MaterialTheme.dimens.small3,
                        end = MaterialTheme.dimens.small3,
                        top = MaterialTheme.dimens.small2,
                        bottom = MaterialTheme.dimens.small2

                    )
                ) {
                    state.messages.keys.forEach { key ->

                        stickyHeader(key = key) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = key,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.secondaryTextColor
                                )
                            )
                        }
                        state.messages[key]?.let { messages ->
                            items(items = messages, key = { it.id }) { chatMessage ->
                                ChatMessageBox(
                                    modifier = Modifier.fillMaxWidth().animateItem(
                                        fadeInSpec = tween(300),
                                        fadeOutSpec = tween(500)
                                    ),
                                    chatMessage = chatMessage
                                )
                            }
                        }
                    }

                }
            }
        }

        AnimatedVisibility(
            visible = state.isTyping,
            modifier = Modifier.padding(start = MaterialTheme.dimens.small3)
        ) {
            TypingIndicator(
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun ChatMessageBox(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage
) {

    Box(
        modifier = modifier,
        contentAlignment = if (chatMessage.fromMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {

        Row(verticalAlignment = Alignment.Bottom) {
            if (!chatMessage.fromMe) {
                Spacer(Modifier.size(size = MaterialTheme.dimens.small1))

                Triangle(
                    true,
                    MaterialTheme.colorScheme.outGoingBubbleColor
                )

            }

            Column(
                modifier = Modifier.fillMaxWidth(0.6f),
                horizontalAlignment = if (chatMessage.fromMe) Alignment.End else Alignment.Start
            ) {
                Box(
                    Modifier.clip(
                        RoundedCornerShape(
                            MaterialTheme.dimens.small2,
                            MaterialTheme.dimens.small2,
                            if (!chatMessage.fromMe) MaterialTheme.dimens.small2 else 0.dp,
                            if (!chatMessage.fromMe) 0.dp else MaterialTheme.dimens.small2
                        )
                    )
                        .background(color = if (!chatMessage.fromMe) MaterialTheme.colorScheme.outGoingBubbleColor else MaterialTheme.colorScheme.inComingBubbleColor)
                        .padding(
                            horizontal = MaterialTheme.dimens.small2,
                            vertical = MaterialTheme.dimens.small1
                        )
                ) {
                    Column {
                        Text(
                            text = chatMessage.message,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.inComingTextColor
                            )
                        )
                        Spacer(Modifier.size(MaterialTheme.dimens.small1))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = chatMessage.time,
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.chatSecondaryTextColor
                                )
                            )
                        }
                    }
                }
                Box(Modifier.size(MaterialTheme.dimens.small2 + MaterialTheme.dimens.small1 / 2))
            }
            if (chatMessage.fromMe) {
                Triangle(
                    false,
                    MaterialTheme.colorScheme.inComingBubbleColor
                )
            }
        }
    }
}

@Composable
private fun TypingIndicator(
    modifier: Modifier = Modifier,
    dotColor: Color = MaterialTheme.colorScheme.chatSecondaryTextColor,
    dotSize: Dp = MaterialTheme.dimens.small2,
    dotSpacing: Dp = MaterialTheme.dimens.small1
) {
    val transition = rememberInfiniteTransition()
    val delays = remember { listOf(0, 300, 600) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Triangle(
            risingToTheRight = true,
            background = MaterialTheme.colorScheme.outGoingBubbleColor,
            bottomPadding = 0.dp
        )
        Row(
            modifier = Modifier.wrapContentWidth()
                .clip(
                    shape = RoundedCornerShape(
                        MaterialTheme.dimens.small2,
                        MaterialTheme.dimens.small2,
                        MaterialTheme.dimens.small2,
                        0.dp
                    )
                )
                .background(MaterialTheme.colorScheme.outGoingBubbleColor)
                .padding(MaterialTheme.dimens.small2),
            horizontalArrangement = Arrangement.spacedBy(dotSpacing)
        ) {
            delays.forEachIndexed { index, delay ->
                val scale by transition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, delayMillis = delay, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .graphicsLayer { scaleX = scale; scaleY = scale }
                        .background(dotColor, shape = CircleShape)
                )
            }
        }
    }

}