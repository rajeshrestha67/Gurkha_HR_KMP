package com.gurkha.hr.detailNoteScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gurkha.hr.components.PlatformMessage
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.components.platform_utils.PlatformUtils
import com.gurkha.hr.components.prompts.PromptModalBottomSheet
import com.gurkha.hr.components.prompts.PromptType
import com.gurkha.hr.domain.note.allNotes.model.toUi
import com.gurkha.hr.model.detail.DetailNoteScreenAction
import com.gurkha.hr.model.note.NoteAction
import com.gurkha.hr.res.SharedRes
import com.gurkha.model.note.ui.NoteDataUi
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailNoteScreen(
    json: String,
    navController: NavHostController,
    onBackClicked: () -> Unit,
    onGoToAddNotesScreen: (String?) -> Unit,
) {
    val viewModel: DetailNoteScreenViewModel = koinViewModel()
    var note by remember { mutableStateOf<NoteDataUi?>(null) }
    var showMore by remember { mutableStateOf(false) }
    var showDialogue by remember { mutableStateOf(false) }

    var showSuccessDialogue by remember { mutableStateOf(false) }
    var showErrorDialogue by remember { mutableStateOf(false) }
    var messageToShow by remember { mutableStateOf("") }

    var sendData by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.successChannel.collect {
            messageToShow = it
            showSuccessDialogue = true
        }
    }
    LaunchedEffect(Unit) {
        viewModel.successChannel.collect {
            messageToShow = it
            showErrorDialogue = true
        }
    }

    LaunchedEffect(sendData) {
        if (sendData) {
            navController.previousBackStackEntry?.savedStateHandle?.set("id", note?.id)
            onBackClicked()
        }
    }



    LaunchedEffect(Unit) {
        viewModel.successChannel.collect {
            messageToShow = it
            showSuccessDialogue = true
        }
    }
    LaunchedEffect(Unit) {
        viewModel.successChannel.collect {
            messageToShow = it
            showErrorDialogue = true
        }
    }

    LaunchedEffect(sendData) {
        if (sendData) {
            navController.previousBackStackEntry?.savedStateHandle?.set("id", note?.id)
            onBackClicked()
        }
    }


    LaunchedEffect(json) {
        val data = Json.decodeFromString<NoteDataUi>(json)
        note = data
    }

    val platformUtils: PlatformUtils = koinInject<PlatformUtils>()

    val platformMessage: PlatformMessage = koinInject()
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(
                        note?.title ?: "", style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.erpColors.darkPrimaryTextColor
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClicked,
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier.padding(MaterialTheme.dimens.small3)
                    ) {
                        //show the delete and other option drop down
                        if (showMore) {
                            DropdownMenu(
                                containerColor = MaterialTheme.colorScheme.background,
                                expanded = showMore,
                                onDismissRequest = { showMore = false }) {

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(SharedRes.Strings.delete),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                MaterialTheme.colorScheme.error
                                            )
                                        )
                                    },
                                    onClick = {
                                        showDialogue = true
                                        showMore = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(SharedRes.Strings.copy),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                MaterialTheme.colorScheme.onBackground
                                            )
                                        )
                                    },
                                    onClick = {
                                        platformUtils.copyToClipboard(note?.description ?: "")
                                        platformMessage.showToast("Copied!!")
                                        showMore = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.ContentCopy,
                                            contentDescription = "Copy",
                                            tint = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                )

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(SharedRes.Strings.share),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                MaterialTheme.colorScheme.onBackground
                                            )
                                        )
                                    },
                                    onClick = {
                                        platformUtils.shareText(note?.description ?: "", note?.title)
                                        showMore = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.Share,
                                            contentDescription = "Share",
                                            tint = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                )
                            }
                        }

                        IconButton(
                            modifier = Modifier
                                .size(MaterialTheme.dimens.medium1),
                            onClick = { showMore = true }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(MaterialTheme.dimens.medium1),
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Option"
                            )
                        }
                    }

                    //show the confirm bottom modal
                    if (showDialogue) {
                        if (showDialogue) {
                            PromptModalBottomSheet(
                                text = stringResource(SharedRes.Strings.delete_confirmation),
                                cancelButton = true,
                                onBackPressed = {
                                    viewModel.onAction(DetailNoteScreenAction.OnDeleteNote(note?.id))
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
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.dimens.small3)
                .verticalScroll(rememberScrollState())
        ) {
            note?.description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.erpColors.darkPrimaryTextColor
                    )
                )
            }

        }

        if (showSuccessDialogue) {
            PromptModalBottomSheet(
                onBackPressed = {
                    sendData = true
                },
                text = messageToShow
            )
        }
        if (showErrorDialogue) {
            PromptModalBottomSheet(
                promptType = PromptType.FAILED,
                onBackPressed = onBackClicked,
                text = messageToShow
            )
        }
    }
}

