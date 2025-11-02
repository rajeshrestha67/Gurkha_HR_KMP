package com.gurkha.hr.profile.model.document_screen

import coil3.Uri

interface DocumentScreenAction {
    data class OnSelectedDocument(val type : DocumentType): DocumentScreenAction
    data class OnReceivedDocumentUri(val uri : String): DocumentScreenAction
}