package com.ikp.transcribe.network

import com.ikp.transcribe.model.LoginRequest
import com.ikp.transcribe.model.LoginResponse
import com.ikp.transcribe.model.TokenCheckResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/token")
    suspend fun checkToken(@Header("Authorization") authToken: String): Response<TokenCheckResponse>
}
