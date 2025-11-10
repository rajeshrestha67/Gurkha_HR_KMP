package com.gurkha.hr.viewAllScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ProfilePicture
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.domain.upComingEvent.model.EventData
import com.gurkha.hr.model.viewAll.ViewAllScreenAction
import com.gurkha.hr.model.viewAll.ViewAllScreenState
import com.gurkha.hr.res.SharedRes
import com.gurkha.model.upComingBirthday.ui.ViewAllUi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllScreen(
    title: String?,
    json: String?,
    onBackClicked: () -> Unit
) {
    val viewModel: ViewAllScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


    LaunchedEffect(json) {
        json?.let {
            viewModel.onAction(ViewAllScreenAction.OnJsonUpdate(json))
        }
    }
    LaunchedEffect(title) {
        title?.let {
            viewModel.onAction(ViewAllScreenAction.OnTitleUpdate(title = title))

        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    state.title?.let { Text(text = it) }
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
        }
    ) { contentPadding ->
        ViewAllScreenContent(
            Modifier.fillMaxSize().padding(contentPadding),
            state = state
        )
    }
}


@Composable
fun ViewAllScreenContent(
    modifier: Modifier = Modifier,
    state: ViewAllScreenState
) {
    val eventTitle = stringResource(SharedRes.Strings.upcoming_events)

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            vertical = MaterialTheme.dimens.small2,
            horizontal = MaterialTheme.dimens.small3
        ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        state.data?.let {
            items(state.data) { item ->
                if (state.title == eventTitle){
                    EventCard(item = item)
                }else{
                    ResultBox(item = item)
                }
            }
        }
    }
}


@Composable
fun ResultBox(
    item: ViewAllUi
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small),
        tonalElevation = 4.dp,

        ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.small)
                .padding(all = MaterialTheme.dimens.small2),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
        ) {
            ProfilePicture(
                imageUrl = item.imageUrl,
                employeeName = item.fullName ?: "",
                nameInitials = item.initials ?: "",
                size = MaterialTheme.dimens.large,
                shape = CircleShape,
                background = MaterialTheme.erpColors.imageBackgroundColor,
                borderWidth = 0.5.dp,
                borderColor = MaterialTheme.colorScheme.outline,
                ratio = 1f
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
            ) {

                item.fullName?.let {
                    Text(
                        text = it, style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.erpColors.primaryTextColor
                        )
                    )
                }

                Text(
                    text = "${stringResource(SharedRes.Strings.designation)} : ${item.designationName}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.erpColors.primaryTextColor
                    )
                )

                Text(
                    "${stringResource(SharedRes.Strings.branch)} : ${item.branchName}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.erpColors.primaryTextColor
                    )
                )
                Text(
                    "${stringResource(SharedRes.Strings.date)} : ${item.date}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.erpColors.primaryTextColor
                    )
                )

            }
        }
    }
}


@Composable
fun EventCard(
    item: ViewAllUi
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(
                MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.1f
                )
            )
            .padding(
                vertical = MaterialTheme.dimens.small2,
                horizontal = MaterialTheme.dimens.small3
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)

    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(SharedRes.Strings.date),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.erpColors.darkPrimaryTextColor
                )
            )
            Text(
                text = "${item.fromDate} - ${item.toDate}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.erpColors.primaryTextColor
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = MaterialTheme.dimens.small1)
                .height(MaterialTheme.dimens.extraSmall)
        )

        Text(
            text = item.title ?: "",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.erpColors.darkPrimaryTextColor
            )
        )

        Text(
            text = item.description ?: "",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.erpColors.primaryTextColor
            )
        )
    }
}



