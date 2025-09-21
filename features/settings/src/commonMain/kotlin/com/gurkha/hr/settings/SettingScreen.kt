package com.gurkha.hr.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.secondaryTextColor
import com.gurkha.hr.settings.model.SettingList
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onBackPressed:()-> Unit,
    onButtonPressed:()-> Unit,


){

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,


        topBar = {
            TopAppBar(
                title = {Text(stringResource(SharedRes.Strings.setting))},
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed){
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Back"
                        )
                    }
                }

            )
        }
    ){
        paddingValues ->
        SettingScreenContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onButtonPressed = (onButtonPressed),
        )

    }
}

@Composable
fun SettingScreenContainer(
    modifier: Modifier = Modifier,
    onButtonPressed:()-> Unit,
) {
    LazyColumn (
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        )
    ){
        settingListItems(SettingList.values().toList()){item ->
            SettingsItemRow(
                text = stringResource(item.title),
                onClick = {
                    when (item){
                        SettingList.ChangePassword -> {
                            onButtonPressed()
                        }

                        SettingList.Theme -> {

                        }
                        SettingList.Language -> {

                        }
                    }
                },

            )
        }

    }

}



private fun LazyListScope.settingListItems(
    list: List<SettingList>,
    itemContent: @Composable LazyItemScope.(item: SettingList) -> Unit
) {
    items(
        list, key = { it.toString() },
        itemContent = itemContent
    )
}
@Composable
fun SettingsItemRow(
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

