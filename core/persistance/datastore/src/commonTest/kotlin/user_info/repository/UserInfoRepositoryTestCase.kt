package user_info.repository

import com.gurkha.hr.datastore.user_info.repository.FakeLocalUserInfoRepository
import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.model.user_info.UserInfo
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UserInfoRepositoryTestCase : KoinTest {

    val repository: UserInfoRepository by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    singleOf(::FakeLocalUserInfoRepository).bind<UserInfoRepository>()
                }
            )
        }
    }

    @Test
    fun `initial userInfoFlow emits default UserInfo`() = runTest {
        val userInfo = repository.userInfoFlow.first()
        userInfo shouldBe UserInfo()
    }

    @Test
    fun `saveUserInfo updates the userInfoFlow`() = runTest {
        val newUser = UserInfo(isFirstTime = true, userThemeMode = 0, langCode = "en")
        repository.saveUserInfo(newUser)
        val emittedUser = repository.userInfoFlow.first()
        newUser shouldBe emittedUser
    }

    @AfterTest
    fun tearDown() = runTest {
        stopKoin()
    }
}