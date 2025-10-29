package com.gurkha.hr.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.splashscreen.model.Indicator
import com.gurkha.hr.splashscreen.model.OnBoardingAction
import com.gurkha.hr.splashscreen.model.OnBoardingScreenState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen(
    onNavigateToLogin: () -> Unit
) {

    val viewModel: OnBoardingViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val pageState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { state.screens.size }
    )


    // only trigger if the current_page changes
    LaunchedEffect(state.currentPage) {
        if (pageState.currentPage != state.currentPage) {
            pageState.animateScrollToPage(state.currentPage)
        }
    }

    // Update ViewModel when pager is manually scrolled
    LaunchedEffect(pageState) {
        snapshotFlow { pageState.currentPage }.collect { page ->
            viewModel.action(OnBoardingAction.SetCurrentPage(page))
        }
    }

    // if the navigation channel gives value true then it navigate to the login screen
    LaunchedEffect(Unit) {
        viewModel.navigationChannel.collect { shouldNavigate ->
            if (shouldNavigate) onNavigateToLogin()
        }
    }

    OnBoardingScreenContainer(
        state = state,
        onAction = viewModel::action,
        onNavigateToLogin = onNavigateToLogin,
        indicators = state.indicators,
        pageState = pageState
    )

}

@Composable
fun OnBoardingScreenContainer(
    state: OnBoardingScreenState,
    onAction: (OnBoardingAction) -> Unit,
    onNavigateToLogin: () -> Unit,
    indicators: List<Indicator>,
    pageState: PagerState
) {


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            HorizontalPager(state = pageState) { item ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = state.screens[item].image,
                        contentDescription = state.screens[item].title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(7f)
                            .padding(MaterialTheme.dimens.medium3),
                        contentScale = ContentScale.FillWidth
                    )
                    Column(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = MaterialTheme.dimens.medium1,
                                    topEnd = MaterialTheme.dimens.medium1
                                )
                            )
                            .weight(6f)
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

                        Text(
                            text = state.screens[item].title,
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            ),
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

                        Text(
                            text = state.screens[item].description,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.erpColors.primaryTextColor,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center

                            )
                        )
                    }
                }
            }


            if (state.currentPage != state.screens.size - 1) {
                TextButton(
                    modifier = Modifier.padding(MaterialTheme.dimens.small1)
                        .align(Alignment.TopEnd),
                    onClick = onNavigateToLogin
                ) {
                    Text(
                        text = stringResource(SharedRes.Strings.skip),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 14.sp
                        )
                    )
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
                    repeat(state.screens.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(MaterialTheme.dimens.extraSmall)
                                .clip(MaterialTheme.shapes.small)
                                .background(indicators[index].color)
                                .width(indicators[index].width)
                                .height(MaterialTheme.dimens.small2)
                        )
                    }
                }

                ERPButton(
                    onClick = {
                        onAction(OnBoardingAction.OnNext)
                    },
                    modifier = Modifier.fillMaxWidth(0.7f),
                    text = stringResource(state.title)
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))
            }
        }
    }
}


