package com.fomaxtro.core.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object Encryptor {
    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE

    private const val GCM_TAG_LENGTH = 128
    private const val GCM_IV_LENGTH = 12

    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

    private const val KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "com.fomaxtro.lazypizza.encryption_key"

    private val keystore = KeyStore.getInstance(KEYSTORE).apply {
        load(null)
    }

    fun encrypt(bytes: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }

        val iv = cipher.iv
        val encrypted = cipher.doFinal(bytes)

        return iv + encrypted
    }

    fun decrypt(bytes: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)

        val iv = bytes.copyOfRange(0, GCM_IV_LENGTH)
        val encryptedText = bytes.copyOfRange(GCM_IV_LENGTH, bytes.size)
        val paramSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)

        cipher.init(Cipher.DECRYPT_MODE, getKey(), paramSpec)

        return cipher.doFinal(encryptedText)
    }

    private fun getKey(): SecretKey {
        val existingKey = keystore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry

        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM, KEYSTORE)
            .apply {
                init(
                    KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(BLOCK_MODE)
                        .setEncryptionPaddings(PADDING)
                        .setRandomizedEncryptionRequired(true)
                        .setUserAuthenticationRequired(false)
                        .build()
                )
            }
            .generateKey()
    }
}