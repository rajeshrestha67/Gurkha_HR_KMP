package com.gurkha.hr.profile.model.document_screen

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class DocumentScreenState(
    val selectedDocumentImageURi : String? = null,
    val selectedDocumentType: String? = null,
    val documentList : List<DocumentList> = documentItemList,
    val isUploading: Boolean = false,
    val isLongImagePressed : Boolean = false,
    val longPressedImage : String = ""
)


data class DocumentList(
    val title: StringResource,
    val uploadText: StringResource,
    val imageType: DocumentType,
    val uploadedImage: String? =null
)

val documentItemList = listOf(
    DocumentList(
        title = SharedRes.Strings.citizenshipFront,
        uploadText = SharedRes.Strings.uploadCitizenshipFront,
        imageType = DocumentType.CITIZENSHIP_FRONT
    ),
    DocumentList(
        title = SharedRes.Strings.citizenshipBack,
        uploadText = SharedRes.Strings.uploadCitizenshipBack,
        imageType = DocumentType.CITIZENSHIP_BACK
    ),
    DocumentList(
        title = SharedRes.Strings.nationalId,
        uploadText = SharedRes.Strings.uploadNationalId,
        imageType = DocumentType.NATIONAL_ID
    ),
    DocumentList(
        title = SharedRes.Strings.slcMarksheet,
        uploadText = SharedRes.Strings.uploadSlcMarksheet,
        imageType = DocumentType.SLC_MARKSHEET
    ),
    DocumentList(
        title = SharedRes.Strings.plusTwoImage,
        uploadText = SharedRes.Strings.uploadPlusTwoImage,
        imageType = DocumentType.PLUS_TWO_IMAGE
    ),
    DocumentList(
        title = SharedRes.Strings.bachelor,
        uploadText = SharedRes.Strings.uploadBachelor,
        imageType = DocumentType.BACHELOR
    ),
    DocumentList(
        title = SharedRes.Strings.master,
        uploadText = SharedRes.Strings.uploadMaster,
        imageType = DocumentType.MASTER
    ),
    DocumentList(
        title = SharedRes.Strings.experienceDocument,
        uploadText = SharedRes.Strings.uploadExperienceDocument,
        imageType = DocumentType.EXPERIENCE_DOCUMENT
    ),
    DocumentList(
        title = SharedRes.Strings.extra_image_one,
        uploadText = SharedRes.Strings.upload_extra_image_one,
        imageType = DocumentType.EXTRA_IMAGE_ONE
    ),
    DocumentList(
        title = SharedRes.Strings.extra_image_two,
        uploadText = SharedRes.Strings.upload_extra_image_two,
        imageType = DocumentType.EXTRA_IMAGE_TWO
    )
)


enum class DocumentType(val key: String) {
    PROFILE_IMAGE("PROFILE_IMAGE"),
    CITIZENSHIP_FRONT("CITIZENSHIP_FRONT"),
    CITIZENSHIP_BACK("CITIZENSHIP_BACK"),
    NATIONAL_ID("NATIONAL_ID"),
    SLC_MARKSHEET("SLC_MARKSHEET"),
    PLUS_TWO_IMAGE("PLUS_TWO_IMAGE"),
    BACHELOR("BACHELOR"),
    MASTER("MASTER"),
    EXPERIENCE_DOCUMENT("EXPERIENCE_DOCUMENT"),
    CLOCK_IN_IMAGE("CLOCK_IN_IMAGE"),
    CLOCK_OUT_IMAGE("CLOCK_OUT_IMAGE"),
    EXTRA_IMAGE_ONE("EXTRA_IMAGE_ONE"),
    EXTRA_IMAGE_TWO("EXTRA_IMAGE_TWO");

    companion object {
        val list: List<DocumentType>
            get() = entries.toList()
    }
}
