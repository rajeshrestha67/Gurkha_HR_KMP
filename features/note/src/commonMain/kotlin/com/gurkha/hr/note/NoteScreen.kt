package com.gurkha.hr.note

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    onGoToAddNotesScreen: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(text = stringResource(SharedRes.Strings.notes))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onGoToAddNotesScreen,
                content = {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            )
        }
    ) { contentPadding ->
        NoteScreenContent(modifier = Modifier.padding(contentPadding))
    }
}

@Composable
fun NoteScreenContent(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = MaterialTheme.dimens.bottomBar,
            start = MaterialTheme.dimens.small3,
            end = MaterialTheme.dimens.small3
        ),
    ) {
        diffResult()
    }
}


fun LazyListScope.diffResult() {
    items(4) {
        ResultBox()
    }
}


@Composable
fun ResultBox() {
    Surface(
        modifier = Modifier.padding(vertical = MaterialTheme.dimens.small2),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.borderColor,
                    shape = MaterialTheme.shapes.small
                )
                .padding(MaterialTheme.dimens.small3),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {
            Text(
                "Project Meeting", style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.darkPrimaryTextColor
                )
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.LocationOn, contentDescription = "Location")
                Text(
                    "RatoPool", style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Column {
                Text(
                    "Description", style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.darkPrimaryTextColor
                    )
                )
                Text(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elitLorem ipsum dolor sit amet, consectetur adipiscing elit",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Start Date", style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.darkPrimaryTextColor
                        )
                    )
                    Text(
                        "12:20", style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.primaryTextColor
                        )
                    )
                }
                Column {
                    Text(
                        "End Date", style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.darkPrimaryTextColor
                        )
                    )
                    Text(
                        "5:20", style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.primaryTextColor
                        )
                    )
                }
                IconButton(
                    onClick = {}) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "edit",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.LockClock,
                        contentDescription = "edit",
                        tint = MaterialTheme.colorScheme.borderColor
                    )
                }
            }
        }
    }
}