import com.gurkha.hr.components.textField.ErrorStatus
import com.gurkha.hr.domain.form.PasswordValidateUseCase
import com.gurkha.hr.res.SharedRes
import io.kotest.matchers.shouldBe
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.Test

class PTest : KoinTest {
    val useCase: PasswordValidateUseCase by inject()

    companion object {
//        @JvmField
//        @RegisterExtension
//        val koinTestExtension = KoinTestExtension.create {
//            modules(
//            )
//        }
    }

    @Test
    fun test() {
        val password = "StrongP@ssw0rd"
        val result = useCase(password)
        result shouldBe null
    }

    @Test
    fun test1() {
        val password = "weak"
        val result = useCase(password)
        result shouldBe ErrorStatus(true, SharedRes.Strings.invalidPasswordLength)
    }
}