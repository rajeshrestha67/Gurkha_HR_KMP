package com.gurkha.hr.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToDashboard: () -> Unit
) {

    val loginViewModel: LoginViewModel = koinViewModel()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
           // loginViewModel.login()
            //navController.navigate(AppRoute.Dashboard)
            onNavigateToDashboard()
        }) {
            Text("Press")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("terstasdfasd ")
            })
        }
    ) {

    }

}