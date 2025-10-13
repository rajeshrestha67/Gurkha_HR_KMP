package com.gurkha.hr.note

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.domain.note.model.NoteData
import com.gurkha.hr.model.note.NoteState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    onGoToAddNotesScreen: () -> Unit
) {
    val viewModel: NoteViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(text = stringResource(SharedRes.Strings.notes))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onGoToAddNotesScreen,
                content = {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            )
        }
    ) { contentPadding ->
        NoteScreenContent(
            modifier = Modifier.padding(contentPadding),
            state = state
        )
    }
}

@Composable
fun NoteScreenContent(
    modifier: Modifier = Modifier,
    state: NoteState
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = MaterialTheme.dimens.bottomBar,
            start = MaterialTheme.dimens.small3,
            end = MaterialTheme.dimens.small3
        ),
    ) {
        diffResult(
            state = state
        )
    }
}


fun LazyListScope.diffResult(
    state: NoteState
) {
    items(state.noteItem){item->
        ResultBox(
            item = item
        )
    }
}


@Composable
fun ResultBox(
    item: NoteData
) {
    Surface(
        modifier = Modifier.padding(vertical = MaterialTheme.dimens.small2),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.borderColor,
                    shape = MaterialTheme.shapes.small
                )
                .padding(MaterialTheme.dimens.small3),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {
            Text(
                text = item.title, style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.darkPrimaryTextColor
                )
            )

            if(item.location.isNotBlank()){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Location")
                    Text(
                        text = item.location, style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.primaryTextColor
                        )
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Column {
                Text(
                    text = stringResource(SharedRes.Strings.description), style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.darkPrimaryTextColor
                    )
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(SharedRes.Strings.startDate), style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.darkPrimaryTextColor
                        )
                    )
                    Text(
                        text = item.startDateBS, style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.primaryTextColor
                        )
                    )
                }
                Column {
                    Text(
                        text = stringResource(SharedRes.Strings.endDate), style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.darkPrimaryTextColor
                        )
                    )
                    Text(
                        text = item.endDateBS, style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.primaryTextColor
                        )
                    )
                }
                IconButton(
                    onClick = {}) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "edit",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.LockClock,
                        contentDescription = "edit",
                        tint = MaterialTheme.colorScheme.borderColor
                    )
                }
            }
        }
    }
}