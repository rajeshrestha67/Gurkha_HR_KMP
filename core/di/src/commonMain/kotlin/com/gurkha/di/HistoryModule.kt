package com.gurkha.di

import com.gurkha.hr.data.history.KtorHistoryRemoteRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.history.repository.HistoryRemoteRepository
import com.gurkha.hr.domain.history.useCase.HistoryUseCase
import com.gurkha.hr.profile.history.HistoryViewModel
import io.ktor.client.HttpClient
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class HistoryModule {
    @Factory(binds = [HistoryRemoteRepository::class])
    fun historyRemoteRepository(httpClient: HttpClient): HistoryRemoteRepository=
        KtorHistoryRemoteRepository(httpClient)

    @Factory
    fun historyUseCase(historyRemoteRepository: HistoryRemoteRepository, userDataRepository:UserDataRepository): HistoryUseCase =
        HistoryUseCase(historyRemoteRepository = historyRemoteRepository, userDataRepository = userDataRepository)

    @Factory
    fun getHistoryScreenViewModel(
        historyUseCase: HistoryUseCase,
        requiredValidationUseCase: RequiredValidationUseCase,
        calendarModel: CalendarModel
    ): HistoryViewModel = HistoryViewModel(
        historyUseCase = historyUseCase,
        requiredValidationUseCase = requiredValidationUseCase,
        calendarModel = calendarModel
    )

}