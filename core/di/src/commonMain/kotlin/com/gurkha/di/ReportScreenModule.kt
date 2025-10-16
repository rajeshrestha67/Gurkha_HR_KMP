package com.gurkha.di

import com.gurkha.hr.data.reportScreen.KtorReportRemoteRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.history.useCase.HistoryUseCase
import com.gurkha.hr.domain.reportScreen.repository.ReportRemoteRepository
import com.gurkha.hr.domain.reportScreen.useCase.ReportUseCase
import com.gurkha.hr.profile.report_screen.ReportViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class ReportScreenModule {
    @Factory(binds = [ReportRemoteRepository::class])
    fun reportRemoteRepository(httpClient: HttpClient): ReportRemoteRepository =
        KtorReportRemoteRepository(httpClient)

    @Factory
    fun reportUseCase(reportRemoteRepository: ReportRemoteRepository, userDataRepository: UserDataRepository): ReportUseCase =
        ReportUseCase(reportRemoteRepository = reportRemoteRepository, userDataRepository = userDataRepository)

    @KoinViewModel
    fun getReportViewModel(
        reportUseCase: ReportUseCase,
        requiredValidationUseCase: RequiredValidationUseCase,
        historyUseCase: HistoryUseCase
    ): ReportViewModel = ReportViewModel(
        reportUseCase = reportUseCase,
        requiredValidationUseCase = requiredValidationUseCase,
        historyUseCase = historyUseCase
    )

}