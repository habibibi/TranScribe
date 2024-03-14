package com.ikp.transcribe.model

data class TokenCheckResponse(
    val nim: String,
    val iat: Long,
    val exp: Long
)
