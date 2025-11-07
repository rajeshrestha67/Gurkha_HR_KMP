package com.gurkha.hr.dashboard.graph

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.gurkha.hr.ChatViewModel
import com.gurkha.hr.chat_list.ChatListScreen
import com.gurkha.hr.chat_room.ChatRoomScreen
import com.gurkha.hr.components.sharedViewModel.koinNavGraphViewModel
import com.gurkha.hr.dashboard.route.ChatGraphRoute
import com.gurkha.hr.dashboard.route.ChatRoute

fun NavGraphBuilder.chatScreenBuilder(
    navController: NavHostController
) {
    navigation<ChatGraphRoute>(
        startDestination = ChatRoute.ChatList
    ) {
        composable<ChatRoute.ChatList> {
            val viewModel = navController.koinNavGraphViewModel<ChatViewModel, ChatGraphRoute>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            ChatListScreen(
                state = state,
                onAction = viewModel::onAction,
                onBackPressed = {
                    navController.popBackStack()
                },
                navigateToChat = { chatId ->
                    navController.navigate(ChatRoute.ChatRoom(chatId))
                }
            )
        }


        composable<ChatRoute.ChatRoom> {
            val viewModel = navController.koinNavGraphViewModel<ChatViewModel, ChatGraphRoute>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            ChatRoomScreen(
                state = state,
                onAction = viewModel::onAction,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }

}
