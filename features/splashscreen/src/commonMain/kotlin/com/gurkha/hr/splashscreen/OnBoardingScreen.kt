package com.gurkha.hr.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.gurkha.hr.splashscreen.model.OnBoardingAction
import com.gurkha.hr.splashscreen.model.ScreenList
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.snapshotFlow
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.splashscreen.model.Indicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val screens = viewModel.screens
    val currentPage by viewModel.currentPage.collectAsState()
    val indicators by viewModel.indicator.collectAsState(initial = screens.map {
        Indicator(
            Color.Gray,
            10.dp,
        )
    })


    val pageState = rememberPagerState(
        initialPage = currentPage,
        pageCount = { screens.size }
    )

    // only trigger if the current_page changes
    LaunchedEffect(currentPage) {
        if (pageState.currentPage != currentPage) {
            pageState.animateScrollToPage(currentPage)
        }
    }

    // Update ViewModel when pager is manually scrolled
    LaunchedEffect(pageState) {
        snapshotFlow { pageState.currentPage }.collect { page ->
            viewModel.setCurrentPage(page)
        }
    }

    // if the navigation channel gives value true then it navigate to the login screen
    LaunchedEffect(Unit) {
        viewModel.navigationChannel.collect { shouldNavigate ->
            if (shouldNavigate) onNavigateToLogin()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    TextButton(
                        modifier = Modifier.padding(MaterialTheme.dimens.small2),
                        onClick = {
                            viewModel.action(OnBoardingAction.OnSkip)
                        }
                    ) {
                        Text(
                            "Skip",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            HorizontalPager(state = pageState) { item ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
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
                            model = screens[item].image,
                            contentDescription = screens[item].title,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(topStart = MaterialTheme.dimens.medium1, topEnd = MaterialTheme.dimens.medium1))
                            .weight(6f)
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

                        Text(
                            text = screens[item].title,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

                        Text(
                            text = screens[item].description,
                            color = MaterialTheme.colorScheme.primaryTextColor,
                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Indicator & Button
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.medium2)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(screens.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(MaterialTheme.dimens.extraSmall)
                                .clip(RoundedCornerShape(MaterialTheme.dimens.medium2))
                                .background(indicators[index].color)
                                .width(indicators[index].width)
                                .height(MaterialTheme.dimens.small2)
                        )
                    }
                }

                ERPButton(
                    onClick = {
                        viewModel.action(OnBoardingAction.OnNext)
                    },
                    modifier = Modifier.defaultMinSize(250.dp),
                    text = if (currentPage != screens.size - 1) "Next" else "Get Started"
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))
            }
        }
    }
}
