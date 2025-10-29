package com.gurkha.hr.note

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.components.prompts.PromptModalBottomSheet
import com.gurkha.hr.components.prompts.PromptType
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.domain.note.allNotes.model.NoteData
import com.gurkha.hr.domain.note.allNotes.model.toUi
import com.gurkha.hr.model.note.NoteAction
import com.gurkha.hr.model.note.NoteState
import com.gurkha.hr.res.SharedRes
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
    var showSuccessDialogue by remember { mutableStateOf(false) }
    var showErrorDialogue by remember { mutableStateOf(false) }
    var messageToShow by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.successChannel.collect {
            messageToShow = it
            showSuccessDialogue = true
        }
    }
    LaunchedEffect(Unit) {
        viewModel.errorChannel.collect {
            messageToShow = it
            showErrorDialogue = true
        }
    }


    val result =
        navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<String?>("data", null)
            ?.collectAsStateWithLifecycle()

    val isUpdate = navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<String?>(
        "isUpdate",
        null
    )?.collectAsStateWithLifecycle()

    val id = navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<Int?>("id", null)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        val id = id?.value
        id?.let {
            viewModel.onAction(NoteAction.OnDeleteNoteFromState(id))
        }
        navController.currentBackStackEntry?.savedStateHandle?.set("id", null)
    }

    LaunchedEffect(result?.value) {
        val json = result?.value
        val isUpdate = isUpdate?.value
        if (!json.isNullOrBlank() && !isUpdate.isNullOrBlank()) {
            viewModel.onAction(NoteAction.OnUpdateNoteDataJson(json, isUpdate))
            navController.currentBackStackEntry?.savedStateHandle?.apply {
                set("data", null)
                set("isUpdate", null)
            }
        } else {
//            execute for the added data
            json?.let {
                viewModel.onAction(NoteAction.OnUpdateNoteDataJson(json, isUpdate))
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    set("data", null)
                    set("isUpdate", null)
                }
            }
        }
    }
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    windowInsets = WindowInsets(0.dp), title = {
                        Text(text = stringResource(SharedRes.Strings.notes))
                    })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { onGoToAddNotesScreen(null) }, content = {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                })
            }) { contentPadding ->
            PullToRefreshBox(
                modifier = Modifier.padding(contentPadding),
                isRefreshing = state.isRefreshing,
                onRefresh = {
                    viewModel.onAction(NoteAction.OnRefresh)
                },
                content = {
                    NoteScreenContent(
                        modifier = Modifier,
                        state = state,
                        onAction = viewModel::onAction,
                        onGoToAddNotesScreen = onGoToAddNotesScreen,
                        onGoToDetailNotesScreen = onGoToDetailNotesScreen,
                        showSuccessDialogue = showSuccessDialogue,
                        showErrorDialogue = showErrorDialogue,
                        messageToShow = messageToShow,
                        onCloseSuccessDialogue = {
                            showSuccessDialogue = false
                        },
                        onCloseErrorDialogue = {
                            showSuccessDialogue = false
                        }
                    )
                }
            )
        }

        if (state.isDeletingData) {
            Box(
                modifier = Modifier.fillMaxSize().background(color = Color(0x80000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(MaterialTheme.dimens.medium3),
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NoteScreenContent(
    modifier: Modifier = Modifier,
    state: NoteState,
    onGoToAddNotesScreen: (String?) -> Unit,
    onGoToDetailNotesScreen: (String?) -> Unit,
    onAction: (NoteAction) -> Unit,
    showSuccessDialogue: Boolean,
    showErrorDialogue: Boolean,
    messageToShow: String,
    onCloseSuccessDialogue: () -> Unit,
    onCloseErrorDialogue: () -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = state.noteItem.isNotEmpty() || state.isFetchingNotes,
    ) { isVisible ->
        if (isVisible) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.animateContentSize().fillMaxSize(),
                state = rememberLazyStaggeredGridState(),
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.dimens.small3, vertical = MaterialTheme.dimens.small2
                ),
                verticalItemSpacing = MaterialTheme.dimens.small3,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                flingBehavior = ScrollableDefaults.flingBehavior(),
                userScrollEnabled = true,
                content = {
                    if (state.isFetchingNotes) {
                        items(8) {
                            val randomHeight = remember { (30..200).random() }
                            ShimmerView(
                                modifier = Modifier.fillMaxWidth()
                                    .clip(shape = MaterialTheme.shapes.small)
                                    .height(randomHeight.dp)
                            )
                        }
                    } else {
                        items(state.noteItem) { item ->
                            AnimatedContent(
                                targetState = item, transitionSpec = {
                                    slideInVertically { height -> height } + fadeIn() with slideOutVertically { height -> -height } + fadeOut()
                                }) {
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
        } else {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(SharedRes.Strings.no_data_found),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.erpColors.primaryTextColor
                    )
                )
            }
        }

    }



    if (showSuccessDialogue) {
        PromptModalBottomSheet(
            onBackPressed = onCloseSuccessDialogue,
            text = messageToShow
        )
    }
    if (showErrorDialogue) {
        PromptModalBottomSheet(
            promptType = PromptType.FAILED,
            onBackPressed = onCloseErrorDialogue,
            text = messageToShow
        )
    }

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
            val data = Json.encodeToString<NoteDataUi>(item.toUi())
            onGoToDetailNotesScreen(data)
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
                modifier = Modifier.heightIn(min = MaterialTheme.dimens.medium1).fillMaxWidth()
                    .padding(MaterialTheme.dimens.small2),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(end = MaterialTheme.dimens.small3),
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.erpColors.darkPrimaryTextColor
                        )
                    )
                }

                Text(
                    text = item.description,
                    maxLines = 7,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.erpColors.primaryTextColor
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
                        onDismissRequest = { showMore = false }) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(SharedRes.Strings.edit)) },
                            onClick = {
                                val data = Json.encodeToString<NoteDataUi>(item.toUi())
                                onGoToAddNotesScreen(data)
                                showMore = false
                            })
                        DropdownMenuItem(
                            text = { Text(text = stringResource(SharedRes.Strings.delete)) },
                            onClick = {
                                onAction(NoteAction.OnDeleteIdSelected(item.id))
                                showDialogue = true
                                showMore = false
                            })
                    }
                }

                IconButton(
                    modifier = Modifier.padding(0.dp).size(MaterialTheme.dimens.medium1),
                    onClick = { showMore = true }) {
                    Icon(
                        modifier = Modifier.padding(0.dp).size(MaterialTheme.dimens.medium1),
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Option"
                    )
                }
            }
            if (showDialogue) {
                PromptModalBottomSheet(
                    text = stringResource(SharedRes.Strings.delete_confirmation),
                    cancelButton = true,
                    onBackPressed = {
                        onAction(NoteAction.OnDeleteNote)
                        showDialogue = false
                    },
                    promptType = PromptType.FAILED,
                    buttonText = SharedRes.Strings.delete,
                    closePopUp = {
                        showDialogue = false
                    },
                )
            }

        }
    }
}
