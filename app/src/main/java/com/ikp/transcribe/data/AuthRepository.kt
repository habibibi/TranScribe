package com.ikp.transcribe.data

import com.ikp.transcribe.model.LoginRequest
import com.ikp.transcribe.model.LoginResponse
import com.ikp.transcribe.model.TokenCheckResponse
import com.ikp.transcribe.network.AuthService
import retrofit2.Response

interface AuthRepository {
    suspend fun login(email: String, password: String): Response<LoginResponse>
    suspend fun checkToken(token: String): Response<TokenCheckResponse>
}

class NetworkAuthRepository(private val authService: AuthService) : AuthRepository {

    override suspend fun login(email: String, password: String): Response<LoginResponse> {
        val request = LoginRequest(email, password)
        return authService.login(request)
    }

    override suspend fun checkToken(token: String): Response<TokenCheckResponse> {
        return authService.checkToken("Bearer $token")
    }
}
