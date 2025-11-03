package login

import com.gurkha.hr.datastore.token.repository.FakeTokenRepository
import com.gurkha.hr.datastore.user_data.repository.FakeUserDataRepository
import com.gurkha.hr.domain.auth.login.repository.FakeUserRemoteRepository
import com.gurkha.hr.domain.auth.login.usecase.FakeClearTokenUseCase
import com.gurkha.hr.domain.auth.login.usecase.LoginUseCase
import com.gurkha.hr.domain.form.FakeEmailValidateUseCase
import com.gurkha.hr.domain.form.FakePasswordValidateUseCase
import com.gurkha.hr.domain.splash.UpdateFirstTimeCheckUseCase
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.BeforeTest

class LoginViewModelTest : KoinTest {
    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    singleOf(::FakeTokenRepository)
                    singleOf(::FakeUserRemoteRepository)
                    singleOf(::FakeUserDataRepository)

                    // UseCases
                    singleOf(::LoginUseCase)
                    singleOf(::FakeClearTokenUseCase)
                    singleOf(::FakeEmailValidateUseCase)
                    singleOf(::FakePasswordValidateUseCase)
                    singleOf(::UpdateFirstTimeCheckUseCase)

                }
            )
        }
    }
}