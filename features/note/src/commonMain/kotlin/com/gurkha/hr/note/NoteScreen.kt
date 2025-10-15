package com.gurkha.hr.note

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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gurkha.hr.domain.note.allNotes.mapper.toUi
import com.gurkha.hr.domain.note.allNotes.model.NoteData
import com.gurkha.hr.model.note.NoteAction
import com.gurkha.hr.model.note.NoteState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.model.note.ui.NoteDataUi
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navController: NavHostController,
    onGoToAddNotesScreen: (String?) -> Unit,
) {
    val viewModel: NoteViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val noteListState = rememberLazyListState()


    val result = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("data", null)
        ?.collectAsStateWithLifecycle()

    val isUpdate = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("isUpdate", null)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(result) {
        val json = result?.value
        val isUpdate = isUpdate?.value
        if (!json.isNullOrBlank() && !isUpdate.isNullOrBlank()) {
            viewModel.onAction(NoteAction.OnUpdateNoteDataJson(json, isUpdate))
            delay(500)
            noteListState.animateScrollToItem(state.noteItem.lastIndex + 1)
        }

    }

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
                onClick = { onGoToAddNotesScreen(null) },
                content = {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            )
        }
    ) { contentPadding ->
        NoteScreenContent(
            modifier = Modifier.padding(contentPadding),
            state = state,
            onAction = viewModel::onAction,
            onGoToAddNotesScreen = onGoToAddNotesScreen,
            noteListState = noteListState
        )
    }
}

@Composable
fun NoteScreenContent(
    modifier: Modifier = Modifier,
    noteListState: LazyListState,
    state: NoteState,
    onGoToAddNotesScreen: (String?) -> Unit,
    onAction: (NoteAction) -> Unit
) {
    LazyColumn(
        state = noteListState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = MaterialTheme.dimens.bottomBar,
            start = MaterialTheme.dimens.small3,
            end = MaterialTheme.dimens.small3
        ),
    ) {
        diffResult(
            onGoToAddNotesScreen = onGoToAddNotesScreen,
            state = state,
            onAction = onAction
        )
    }
}


fun LazyListScope.diffResult(
    onGoToAddNotesScreen: (String?) -> Unit,
    state: NoteState,
    onAction: (NoteAction) -> Unit
) {
    if (state.noteItem.isEmpty()) {
        item {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(SharedRes.Strings.no_data_found),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }
        }
    } else {
        items(state.noteItem) { item ->
            ResultBox(
                onGoToAddNotesScreen = onGoToAddNotesScreen,
                item = item,
                onAction = onAction
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultBox(
    onGoToAddNotesScreen: (String?) -> Unit,
    item: NoteData,
    onAction: (NoteAction) -> Unit
) {
    var showDialogue by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.padding(vertical = MaterialTheme.dimens.small2),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 1.dp
    ) {
        var showMore by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(MaterialTheme.dimens.small3),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title, style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.darkPrimaryTextColor
                    )
                )

                Box {
                    if (showMore) {
                        DropdownMenu(
                            containerColor = MaterialTheme.colorScheme.background,
                            expanded = showMore,
                            onDismissRequest = {
                                showMore = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(SharedRes.Strings.edit)
                                    )
                                },
                                onClick = {
                                    val data = Json.encodeToString<NoteDataUi>(item.toUi())
                                    onGoToAddNotesScreen(data)
                                    showMore = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(SharedRes.Strings.delete)
                                    )
                                },
                                onClick = {
                                    onAction(NoteAction.OnDeleteIdSelected(item.id))
                                    showDialogue = true
                                    showMore = false
                                }
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            showMore = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Option"
                        )
                    }
                }
            }

            if (showDialogue) {
                AlertDialog(
                    onDismissRequest = { },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onAction(NoteAction.OnDeleteNote)
                                showDialogue = false
                            }
                        ) {
                            Text(text = stringResource(SharedRes.Strings.yes))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDialogue = false
                            }
                        ) {
                            Text(text = stringResource(SharedRes.Strings.cancel))
                        }
                    },
                    title = {
                        Text("Confirmation")
                    },
                    text = {
                        Text("Are you sure you wanna delete?")
                    },
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    )
                )
            }


//            if (item.location.isNotBlank()) {
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(Icons.Filled.LocationOn, contentDescription = "Location")
//                    Text(
//                        text = item.location, style = MaterialTheme.typography.titleSmall.copy(
//                            color = MaterialTheme.colorScheme.primaryTextColor
//                        )
//                    )
//                }
//            }

            Column {
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }

        }
    }
}