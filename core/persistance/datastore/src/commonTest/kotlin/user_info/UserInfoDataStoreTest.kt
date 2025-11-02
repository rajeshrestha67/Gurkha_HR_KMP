package user_info

import com.gurkha.hr.datastore.user_info.local.UserInfoDataStore
import com.gurkha.hr.datastore.user_info.repository.LocalUserInfoRepository
import com.gurkha.hr.datastore.user_info.repository.UserInfoRepository
import com.gurkha.model.user_info.UserInfo
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.ExperimentalTime

class UserInfoDataStoreTest : KoinTest {
    lateinit var tempFilePath: String
    val userInfoDataStore: UserInfoDataStore by inject()
    val repository: UserInfoRepository by inject()
    val fs = FileSystem.SYSTEM
    private val testScope = TestScope()

    @OptIn(ExperimentalTime::class)
    @BeforeTest
    fun setup() {
        val absolutePath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "userinfo_${Random.nextInt()}.db"
        tempFilePath = absolutePath.toString()

        startKoin {
            modules(
                module {
                    single { UserInfoDataStore { tempFilePath } }
                    single<UserInfoRepository> { LocalUserInfoRepository(get()) }
                }
            )
        }
    }

    @Test
    fun shouldSaveAndReadUserInfoCorrectly() = testScope.runTest {
        val userInfo = UserInfo(isFirstTime = false, userThemeMode = 1, langCode = "np")
        userInfoDataStore.update(userInfo)
        val actual = repository.userInfo.first()
        actual shouldBe userInfo
    }

    @AfterTest
    fun tearDown() = testScope.runTest {
        withContext(Dispatchers.IO) {
            val path = tempFilePath.toPath()
            if (fs.exists(path)) fs.delete(path)
        }
        stopKoin()
    }
}