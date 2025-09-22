package com.gurkha.hr.profile

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.imageBackgroundColor
import com.gurkha.hr.res.theme.secondaryTextColor
import org.jetbrains.compose.resources.stringResource
//import com.gurkha.hr.profile.model.InfoList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveScreen()

{
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),


        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {Text(stringResource(SharedRes.Strings.profile))},
                navigationIcon = {
                    IconButton(
                        onClick = {}){
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {

                }

            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ProfileInfoContainer()
        }
    }
}

@Composable
fun ProfileInfoContainer(
    modifier: Modifier = Modifier,

    ){
    LazyColumn (
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        )
    ){
        item {
            ProfileCard()
        }
    }
}

//private fun LazyListScope.ProfileInfoScreenItems(
//    list: List<InfoList>,
//    itemContent: @Composable LazyItemScope.(item: InfoList) -> Unit
//){
//    items(
//        list, key = { it.toString() },
//        itemContent = itemContent
//    )
//
//}


@Composable
fun ProfileCard() {
    Row(
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
                style = MaterialTheme.typography.titleMedium,
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
                            tint = MaterialTheme.colorScheme.primary
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
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "baneshwor",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                // Date joined (below the row)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = MaterialTheme.dimens.small1)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Joined Date Icon",
                        tint = MaterialTheme.colorScheme.primary
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



