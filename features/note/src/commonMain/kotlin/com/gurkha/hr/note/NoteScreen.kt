package com.gurkha.hr.note

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
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
import com.gurkha.hr.domain.note.allNotes.model.NoteData
import com.gurkha.hr.domain.note.allNotes.model.toUi
import com.gurkha.hr.model.note.NoteAction
import com.gurkha.hr.model.note.NoteState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.model.note.ui.NoteDataUi
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navController: NavHostController,
    onGoToAddNotesScreen: (String?) -> Unit,
    onGoToDetailNotesScreen: (String?) -> Unit,
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

    LaunchedEffect(result?.value) {
        val json = result?.value
        val isUpdate = isUpdate?.value
        if (!json.isNullOrBlank() && !isUpdate.isNullOrBlank()) {
            viewModel.onAction(NoteAction.OnUpdateNoteDataJson(json, isUpdate))
            navController.currentBackStackEntry
                ?.savedStateHandle?.apply {
                    set("data", null)
                    set("isUpdate", null)
                }
        }else{
//            execute for the added data
           json?.let {
               viewModel.onAction(NoteAction.OnUpdateNoteDataJson(json, isUpdate))
               navController.currentBackStackEntry
                   ?.savedStateHandle?.apply {
                       set("data", null)
                       set("isUpdate", null)
                   }
           }
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
            noteListState = noteListState,
            onGoToDetailNotesScreen = onGoToDetailNotesScreen
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NoteScreenContent(
    modifier: Modifier = Modifier,
    noteListState: LazyListState,
    state: NoteState,
    onGoToAddNotesScreen: (String?) -> Unit,
    onGoToDetailNotesScreen: (String?) -> Unit,
    onAction: (NoteAction) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize(),
        state = rememberLazyStaggeredGridState(),
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        ),
        verticalItemSpacing = MaterialTheme.dimens.small3,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        flingBehavior = ScrollableDefaults.flingBehavior(),
        userScrollEnabled = true,
        content = {
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
                    AnimatedContent(
                        targetState = item,
                        transitionSpec = {
                            slideInVertically { height -> height } + fadeIn() with
                                    slideOutVertically { height -> -height } + fadeOut()
                        }
                    ) {
                        ResultBox(
                            onGoToAddNotesScreen = onGoToAddNotesScreen,
                            item = item,
                            onAction = onAction,
                            onGoToDetailNotesScreen = onGoToDetailNotesScreen
                        )
                    }
                }
            }
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultBox(
    onGoToAddNotesScreen: (String?) -> Unit,
    onGoToDetailNotesScreen: (String?) -> Unit,
    item: NoteData,
    onAction: (NoteAction) -> Unit
) {
    var showDialogue by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.clickable(onClick = {
//            val data = Json.encodeToString<NoteDataUi>(item.toUi())
//            onGoToDetailNotesScreen(data)
        }),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 4.dp,

        ) {
        var showMore by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd,
        ) {
            Column(
                modifier = Modifier
                    .heightIn(min = MaterialTheme.dimens.medium1)
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.small2),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(end = MaterialTheme.dimens.small3),
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.darkPrimaryTextColor
                        )
                    )
                }

                Text(
                    text = item.description,
                    maxLines = 7,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }
            Box(
                modifier = Modifier.padding(vertical = MaterialTheme.dimens.small2)
            ) {
                if (showMore) {
                    DropdownMenu(
                        containerColor = MaterialTheme.colorScheme.background,
                        expanded = showMore,
                        onDismissRequest = { showMore = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(SharedRes.Strings.edit)) },
                            onClick = {
                                val data = Json.encodeToString<NoteDataUi>(item.toUi())
                                onGoToAddNotesScreen(data)
                                showMore = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(SharedRes.Strings.delete)) },
                            onClick = {
                                onAction(NoteAction.OnDeleteIdSelected(item.id))
                                showDialogue = true
                                showMore = false
                            }
                        )
                    }
                }

                IconButton(
                    modifier = Modifier
                        .padding(0.dp)
                        .size(MaterialTheme.dimens.medium1),
                    onClick = { showMore = true }
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(0.dp)
                            .size(MaterialTheme.dimens.medium1),
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Option"
                    )
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
        }
    }
}