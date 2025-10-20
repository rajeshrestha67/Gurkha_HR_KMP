package com.gurkha.hr.profile.profile_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.ProfilePicture
import com.gurkha.hr.components.media.MediaSelectorModalBottomSheet
import com.gurkha.hr.profile.model.profile_screen.AccountList
import com.gurkha.hr.profile.model.profile_screen.GeneralList
import com.gurkha.hr.profile.model.profile_screen.ProfileScreenState
import com.gurkha.hr.profile.profile_screen.model.ProfileScreenAction
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.imageBackgroundColor
import com.gurkha.hr.res.theme.logOutButtonColor
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val TAG = "ProfileScreen"


@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onAccountClick: (AccountList) -> Unit,
    onGeneralClick: (GeneralList) -> Unit
) {
    val viewModel: ProfileScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreenContent(
        onLogout = onLogout,
        onAccountClick = onAccountClick,
        onGeneralClick = onGeneralClick,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    onLogout: () -> Unit,
    onAccountClick: (AccountList) -> Unit,
    onGeneralClick: (GeneralList) -> Unit,
    state: ProfileScreenState,
    onAction: (ProfileScreenAction) -> Unit
) {
    var showMediaBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(start = MaterialTheme.dimens.small2),
                windowInsets = WindowInsets(0.dp),
                title = {

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ProfilePicture(
                            imageUrl = state.userProfileUrl,
                            employeeName = state.fullName,
                            nameInitials = state.initials,
                            size = MaterialTheme.dimens.extraLarge,
                            shape = CircleShape,
                            background = MaterialTheme.colorScheme.imageBackgroundColor,
                            borderWidth = 0.5.dp,
                            borderColor = MaterialTheme.colorScheme.borderColor,
                            ratio = 1f,
                            onClick = {
                                showMediaBottomSheet = true
                            }
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = MaterialTheme.dimens.small2)
                        ) {
                            Text(
                                style = MaterialTheme.typography.titleMedium,
                                text = state.fullName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis

                            )
                            Text(
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.secondaryTextColor
                                ),
                                maxLines = 1,
                                text = state.levelName
                            )
                            Text(
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.secondaryTextColor
                                ),
                                text = state.phoneNumber
                            )
                        }
                    }
                }
            )
        }


    ) { paddingValues ->
        ProfileScreenContainer(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            onLogout = onLogout,
            onAccountClick = onAccountClick,
            onGeneralClick = onGeneralClick
        )

    }
    if (showMediaBottomSheet) {
        MediaSelectorModalBottomSheet(
            tag = TAG,
            onDismiss = {
                showMediaBottomSheet = false
            },
            onImageReceived = { uri ->
                showMediaBottomSheet = false
                onAction(ProfileScreenAction.OnProfileImageReceived(uri))
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileScreenContainer(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onAccountClick: (AccountList) -> Unit,
    onGeneralClick: (GeneralList) -> Unit
) {

    val generalList = remember { GeneralList.list }
    val accountList = remember { AccountList.list }

    var showDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        )
    ) {


        profileHeaderSection(
            title = SharedRes.Strings.general,
            key = "general title"
        )
        profileListGeneral(
            list = generalList
        ) { item ->
            ProfileItemRow(
                text = stringResource(item.title),
                onClick = { onGeneralClick(item) },
                showDivider = item != GeneralList.Report
            )

        }

        profileHeaderSection(
            title = SharedRes.Strings.account,
            key = "account title"
        )


        profileListAccount(
            list = accountList
        ) { item ->
            ProfileItemRow(
                text = stringResource(item.title),
                onClick = {
                    onAccountClick(item)
                }
            )
        }

        //Log Out Button
        item {
            TextButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.dimens.small1,
                        vertical = MaterialTheme.dimens.small2
                    )
                    .fillMaxWidth(),

                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(SharedRes.Strings.log_out),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.logOutButtonColor
                        )
                    )
                }
            }

            if (showDialog) {
                LogoutBottomSheet(
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        onLogout()
                        showDialog = false
                    }
                )
            }
        }
    }
}

private fun LazyListScope.profileHeaderSection(
    title: StringResource,
    key: String
) {
    item(key = key) { SectionHeader(title) }
}

private fun LazyListScope.profileListAccount(
    list: List<AccountList>,
    itemContent: @Composable LazyItemScope.(item: AccountList) -> Unit
) {
    items(
        list, key = { it.toString() },
        itemContent = itemContent
    )
}


private fun LazyListScope.profileListGeneral(
    list: List<GeneralList>,
    itemContent: @Composable LazyItemScope.(item: GeneralList) -> Unit
) {
    items(
        list, key = { it.toString() },
        itemContent = itemContent
    )
}

@Composable
fun SectionHeader(text: StringResource) {

    Text(
        modifier = Modifier
            .padding(top = MaterialTheme.dimens.small1)
            .padding(
                horizontal = MaterialTheme.dimens.small3,
                vertical = MaterialTheme.dimens.small1,
            ),
        text = stringResource(text),
        style = MaterialTheme.typography.titleLarge

    )
}

@Composable
fun ProfileItemRow(
    text: String,
    onClick: () -> Unit,
    showDivider: Boolean = true
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(MaterialTheme.dimens.small2)
            .padding(MaterialTheme.dimens.small2),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            color = MaterialTheme.colorScheme.secondaryTextColor
        )
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = "Arrow Right"
        )
    }
    if (showDivider) {
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small3),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.borderColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val sheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        sheetState = sheet,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(
                MaterialTheme.dimens.small3
            ),
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.dimens.small2,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(MaterialTheme.dimens.promptDialogSize)
                    .aspectRatio(1f)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .padding(MaterialTheme.dimens.small3)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(SharedRes.Icons.logout),
                    contentDescription = "Logout",
                    tint = Color.White
                )
            }
            Text(
                modifier = Modifier.padding(top = MaterialTheme.dimens.small2),
                text = stringResource(SharedRes.Strings.are_you_sure),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(SharedRes.Strings.do_you_really_want_to_logout),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                ),
                textAlign = TextAlign.Center
            )

            ERPButton(
                modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.dimens.medium1),
                text = stringResource(SharedRes.Strings.log_out),
                onClick = onConfirm
            )
            ERPButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.Strings.cancel),
                backgroundColor = MaterialTheme.colorScheme.error,
                onClick = onDismiss
            )

        }

    }
}




