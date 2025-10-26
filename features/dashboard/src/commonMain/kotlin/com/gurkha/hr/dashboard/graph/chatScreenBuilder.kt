package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gurkha.hr.chat_list.ChatListScreen
import com.gurkha.hr.chat_room.ChatRoomScreen
import com.gurkha.hr.dashboard.route.ChatRoute

fun NavGraphBuilder.chatScreenBuilder(
    navController: NavHostController
) {
    composable<ChatRoute.ChatList> {
        ChatListScreen(
            onBackPressed = {
                navController.popBackStack()
            },
            navigateToChat = { chatId ->
                navController.navigate(ChatRoute.ChatRoom(chatId))
            }
        )
    }


    composable<ChatRoute.ChatRoom> {
        val chatUserJsonData = it.toRoute<ChatRoute.ChatRoom>().json
        ChatRoomScreen(
            onBackPressed = {
                navController.popBackStack()
            },
            chatUserJsonData = chatUserJsonData
        )
    }

}
