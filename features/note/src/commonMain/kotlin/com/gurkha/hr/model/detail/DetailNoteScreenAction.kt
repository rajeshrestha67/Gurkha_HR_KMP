package com.gurkha.hr.model.detail

interface DetailNoteScreenAction {
    data class OnDeleteNote(val id: Int?): DetailNoteScreenAction
}