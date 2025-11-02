package com.gurkha.hr.profile.model.document_screen

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

enum class DocumentList(
    val title: StringResource,
    val uploadText: StringResource,
    val imageType: DocumentType,
) {
    UploadProfileImage(
        title = SharedRes.Strings.profileImage,
        uploadText = SharedRes.Strings.uploadProfileImage,
        imageType = DocumentType.PROFILE_IMAGE
    ),
    UploadCitizenshipFront(
        title = SharedRes.Strings.citizenshipFront,
        uploadText = SharedRes.Strings.uploadCitizenshipFront,
        imageType = DocumentType.CITIZENSHIP_FRONT
    ),
    UploadCitizenshipBack(
        title = SharedRes.Strings.citizenshipBack,
        uploadText = SharedRes.Strings.uploadCitizenshipBack,
        imageType = DocumentType.CITIZENSHIP_BACK

    ),
    UploadNationalId(
        title = SharedRes.Strings.nationalId,
        uploadText = SharedRes.Strings.uploadNationalId,
        imageType = DocumentType.NATIONAL_ID

    ),
    UploadSlcMarksheet(
        title = SharedRes.Strings.slcMarksheet,
        uploadText = SharedRes.Strings.uploadSlcMarksheet,
        imageType = DocumentType.SLC_MARKSHEET

    ),
    UploadPlusTwoImage(
        title = SharedRes.Strings.plusTwoImage,
        uploadText = SharedRes.Strings.uploadPlusTwoImage,
        imageType = DocumentType.PLUS_TWO_IMAGE

    ),
    UploadBachelor(
        title = SharedRes.Strings.bachelor,
        uploadText = SharedRes.Strings.uploadBachelor,
        imageType = DocumentType.BACHELOR
    ),
    UploadMaster(
        title = SharedRes.Strings.master,
        uploadText = SharedRes.Strings.uploadMaster,
        imageType = DocumentType.MASTER

    ),
    UploadExperienceDocument(
        title = SharedRes.Strings.experienceDocument,
        uploadText = SharedRes.Strings.uploadExperienceDocument,
        imageType = DocumentType.EXPERIENCE_DOCUMENT
    );

    companion object {
        private val titleMap =
            enumValues<DocumentList>().associateBy { it.title }
//        private val uploadMap =
//            enumValues<DocumentList>().associateBy { it.uploadText }

        fun get(titleName: StringResource): DocumentList =
            titleMap[titleName] ?: UploadProfileImage

//        fun get(uploadName: StringResource): DocumentList =
//            uploadMap[uploadName] ?: UploadProfileImage

        val list: List<DocumentList>
            get() = entries.toList().map { it }
    }


}

enum class DocumentType {
    PROFILE_IMAGE,
    CITIZENSHIP_FRONT,
    CITIZENSHIP_BACK,
    NATIONAL_ID,
    SLC_MARKSHEET,
    PLUS_TWO_IMAGE,
    BACHELOR,
    MASTER,
    EXPERIENCE_DOCUMENT,
    CLOCK_IN_IMAGE,
    CLOCK_OUT_IMAGE,
    EXTRA_IMAGE_ONE,
    EXTRA_IMAGE_TWO
}