package com.gurkha.hr.viewAllScreen

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ProfilePicture
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.imageBackgroundColor
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.veryLightGray
import com.gurkha.hr.model.viewAll.ViewAllScreenAction
import com.gurkha.hr.model.viewAll.ViewAllScreenState
import com.gurkha.model.upComingBirthday.ui.ViewAllUi
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllScreen(
    json: String?,
    onBackClicked: () -> Unit
) {
    val viewModel: ViewAllScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(json) {
        json?.let {
            viewModel.onAction(ViewAllScreenAction.OnJsonUpdate(json))
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text("View All")
                }
            )
        }
    ) { contentPadding ->
        ViewAllScreenContent(
            Modifier.fillMaxSize().padding(contentPadding),
            state = state
        )
    }
}


@Composable
fun ViewAllScreenContent(
    modifier: Modifier = Modifier,
   state: ViewAllScreenState
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            vertical = MaterialTheme.dimens.small2,
            horizontal = MaterialTheme.dimens.small3
        ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        state.data?.let {
            items(state.data) { item ->
                ResultBox(item = item)
            }
        }
    }
}


@Composable
fun ResultBox(
    item: ViewAllUi
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.veryLightGray)
            .padding(all = MaterialTheme.dimens.small2),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        ProfilePicture(
            imageUrl = item.imageUrl,
            employeeName = item.fullName,
            nameInitials = item.initials,
            size = MaterialTheme.dimens.large,
            shape = CircleShape,
            background = MaterialTheme.colorScheme.imageBackgroundColor,
            borderWidth = 0.5.dp,
            borderColor = MaterialTheme.colorScheme.borderColor,
            ratio = 1f
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                text = item.fullName, style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.darkPrimaryTextColor
                )
            )

            Text(
                text = "Designation : ${item.designationName}",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )

            Text(
                "Branch : ${item.branchName}", style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )

        }
    }
}


