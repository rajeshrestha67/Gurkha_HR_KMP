package com.gurkha.hr.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gurkha.hr.profile.model.ScreenList
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens


@Composable
fun ProfileScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
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
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        )
    ) {
        item(key = "header") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.small2)

            ) {
                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(MaterialTheme.dimens.extraLarge)
                        .border(
                            shape = CircleShape,
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ),
                    contentScale = ContentScale.FillWidth,

                    model = SharedRes.getRes(
                        "drawable/Default.png"
                    ),
                    contentDescription = "Profile Image",


                    )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .padding(top = MaterialTheme.dimens.small1)

                ) {
                    Text(
                        "Shreejesh Pathak",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W600
                        )
                    )
                    Text(
                        "Intern",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray

                    )
                    Text(
                        "9845875484",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray

                    )


                }
            }


        }

        items(items = ScreenList.screenList, key = { it.title }) { item ->
            val isFirst = item == ScreenList.screenList.first()
            Row(
                modifier = Modifier.fillMaxWidth()
                    .then(
                        if (isFirst) Modifier.padding(top = MaterialTheme.dimens.small3)
                        else Modifier
                    )
                    .clickable(
                        onClick = {
                            println("Button Clicked")
                        }
                    )
                    .padding(MaterialTheme.dimens.small2)
                    .background(
                        Color(0xFFD3D3D3),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(MaterialTheme.dimens.small3),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = item.title
                )
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Arrow Right",

                    )

            }
        }


    }
}