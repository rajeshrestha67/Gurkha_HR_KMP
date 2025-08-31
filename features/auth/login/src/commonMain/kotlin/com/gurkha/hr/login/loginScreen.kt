package com.gurkha.hr.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gurkha.hr.res.SharedRes

import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToDashboard: () -> Unit
) {

    val loginViewModel: LoginViewModel = koinViewModel()

//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Button(onClick = {
//           // loginViewModel.login()
//            //navController.navigate(AppRoute.Dashboard)
//            onNavigateToDashboard()
//        }) {
//            Text("Press")
//        }
//    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
        ) {

            AsyncImage(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
                    .weight(1f),
                model = SharedRes.getRes("drawable/gurkha_hr.png"),
                contentDescription = "gurkha_hr",
                contentScale = ContentScale.FillWidth
            )


            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {

                Text(
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.Green,
                        fontSize = 30.sp
                    ),
                    text = stringResource(SharedRes.Strings.welcome)
                )
            }


        }

    }

}