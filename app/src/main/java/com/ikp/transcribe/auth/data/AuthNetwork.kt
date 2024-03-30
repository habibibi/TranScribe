package com.ikp.transcribe.auth.data

import com.ikp.transcribe.auth.model.LoginRequest
import com.ikp.transcribe.auth.model.LoginResponse
import com.ikp.transcribe.auth.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthNetwork {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/token")
    suspend fun checkToken(@Header("Authorization") authToken: String): Response<TokenResponse>
}
