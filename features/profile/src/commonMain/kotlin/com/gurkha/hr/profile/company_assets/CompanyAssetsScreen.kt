package com.gurkha.hr.profile.company_assets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.domain.companyAssets.model.CompanyAssetsData
import com.gurkha.hr.profile.model.companyAssets.CompanyAssetsState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.highLightColor
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyAssetsScreen(
    onBackPressed: () -> Unit
) {
    val viewModel: CompanyAssetsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(stringResource(SharedRes.Strings.company_assets)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

            )
        }
    ) { paddingValues ->
        CompanyAssetsScreenContainer(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues),
            state = state
        )


    }
}

@Composable
fun CompanyAssetsScreenContainer(
    modifier: Modifier = Modifier,
    state: CompanyAssetsState
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small1,
        ),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.small2,
            alignment = Alignment.Top
        )
    ) {
        stickyHeader {
            HeaderSection(
                text = SharedRes.Strings.assignedAssets
            )
        }
        if (state.companyAssetsList.isEmpty()){
            item {
                EmptyMessage()
            }

        }else{
            items(
                state.companyAssetsList, key = { it.toString() },
                itemContent = { item ->
                    CompanyAssetsDetails(
                        item = item,

                        )
                },
            )
        }

    }
}


@Composable
fun EmptyMessage(){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(SharedRes.Strings.noAssetsAvailable),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.darkPrimaryTextColor
            ),

        )
    }
}
@Composable
fun HeaderSection(text: StringResource) {

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                MaterialTheme.dimens.small2
            ),
        text = stringResource(text),
        style = MaterialTheme.typography.titleLarge

    )
}

@Composable
fun CompanyAssetsDetails(
    item: CompanyAssetsData

) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .padding(MaterialTheme.dimens.small1)
            .background(MaterialTheme.colorScheme.highLightColor)
            .padding(
                horizontal = MaterialTheme.dimens.small2,
                vertical = MaterialTheme.dimens.small2
            ),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.small2,
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {


        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.dimens.small2)
        ) {
            Text(
                text = stringResource(SharedRes.Strings.date),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )
            Text(
                text = item.dateInBs,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.secondaryTextColor
                )
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.dimens.small1,
                    vertical = MaterialTheme.dimens.small1
                )
        )
        ColumnText(
            name = SharedRes.Strings.assetName,
            value = item.assetsName
        )
        ColumnText(
            name = SharedRes.Strings.description,
            value = item.assetsDescription
        )
    }


}

@Composable
private fun ColumnText(
    name: StringResource,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.dimens.small2),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(name),
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.secondaryTextColor
            )
        )
        Text(
            modifier = Modifier.weight(1f),
            text = value.trim(),
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.primaryTextColor
            ),
            textAlign = TextAlign.End
        )
    }
}


