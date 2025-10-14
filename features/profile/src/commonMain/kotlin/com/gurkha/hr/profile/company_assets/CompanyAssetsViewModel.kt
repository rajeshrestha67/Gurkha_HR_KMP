package com.gurkha.hr.profile.company_assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.companyAssets.usecase.CompanyAssetsUseCase
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.companyAssets.CompanyAssetsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CompanyAssetsViewModel(
    private val companyAssetsUseCase: CompanyAssetsUseCase
): ViewModel() {
    private val _state = MutableStateFlow(CompanyAssetsState())
    val state = _state
        .onStart {
            onFetchData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CompanyAssetsState()
        )

    private fun onFetchData()= viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        companyAssetsUseCase(). onSuccess {
            data -> println("companyAssets $data")
            _state.update {
                it.copy(
                    isLoading = false,
                    companyAssetsList = data
                )
            }
        }

    }
}