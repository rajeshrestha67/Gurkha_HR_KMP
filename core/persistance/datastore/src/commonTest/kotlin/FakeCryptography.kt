import com.gurkha.hr.crypto.Cryptography
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class FakeCryptography : Cryptography {
    override suspend fun <T> encrypt(
        t: T,
        serializer: KSerializer<T>
    ): ByteArray? {
        val json = Json.Default.encodeToString(serializer, t)
        return "[FAKE_ENCRYPTED]$json".encodeToByteArray()
    }

    override suspend fun <T> decrypt(
        bytes: ByteArray,
        deserializer: KSerializer<T>
    ): T? {
        val content = bytes.decodeToString()
        val json = content.removePrefix("[FAKE_ENCRYPTED]")
        return Json.Default.decodeFromString(deserializer, json)
    }

}