import com.gurkha.hr.domain.form.PasswordValidateUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PasswordUseCaseTest : FunSpec({
    val passwordUseCase = PasswordValidateUseCase()
    test("valid password returns null") {
        val password = "StrongP@ssw0rd"
        val result = passwordUseCase(password)
        result shouldBe null
    }
})