package com.gurkha.hr.components.sharedViewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel

@Composable
inline fun <reified VM : ViewModel, reified Parent : Any>
        NavController.koinNavGraphViewModel(): VM {

    val parentEntry = remember(Parent::class) {
        getBackStackEntry(Parent::class.qualifiedName!!)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}