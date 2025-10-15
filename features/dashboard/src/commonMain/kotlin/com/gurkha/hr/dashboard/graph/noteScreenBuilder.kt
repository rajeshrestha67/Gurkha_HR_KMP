package com.gurkha.hr.dashboard.graph


import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gurkha.hr.addNoteScreen.AddNoteScreen
import com.gurkha.hr.dashboard.route.DashboardRoute
import com.gurkha.hr.dashboard.route.NoteRoute
import com.gurkha.hr.detailNoteScreen.DetailNoteScreen
import com.gurkha.hr.note.NoteScreen

fun NavGraphBuilder.noteScreenBuilder(
    navController: NavHostController,
    onGoToAddNotesScreen: (String?) -> Unit,
    onGoToDetailNotesScreen: (String?) -> Unit,
) {
    composable<DashboardRoute.NoteRoute> {
        NoteScreen(
            navController = navController,
            onGoToAddNotesScreen = onGoToAddNotesScreen,
            onGoToDetailNotesScreen= onGoToDetailNotesScreen
        )
    }

    composable<NoteRoute.AddNoteRoute> {
        val json: String? = it.toRoute<NoteRoute.AddNoteRoute>().json
        AddNoteScreen(
            navController = navController,
            json = json,
            onBackClicked = {
                navController.popBackStack(route = DashboardRoute.NoteRoute, inclusive = false)

            }
        )
    }

    composable<NoteRoute.DetailNoteRoute> {
        val json: String = it.toRoute<NoteRoute.DetailNoteRoute>().json
        DetailNoteScreen(
            navController = navController,
            json = json,
            onGoToAddNotesScreen = onGoToAddNotesScreen,
            onBackClicked = {
                navController.popBackStack(route = DashboardRoute.NoteRoute, inclusive = false)
            }
        )
    }
}