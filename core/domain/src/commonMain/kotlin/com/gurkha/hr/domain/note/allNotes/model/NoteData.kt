package com.gurkha.hr.domain.note.allNotes.model


data class NoteData(
    val id: Int ,
    val title: String ,
    val description: String ,
    val isEvent: String ,
    val startDateAD: String ,
    val endDateAD: String ,
    val startDateBS: String ,
    val endDateBS: String ,
    val location: String ,
    val active: String ,
    val startTime: String ,
    val endTime: String ,
    val isReminder: String ,
    val createdAtAd: String ,
    val createdAtBs: String
)

