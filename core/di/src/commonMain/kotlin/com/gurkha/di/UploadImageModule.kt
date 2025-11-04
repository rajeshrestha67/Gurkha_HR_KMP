package com.gurkha.di

import com.gurkha.hr.data.uploadImage.KtorImageUploadRepository
import com.gurkha.hr.datastore.user_data.repository.LocalUserDataRepository
import com.gurkha.hr.domain.imageUpload.ImageUploadRepository
import com.gurkha.hr.domain.uploadImage.UploadImageUseCase
import io.ktor.client.HttpClient
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class UploadImageModule {

    @Factory(binds = [ImageUploadRepository::class])
    fun imageUploadRepository(httpClient: HttpClient): ImageUploadRepository =
        KtorImageUploadRepository(httpClient = httpClient)

    @Factory
    fun uploadImageUseCase(
        imageUploadRepository: ImageUploadRepository,
        userDataRepository: LocalUserDataRepository
    ): UploadImageUseCase = UploadImageUseCase(
        userDataRepository = userDataRepository,
        imageUploadRepository = imageUploadRepository)
}


