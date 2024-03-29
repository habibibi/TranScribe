package com.ikp.transcribe.auth.model

data class TokenResponse (
    val nim: String,
    val iat: Long,
    val exp: Long
)
