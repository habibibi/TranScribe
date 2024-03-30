package com.ikp.transcribe.auth.crypto

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoUtils(private val secretKey: String, private val iv: String) {

    private val transformation = "AES/CBC/PKCS7Padding"

    fun encrypt(text: String): String {
        val cipher = Cipher.getInstance(transformation)
        val secretKeySpec = SecretKeySpec(Base64.decode(secretKey, Base64.DEFAULT), "AES")
        val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encryptedBytes = cipher.doFinal(text.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(encryptedText: String): String {
        val cipher = Cipher.getInstance(transformation)
        val secretKeySpec = SecretKeySpec(Base64.decode(secretKey, Base64.DEFAULT), "AES")
        val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}
