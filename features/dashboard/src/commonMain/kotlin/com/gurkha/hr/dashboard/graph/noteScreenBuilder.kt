package com.gurkha.hr.dashboard.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gurkha.hr.addNoteScreen.AddNoteScreen
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.dashboard.route.NoteRoute
import com.gurkha.hr.note.NoteScreen

fun NavGraphBuilder.noteScreenBuilder(
    navController: NavHostController,
    onGoToAddNotesScreen:()-> Unit
){
    composable<DashboardRoute.NoteRoute>{
        NoteScreen(
            onGoToAddNotesScreen = onGoToAddNotesScreen,
        )
    }

    composable<NoteRoute.AddNoteRoute>{
        AddNoteScreen(
            onBackClicked = {
                navController.popBackStack()
            }
        )
    }
}