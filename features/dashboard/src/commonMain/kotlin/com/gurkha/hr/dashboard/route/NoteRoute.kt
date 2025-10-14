package com.gurkha.hr.dashboard.route
import com.gurkha.hr.domain.note.addNote.model.AddNoteData
import kotlinx.serialization.Serializable

@Serializable
sealed interface NoteRoute {
    @Serializable
    data object AddNoteRoute : NoteRoute

    @Serializable
    data class EditNoteRoute(val json: String) : NoteRoute
}