package com.gurkha.hr.profile.model.document_screen

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

enum class DocumentList(
    val title: StringResource,
    val uploadText: StringResource
) {
    UploadProfileImage(
        title = SharedRes.Strings.profileImage,
        uploadText = SharedRes.Strings.uploadProfileImage),
    UploadCitizenshipFront(
        title = SharedRes.Strings.citizenshipFront,
        uploadText = SharedRes.Strings.uploadCitizenshipFront),
    UploadCitizenshipBack(
        title = SharedRes.Strings.citizenshipBack,
        uploadText = SharedRes.Strings.uploadCitizenshipBack),
    UploadNationalId(
        title = SharedRes.Strings.nationalId,
        uploadText = SharedRes.Strings.uploadNationalId),
    UploadSlcMarksheet(
        title = SharedRes.Strings.slcMarksheet,
        uploadText = SharedRes.Strings.uploadSlcMarksheet),
    UploadPlusTwoImage(
        title = SharedRes.Strings.plusTwoImage,
        uploadText = SharedRes.Strings.uploadPlusTwoImage),
    UploadBachelor(
        title = SharedRes.Strings.bachelor,
        uploadText = SharedRes.Strings.uploadBachelor),
    UploadMaster(
        title = SharedRes.Strings.master,
        uploadText = SharedRes.Strings.uploadMaster),
    UploadExperienceDocument(
        title = SharedRes.Strings.experienceDocument,
        uploadText = SharedRes.Strings.uploadExperienceDocument);
    companion object{
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