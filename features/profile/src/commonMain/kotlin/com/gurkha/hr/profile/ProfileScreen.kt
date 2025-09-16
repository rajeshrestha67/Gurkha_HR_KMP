package com.gurkha.hr.profile


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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gurkha.hr.profile.model.ScreenItem
import com.gurkha.hr.profile.model.ScreenList
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.imageBackgroundColor
import com.gurkha.hr.res.theme.logOutButtonColor
import com.gurkha.hr.res.theme.secondaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                navigationIcon = {
                    AsyncImage(
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .size(MaterialTheme.dimens.extraLarge)
                            .aspectRatio(1f)
                            .background(MaterialTheme.colorScheme.imageBackgroundColor),
                        model = SharedRes.getRes(path = "drawable/gurkha_hr.png"),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Fit,
                    )
                },
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
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
                            text = "Android Developer"
                        )
                        Text(
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.colorScheme.secondaryTextColor
                            ),
                            text = "9866290535"
                        )
                    }
                }
            )
        }


    ) { paddingValues ->
        ProfileScreenContainer(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        )

    }
}

@Composable
fun ProfileScreenContainer(
    modifier: Modifier = Modifier
) {

    val generalList = remember { ScreenList.general }
    val accountList = remember { ScreenList.account }

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
        profileList(
            list = generalList
        ) { item ->
            ProfileItemRow(
                text = stringResource(item.title),
                onClick = { println("Clicked Account Items") }
            )

        }

        profileHeaderSection(
            title = SharedRes.Strings.account,
            key = "account title"
        )


        profileList(
            list = accountList
        ) { item ->
            ProfileItemRow(
                text = stringResource(item.title),
                onClick = { println("Clicked Account Items") }
            )

        }

        //Log Out Button
        item {
            TextButton(
                onClick = {},
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
        }
    }
}

private fun LazyListScope.profileHeaderSection(
    title: StringResource,
    key: String
) {
    item(key = key) { SectionHeader(title) }
}

private fun LazyListScope.profileList(
    list: List<ScreenItem>,
    itemContent: @Composable LazyItemScope.(item: ScreenItem) -> Unit
) {
    items(
        list, key = { it.title.toString() },
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
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
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
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small2),
            thickness = 1.dp,
            color = Color.LightGray
        )
    }
}

