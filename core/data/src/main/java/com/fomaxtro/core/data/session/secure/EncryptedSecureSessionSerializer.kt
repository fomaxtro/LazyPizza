package com.fomaxtro.core.data.session.secure

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.fomaxtro.core.data.security.Encryptor
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream


class EncryptedSecureSessionSerializer : Serializer<SecureSessionData> {
    override val defaultValue = SecureSessionData()

    override suspend fun readFrom(input: InputStream): SecureSessionData {
        return try {
            val encryptedBytes = input.readBytes()

            if (encryptedBytes.isEmpty()) {
                return defaultValue
            }

            val decryptedBytes = Encryptor.decrypt(encryptedBytes)

            Json.decodeFromString(decryptedBytes.decodeToString())
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            Timber.tag("EncryptedSecureSessionSerializer").e(e)

            throw CorruptionException("Cannot decrypt or read data", e)
        }
    }

    override suspend fun writeTo(
        t: SecureSessionData,
        output: OutputStream
    ) {
        val encoded = Json.encodeToString(t)
        val encryptedBytes = Encryptor.encrypt(encoded.toByteArray())

        output.write(encryptedBytes)
    }
}