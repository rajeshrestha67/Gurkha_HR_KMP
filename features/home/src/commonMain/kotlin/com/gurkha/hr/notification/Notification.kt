package com.gurkha.hr.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ProfilePicture
import com.gurkha.hr.domain.notification.notificationData.model.NotificationData
import com.gurkha.hr.notification.model.NotificationState
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.imageBackgroundColor
import com.gurkha.hr.res.theme.primaryTextColor
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notification(
    onBackClicked: () -> Unit
) {
    val viewModel: NotificationViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text("Notification")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClicked,
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "go back "
                            )
                        }
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->
        NotificationScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            state = state
        )
    }
}

@Composable
fun NotificationScreenContent(
    modifier: Modifier = Modifier,
    state: NotificationState
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        state.notificationGrouped.forEach { (date, notification) ->
            item {
                Text(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small3),
                    text = date, style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }

            items(notification) { item ->
                NotificationBox(item = item)
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small2)
                )
            }
        }
    }
}


@Composable
fun NotificationBox(
    item: NotificationData
) {
    val color = if (item.seen) MaterialTheme.colorScheme.background
    else MaterialTheme.colorScheme.primary.copy(
        alpha = 0.1f
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {})
            .background(color = color)
            .padding(all = MaterialTheme.dimens.small3),
        contentAlignment = Alignment.BottomEnd
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfilePicture(
                imageUrl = item.imageUrl,
                employeeName = item.actionPerformerName,
                nameInitials = item.initials,
                size = MaterialTheme.dimens.large,
                shape = CircleShape,
                background = MaterialTheme.colorScheme.imageBackgroundColor,
                borderWidth = 0.5.dp,
                borderColor = MaterialTheme.colorScheme.borderColor,
                ratio = 1f
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
            ) {
                Text(
                    text = "${item.actionField} ${item.actionType}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.darkPrimaryTextColor
                    )
                )
                Text(
                    "Your ${item.actionField} request has been ${item.actionType}ed by : ${item.actionPerformerName}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )

            }

        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            text = item.actionTime, style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.primaryTextColor
            )
        )
    }
}