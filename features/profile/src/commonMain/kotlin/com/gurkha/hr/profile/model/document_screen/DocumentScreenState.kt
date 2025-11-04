package com.gurkha.hr.profile.model.document_screen

import com.gurkha.hr.res.SharedRes
import com.gurkha.model.uploadImage.ImageUpdateDocumentType
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
    val imageType: ImageUpdateDocumentType,
    val uploadedImage: String? =null
)

val documentItemList = listOf(
    DocumentList(
        title = SharedRes.Strings.citizenshipFront,
        uploadText = SharedRes.Strings.uploadCitizenshipFront,
        imageType = ImageUpdateDocumentType.CITIZENSHIP_FRONT
    ),
    DocumentList(
        title = SharedRes.Strings.citizenshipBack,
        uploadText = SharedRes.Strings.uploadCitizenshipBack,
        imageType = ImageUpdateDocumentType.CITIZENSHIP_BACK
    ),
    DocumentList(
        title = SharedRes.Strings.nationalId,
        uploadText = SharedRes.Strings.uploadNationalId,
        imageType = ImageUpdateDocumentType.NATIONAL_ID
    ),
    DocumentList(
        title = SharedRes.Strings.slcMarksheet,
        uploadText = SharedRes.Strings.uploadSlcMarksheet,
        imageType = ImageUpdateDocumentType.SLC_MARKSHEET
    ),
    DocumentList(
        title = SharedRes.Strings.plusTwoImage,
        uploadText = SharedRes.Strings.uploadPlusTwoImage,
        imageType = ImageUpdateDocumentType.PLUS_TWO_IMAGE
    ),
    DocumentList(
        title = SharedRes.Strings.bachelor,
        uploadText = SharedRes.Strings.uploadBachelor,
        imageType = ImageUpdateDocumentType.BACHELOR
    ),
    DocumentList(
        title = SharedRes.Strings.master,
        uploadText = SharedRes.Strings.uploadMaster,
        imageType = ImageUpdateDocumentType.MASTER
    ),
    DocumentList(
        title = SharedRes.Strings.experienceDocument,
        uploadText = SharedRes.Strings.uploadExperienceDocument,
        imageType = ImageUpdateDocumentType.EXPERIENCE_DOCUMENT
    ),
    DocumentList(
        title = SharedRes.Strings.extra_image_one,
        uploadText = SharedRes.Strings.upload_extra_image_one,
        imageType = ImageUpdateDocumentType.EXTRA_IMAGE_ONE
    ),
    DocumentList(
        title = SharedRes.Strings.extra_image_two,
        uploadText = SharedRes.Strings.upload_extra_image_two,
        imageType = ImageUpdateDocumentType.EXTRA_IMAGE_TWO
    )
)



