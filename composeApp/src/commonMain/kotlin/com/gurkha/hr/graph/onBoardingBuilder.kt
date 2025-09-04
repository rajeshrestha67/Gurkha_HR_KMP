package com.gurkha.hr.graph

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gurkha.hr.route.AppRoute

fun NavGraphBuilder.onBoardingBuilder(onClick: () -> Unit = {}){
    composable<AppRoute.OnBoardingRoute>{
        Text(
            modifier = Modifier.clickable{
                onClick()
            },
            text = "OnBoarding"
        )
    }
}
