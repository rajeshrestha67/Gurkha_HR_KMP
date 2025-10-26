package com.gurkha.hr.dashboard.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface NoteRoute {
    @Serializable
    data class AddNoteRoute (val json: String?) : NoteRoute
    @Serializable
    data class DetailNoteRoute (val json: String) : NoteRoute
}