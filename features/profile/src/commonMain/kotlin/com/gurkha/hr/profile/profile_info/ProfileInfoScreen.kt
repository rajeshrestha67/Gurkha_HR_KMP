package com.gurkha.hr.profile.profile_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.gurkha.hr.profile.model.profileinfo_screen.InfoList
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfo
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfoScreenState
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfoViewAction
import com.gurkha.hr.profile.model.profileinfo_screen.contactInfo
import com.gurkha.hr.profile.model.profileinfo_screen.guardianInfo
import com.gurkha.hr.profile.model.profileinfo_screen.personalDetails
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.imageBackgroundColor
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
import com.gurkha.hr.res.theme.veryLightGray
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
    onAction:(ProfileInfoViewAction) -> Unit,
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,


        topBar = {
            TopAppBar(

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
    onAction:(ProfileInfoViewAction) -> Unit
) {
    val infoList = InfoList.list.map { stringResource(it.title) }
//    var selectedTab by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        )
    ) {
        item {
            ProfileCard()
        }
        stickyHeader {
            ProfileInfoRow(
                selectedIndex = state.selectedTab,
                items = infoList,
                onTabSelected = { index ->
                    onAction(ProfileInfoViewAction.OnItemSelected(index))
                }

            )
        }


        when (state.selectedTab) {
            0 -> {
                item {
                    HeaderSection(
                        title = SharedRes.Strings.contact_info,

                        )
                }

                items(contactInfo, key = { it.name.key }) { item ->
                    InfoItem(
                        item = item
                    )
                }
                item {
                    HeaderSection(
                        title = SharedRes.Strings.personal_details,

                        )
                }

                items(personalDetails, key = { it.name.key }) { item ->
                    InfoItem(
                        item = item
                    )
                }


            }

            1 -> {
                item {
                    HeaderSection(
                        title = SharedRes.Strings.guardian_information
                    )
                }

                items(guardianInfo, key = { it.name.key }) { item ->
                    InfoItem(
                        item = item
                    )
                }
            }
        }

    }
}


@Composable
fun ProfileCard() {
    Row(
        modifier = Modifier.padding(bottom = MaterialTheme.dimens.small3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar image
        AsyncImage(
            modifier = Modifier
                .size(MaterialTheme.dimens.profileScreenImageSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.imageBackgroundColor),
            model = SharedRes.getRes(path = "drawable/gurkha_hr.png"),
            contentDescription = "Profile picture",
            contentScale = ContentScale.Fit
        )

        // User info
        Column(
            modifier = Modifier
                .padding(start = MaterialTheme.dimens.small2)
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = "Shreejesh Pathak",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.secondaryTextColor
                ),
                maxLines = 1,
                text = "Intern"
            )
            Text(
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.secondaryTextColor
                ),
                text = "Employee Id: 136"
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
                            text = "TECH_Branch",
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
                            text = "baneshwor",
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
                        text = "Joined: 2082-05-17",
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
    selectedIndex: Int,
    items: List<String>,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.dimens.small3),

        divider = {

        },
        indicator = {}
    ) {
        items.forEachIndexed { index, title ->
            val isSelected = selectedIndex == index
            val backgroundColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.veryLightGray
            }
            val textColor = if (isSelected) {
                MaterialTheme.colorScheme.onBackground
            } else {
                MaterialTheme.colorScheme.primaryTextColor
            }

            Tab(
                modifier = Modifier
                    .background(backgroundColor),
                selected = isSelected,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = textColor
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
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




