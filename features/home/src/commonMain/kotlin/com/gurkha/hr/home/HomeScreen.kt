package com.gurkha.hr.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gurkha.hr.home.Model.attendanceList
import com.gurkha.hr.home.Model.attendanceList2
import com.gurkha.hr.home.Model.calenderList
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens


@Composable
fun HomeScreen() {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier
            .padding(
                horizontal = MaterialTheme.dimens.small3, vertical = MaterialTheme.dimens.small2
            ).fillMaxSize(), containerColor = MaterialTheme.colorScheme.background,
        topBar = {

        }
    ) { paddingValues ->
        HomeScreenContent(modifier = Modifier.fillMaxSize().padding(paddingValues))
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
//            header part
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier.weight(4f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .width(MaterialTheme.dimens.medium3)
                            .height(MaterialTheme.dimens.medium3)
                            .background(Color.Black),
                        model = SharedRes.getRes(path = "drawable/gurkha_hr.png"),
                        contentDescription = "avatar",
                        contentScale = ContentScale.FillWidth,
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))
                    Column {
                        Text(
                            style = MaterialTheme.typography.titleMedium,
                            text = "Suneel Shrestha"
                        )
                        Text(
                            style = MaterialTheme.typography.titleSmall,
                            text = "Android Developer"
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Filled.Notifications, contentDescription = "notification icon"
                    )
                    Icon(
                        Icons.Filled.Call, contentDescription = "notification icon"
                    )
                    Icon(
                        Icons.Filled.Person, contentDescription = "notification icon"
                    )
                }
            }
        }

        //    Notification part
        item(key = "notification") {
            Row(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimens.medium2)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Notification view")
                Icon(Icons.Filled.Close, contentDescription = "close icon")
            }
        }

//            calender part
        stickyHeader(key = "calender") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                calenderList.forEach { item ->
                    val color =
                        if (item.active) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.background
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = color)
                            .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
                            .weight(1f)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = item.day)
                        Text(text = item.date)

                    }
                }
            }
        }

//        attendance part
        item {
            Column(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimens.medium2)
            ) {
                Text(text = "Today Attendance", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    attendanceList.forEach { item ->
                        AttendanceItemContent(
                            modifier = Modifier.weight(1f),
                            icon = item.icon,
                            title = item.title,
                            time = item.time,
                            status = item.status
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

//                second row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    attendanceList2.forEach { item ->
                        AttendanceItemContent(
                            modifier = Modifier.weight(1f),
                            icon = item.icon,
                            title = item.title,
                            time = item.time,
                            status = item.status
                        )
                    }
                }
            }

        }

//        activity part
        item {
            Text(text = "Your Activity", style = MaterialTheme.typography.titleLarge)
        }

//        draggable part
        item {

        }
    }
}

@Composable
fun AttendanceItemContent(
    icon: ImageVector,
    title: String,
    time: String,
    status: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.dimens.small2))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.dimens.small3,
                vertical = MaterialTheme.dimens.small2
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {
            Icon(icon, contentDescription = "arrow right")
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        }

        Column(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.dimens.small3,
                vertical = MaterialTheme.dimens.small2
            ),

            ) {
            Text(text = time, style = MaterialTheme.typography.titleLarge)
            Text(text = status, style = MaterialTheme.typography.titleSmall)
        }

    }
}