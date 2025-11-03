package com.gurkha.hr.domain.auth.login.usecase

class FakeClearTokenUseCase {
    var called = false

    suspend operator fun invoke() {
        called = true
    }
}