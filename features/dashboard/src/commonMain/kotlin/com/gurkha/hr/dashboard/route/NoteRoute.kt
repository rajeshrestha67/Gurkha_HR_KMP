package com.gurkha.hr.dashboard.route
import kotlinx.serialization.Serializable

@Serializable
sealed interface NoteRoute {
    @Serializable
    data object AddNoteRoute : NoteRoute

    @Serializable
    data object EditNoteRoute : NoteRoute
}