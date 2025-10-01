package com.gurkha.hr.chat_room

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gurkha.hr.chat_room.model.ChatRoomScreenAction
import com.gurkha.hr.chat_room.model.ChatRoomScreenState
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatRoomScreen(
    onBackPressed: () -> Unit,
    chatUserJsonData: String
) {

    val viewModel = koinViewModel<ChatRoomViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(chatUserJsonData) {
        viewModel.onAction(ChatRoomScreenAction.UpdateChatData(chatUserJsonData))
    }
    ChatRoomScreenContent(
        onBackPressed = onBackPressed,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatRoomScreenContent(
    onBackPressed: () -> Unit,
    state: ChatRoomScreenState
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(),
                title = {
                    state.chatUserData?.let { item ->
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
                                    .size(MaterialTheme.dimens.medium3)
                                    .background(
                                        color = Color(item.backgroundColor),
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 0.5.dp,
                                        color = MaterialTheme.colorScheme.borderColor,
                                        shape = CircleShape
                                    )
                                    .aspectRatio(1f)
                            ) {
                                item.profileImageUrl?.let {
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                                        model = item.profileImageUrl,
                                        contentDescription = item.employeeName,
                                        contentScale = ContentScale.Crop
                                    )
                                } ?: Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = item.nameInitials,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = MaterialTheme.colorScheme.primaryTextColor
                                    )
                                )
                            }


                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = item.employeeName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.primaryTextColor
                                    )
                                )
                                Text(
                                    text = item.branchName,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = MaterialTheme.colorScheme.secondaryTextColor
                                    )
                                )
                            }
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
        },

        ) { contentPadding ->

    }

}