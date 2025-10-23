package com.gurkha.hr.profile.profile_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ProfilePicture
import com.gurkha.hr.components.tabbar.ERPTabView
import com.gurkha.hr.profile.model.profileinfo_screen.InfoList
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfo
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfoScreenState
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfoViewAction
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.imageBackgroundColor
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoScreen(
    onBackPressed: () -> Unit

) {

    val viewModel: ProfileInfoScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProfileInfoScreenContainer(
        onBackPressed = onBackPressed,
        state = state,
        onAction = viewModel::action
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoScreenContainer(
    onBackPressed: () -> Unit,
    state: ProfileInfoScreenState,
    onAction: (ProfileInfoViewAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(),

        topBar = {

            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                modifier = Modifier.padding(top = MaterialTheme.dimens.small1),
                title = { Text(stringResource(SharedRes.Strings.profile)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {

                }

            )
        }
    ) { paddingValues ->
        ProfileInfoContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state,
            onAction = onAction
        )
    }
}

@Composable
fun ProfileInfoContainer(
    modifier: Modifier = Modifier,
    state: ProfileInfoScreenState,
    onAction: (ProfileInfoViewAction) -> Unit
) {
    //val infoList = InfoList.list.map { stringResource(it.title) }
//    var selectedTab by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        )
    ) {
        item {
            ProfileCard(
                state = state
            )
        }
        stickyHeader {
            ProfileInfoRow(
                selectedTab = state.selectedTab,
                items = state.infoList,
                onTabSelected = { index ->
                    onAction(ProfileInfoViewAction.OnItemSelected(index))
                }

            )
        }


        when (state.selectedTab) {
            InfoList.PersonalInfo -> {
                item {
                    HeaderSection(
                        title = SharedRes.Strings.contact_info,

                        )
                }

                items(state.contactInfo, key = { it.name.key }) { item ->
                    InfoItem(
                        item = item
                    )
                }
                item {
                    HeaderSection(
                        title = SharedRes.Strings.personal_details

                    )
                }

                items(state.personalDetails, key = { it.name.key }) { item ->
                    InfoItem(
                        item = item
                    )
                }


            }

            InfoList.EmergencyContact -> {
                item {
                    HeaderSection(
                        title = SharedRes.Strings.guardian_information
                    )
                }

                items(state.guardianInfo, key = { it.name.key }) { item ->
                    InfoItem(
                        item = item
                    )
                }
            }
        }

    }
}


@Composable
fun ProfileCard(
    state: ProfileInfoScreenState,
) {

    Row(
        modifier = Modifier.padding(bottom = MaterialTheme.dimens.small3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar image
        ProfilePicture(
            imageUrl = state.userProfileUrl,
            employeeName = state.fullName,
            nameInitials = state.initials,
            size = MaterialTheme.dimens.profileScreenImageSize,
            shape = CircleShape,
            background = MaterialTheme.colorScheme.imageBackgroundColor,
            borderWidth = 0.dp,
            borderColor = Color.Transparent,
            ratio = 1f
        )
        // User info
        Column(
            modifier = Modifier
                .padding(start = MaterialTheme.dimens.small2)
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
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
                text = "Employee Id: ${state.employeeId}"
            )


            Column(
                modifier = Modifier.padding(top = MaterialTheme.dimens.small1)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    // Branch
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "Branch Icon",

                            )
                        Text(
                            text = state.branchName,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    // Location
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location Icon",

                            )
                        Text(
                            text = state.address,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                //Date Joined
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = MaterialTheme.dimens.small1)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Joined Date Icon",

                        )
                    Text(
                        text = "Joined: ${state.joinedDate}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun ProfileInfoRow(
    selectedTab: InfoList,
    items: List<InfoList>,
    onTabSelected: (InfoList) -> Unit
) {
    ERPTabView(
        items = items,
        selectedTab = selectedTab,
        shape = MaterialTheme.shapes.medium,
        onItemSelected = {
            onTabSelected(it)
        }
    ) { item, isSelected ->
        val color =
            if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
        Text(
            text = stringResource(item.title),
            style = MaterialTheme.typography.titleSmall.copy(
                color = color
            )
        )
    }
}


@Composable
fun HeaderSection(
    title: StringResource

) {
    HeaderSectionStyle(text = title)

}


@Composable
fun HeaderSectionStyle(text: StringResource) {

    Text(
        modifier = Modifier
            .padding(top = MaterialTheme.dimens.small2)
            .padding(
                horizontal = MaterialTheme.dimens.small1,
                vertical = MaterialTheme.dimens.small2,
            ),
        text = stringResource(text),
        style = MaterialTheme.typography.titleLarge

    )
}

@Composable
private fun InfoItem(
    item: ProfileInfo
) {
    Column(
        modifier = Modifier.padding(
            vertical = MaterialTheme.dimens.small1,
            horizontal = MaterialTheme.dimens.small1
        )
    ) {
        Text(
            text = stringResource(item.name),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.secondaryTextColor
            )
        )
        Text(
            text = item.value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primaryTextColor
            )
        )
    }
}






