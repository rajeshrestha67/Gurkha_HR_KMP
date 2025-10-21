package com.gurkha.hr.viewAllScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gurkha.hr.components.ProfilePicture
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.veryLightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllScreen(
    json: String?,
    onBackClicked:()-> Unit
){
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
    ) {contentPadding->
        ViewAllScreenContent(Modifier.fillMaxSize().padding(contentPadding))
    }
}


@Composable
fun ViewAllScreenContent(
    modifier: Modifier = Modifier,
){
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            vertical = MaterialTheme.dimens.small2,
            horizontal = MaterialTheme.dimens.small3
        ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        items(3){item->
            ResultBox()
        }
    }
}


@Composable
fun ResultBox(){
    Column (
        modifier = Modifier.fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.veryLightGray)
            .padding(vertical = MaterialTheme.dimens.small3)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        ProfilePicture(
            imageUrl = "",
            employeeName = "Suneel",
            nameInitials = "SS",
            size = MaterialTheme.dimens.extraLarge,
            shape = MaterialTheme.shapes.extraLarge,
            background = MaterialTheme.colorScheme.primary,
            borderWidth = 1.dp,
            borderColor = MaterialTheme.colorScheme.primary,
            ratio = 1f,
        )

        Text("Suneel Shrestha", style = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.darkPrimaryTextColor
        ))

        Text("Intern", style = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.darkPrimaryTextColor
        ))
    }
}