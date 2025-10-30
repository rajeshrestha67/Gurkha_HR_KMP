package com.gurkha.hr.profile.document

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.profile.model.document_screen.DocumentList
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScreen(
    onBackPressed: () -> Unit,
) {

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(stringResource(SharedRes.Strings.document)) },
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
        PullToRefreshBox(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            isRefreshing = false,
            onRefresh = {},
            content = {
                DocumentScreenContainer()
            }
        )
    }
}

@Composable
fun DocumentScreenContainer(
) {
    val documentList = remember { DocumentList.list }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        )
    ) {
        items(
            documentList,
            key = { it.toString() },
        ) { item ->
            DocumentItemRow(
                text = stringResource(item.title),
                uploadText = stringResource(item.uploadText),
                onClick = {},
            )
        }
    }
}

@Composable
fun DocumentItemRow(
    text: String,
    uploadText: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onClick() }
            .padding(MaterialTheme.dimens.small3),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.erpColors.primaryTextColor
            ),
            textAlign = TextAlign.Center
        )
        Icon(
            imageVector = Icons.Filled.CloudUpload,
            contentDescription = "upload",
            modifier = Modifier.size(MaterialTheme.dimens.medium1),
            tint = MaterialTheme.erpColors.secondaryTextColor
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
        Text(
            text = uploadText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.erpColors.secondaryTextColor,
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
        )
    }
}

