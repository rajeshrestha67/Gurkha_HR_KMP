package com.gurkha.hr.profile.company_assets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyAssetsScreen(
    onBackPressed: () -> Unit
) {

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
        )


    }
}

@Composable
fun CompanyAssetsScreenContainer(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3
        )
    ) {
        stickyHeader {
            HeaderSection(
                text = SharedRes.Strings.assignedAssets
            )
        }
        item(key = "company_assets") {
            CompanyAssetsRow()
        }
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
fun CompanyAssetsRow(

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.dimens.small3,
                vertical = MaterialTheme.dimens.small2
            ),
//            .border(
//                width = 1.dp,
//                shape = MaterialTheme.shapes.extraSmall,
//                color = MaterialTheme.colorScheme.outline
//            ),
        horizontalArrangement = Arrangement.SpaceBetween,
    )
    {
        AssetsRowText(
            name = SharedRes.Strings.assetName
        )
        AssetsRowText(
            name = SharedRes.Strings.description
        )
        AssetsRowText(
            name = SharedRes.Strings.assignedDate
        )
    }
    Text(
        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
        text = "No Asset Found"
    )

}

@Composable
fun AssetsRowText(
    name: StringResource
) {
    Text(
        text = stringResource(name),
        style = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.primaryTextColor,
            fontWeight = FontWeight.SemiBold
        )
    )
}
