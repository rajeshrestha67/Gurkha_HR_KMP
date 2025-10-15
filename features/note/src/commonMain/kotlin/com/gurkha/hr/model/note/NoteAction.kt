package com.gurkha.hr.model.note

interface NoteAction {
    data class OnUpdateNoteDataJson(val data: String, val isUpdate : String) : NoteAction
    data object OnDeleteNote: NoteAction

    data class OnDeleteIdSelected(val id:Int): NoteAction


}