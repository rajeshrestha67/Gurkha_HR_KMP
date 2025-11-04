package com.gurkha.model.uploadImage

enum class ImageUpdateDocumentType(val key: String) {
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
        val list: List<ImageUpdateDocumentType>
            get() = entries.toList()
    }
}