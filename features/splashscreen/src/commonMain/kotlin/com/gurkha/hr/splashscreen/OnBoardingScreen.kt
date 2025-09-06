package com.gurkha.hr.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.splashscreen.model.ScreenList
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen(
    onNavigateToLogin: () -> Unit
) {
    val screenList = ScreenList.screenList
    val pageState = rememberPagerState { screenList.size }

//    not fixed
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("")
                },
                actions = {
                    TextButton(
                        modifier = Modifier.padding(MaterialTheme.dimens.small2),
                        onClick = {
//                            not fixed
                            scope.launch {
                                pageState.animateScrollToPage(screenList.size - 1)
                            }
                        },
                    ) {
                        Text(
                            "Skip",
                            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    Color(0xFFE7F5DA)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(Color(0xFFE7F5DA))
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            HorizontalPager(state = pageState) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .weight(7f)
                            .padding(MaterialTheme.dimens.medium3)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopStart

                    ) {
                        AsyncImage(
                            model = screenList[item].image,
                            contentDescription = screenList[0].title,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .weight(6f)
                            .background(color = MaterialTheme.colorScheme.background)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        Spacer(modifier = Modifier.height(40.dp))

                        Text(
                            text = screenList[item].title,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = screenList[item].description,
                            color = MaterialTheme.colorScheme.primaryTextColor,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }


//        indicator and button section
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Row(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.medium2)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(screenList.size) { item ->
                        val color = if (item == pageState.currentPage) Color.Red else MaterialTheme.colorScheme.primary
                        val width = if (item == pageState.currentPage) 30.dp else 10.dp
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(shape = RoundedCornerShape(50.dp))
                                .background(
                                    color = color
                                )
                                .width(width)
                                .height(8.dp)
                        )

                    }
                }
                FilledTonalButton(
                    onClick = {
//                        not fixed
                        scope.launch {
                            if (pageState.currentPage == screenList.size - 1) {
                                onNavigateToLogin()
                            } else {
                                pageState.animateScrollToPage(pageState.currentPage + 1)

                            }
                        }
                    }, modifier = Modifier
                        .defaultMinSize(250.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface,
                    )
                ) {
                    Text(
                        if (pageState.currentPage != screenList.size - 1) "Next" else "Get Started",
                        modifier = Modifier.padding(MaterialTheme.dimens.small1)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }

    }

}

