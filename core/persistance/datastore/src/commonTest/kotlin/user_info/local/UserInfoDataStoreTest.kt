package user_info.local

import com.gurkha.hr.crypto.Cryptography
import com.gurkha.hr.crypto.FakeCryptography
import com.gurkha.hr.datastore.user_info.local.UserInfoDataStore
import com.gurkha.model.user_info.UserInfo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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

    val fs = FileSystem.Companion.SYSTEM

    @OptIn(ExperimentalTime::class)
    @BeforeTest
    fun setup() {
        val absolutePath =
            FileSystem.Companion.SYSTEM_TEMPORARY_DIRECTORY / "userinfo_${Random.Default.nextInt()}.db"
        tempFilePath = absolutePath.toString()
        startKoin {
            modules(
                module {
                    single { UserInfoDataStore { tempFilePath } }
                    singleOf(::FakeCryptography).bind<Cryptography>()
                }
            )
        }
    }

    @Test
    fun `Should Save And Read UserInfo Correctly`() = runTest {
        val userInfo = UserInfo(isFirstTime = false, userThemeMode = 1, langCode = "np")
        userInfoDataStore.update(userInfo)
        val actual = userInfoDataStore.userInfoFlow.first()
        actual shouldBe userInfo
    }

    @Test
    fun `Should Save And Read UserInfo Incorrectly`() = runTest {
        val userInfo = UserInfo(isFirstTime = false, userThemeMode = 1, langCode = "np")
        userInfoDataStore.update(userInfo)
        val actual = userInfoDataStore.userInfoFlow.first()
        actual shouldNotBe UserInfo(isFirstTime = false, userThemeMode = 1, langCode = "en")
    }

    @Test
    fun `Should Return Default UserInfo When Empty`() = runTest {
        val actual = userInfoDataStore.userInfoFlow.first()
        actual shouldBe UserInfo()
    }

    @Test
    fun `Should Overwrite Existing UserInfo`() = runTest {
        val first = UserInfo(isFirstTime = true, userThemeMode = 0, langCode = "en")
        userInfoDataStore.update(first)

        val second = UserInfo(isFirstTime = false, userThemeMode = 2, langCode = "np")
        userInfoDataStore.update(second)

        val actual = userInfoDataStore.userInfoFlow.first()
        actual shouldBe second
        actual shouldNotBe first
    }

    @Test
    fun `Should Handle Corrupted Data Gracefully`() = runTest {
        withContext(Dispatchers.IO) {
            val path = tempFilePath.toPath()
            fs.write(path) { writeUtf8("invalid_json_encrypted_data") }
        }

        val actual = userInfoDataStore.userInfoFlow.first()
        actual shouldBe UserInfo()
    }

    @AfterTest
    fun tearDown() = runTest {
        withContext(Dispatchers.IO) {
            val path = tempFilePath.toPath()
            if (fs.exists(path)) fs.delete(path)
        }
        stopKoin()
    }
}