package com.ikp.transcribe.auth.crypto

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.KeyGenerator

object CryptoConstants {
    private const val SECRET_KEY_LENGTH_BITS = 256
    private const val IV_LENGTH_BYTES = 16

    fun generateSecretKey(): String {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(SECRET_KEY_LENGTH_BITS)
        val keyBytes = keyGenerator.generateKey().encoded
        return Base64.encodeToString(keyBytes, Base64.DEFAULT)
    }

    fun generateIV(): String {
        val random = SecureRandom()
        val iv = ByteArray(IV_LENGTH_BYTES)
        random.nextBytes(iv)
        return Base64.encodeToString(iv, Base64.DEFAULT)
    }
}
