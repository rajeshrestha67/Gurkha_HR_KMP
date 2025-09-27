package com.gurkha.hr.profile.profile_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gurkha.hr.profile.model.profile_screen.AccountList
import com.gurkha.hr.profile.model.profile_screen.GeneralList
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.imageBackgroundColor
import com.gurkha.hr.res.theme.logOutButtonColor
import com.gurkha.hr.res.theme.logOutTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onAccountClick:(AccountList)->Unit,
    onGeneralClick:(GeneralList)->Unit
) {
    val viewModel: ProfileScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(start = MaterialTheme.dimens.small3),
                windowInsets = WindowInsets(0.dp),
                navigationIcon = {
                    AsyncImage(
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .size(MaterialTheme.dimens.extraLarge)
                            .aspectRatio(1f)
                            .background(MaterialTheme.colorScheme.imageBackgroundColor),
                        model = state.userProfileUrl,
                        contentDescription = "avatar",
                        contentScale = ContentScale.Fit,
                    )
                },
                title = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = MaterialTheme.dimens.small2)
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
}

@Composable
fun ProfileScreenContainer(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onAccountClick:(AccountList) -> Unit,
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
                showDivider = item != GeneralList.History
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
                        horizontal = MaterialTheme.dimens.small2,
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
                ActionDialog(
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
            .padding(top = MaterialTheme.dimens.small2)
            .padding(
                horizontal = MaterialTheme.dimens.small3,
                vertical = MaterialTheme.dimens.small2,
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
                .padding(horizontal = MaterialTheme.dimens.small2),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.borderColor
        )
    }
}


@Composable
fun ActionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(SharedRes.Strings.are_you_sure)) },
        text = { Text(text = stringResource(SharedRes.Strings.do_you_really_want_to_logout)) },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.logOutButtonColor,
                    contentColor = MaterialTheme.colorScheme.logOutTextColor

                )

            ) {
                Text(text = stringResource(SharedRes.Strings.yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }) {
                Text(
                    text = stringResource(SharedRes.Strings.no),
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    )
}




