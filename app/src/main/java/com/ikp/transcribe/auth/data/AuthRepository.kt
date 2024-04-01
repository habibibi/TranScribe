package com.ikp.transcribe.auth.data

import android.content.Context
import com.ikp.transcribe.auth.model.LoginRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

class AuthRepository(context: Context) {
    // Storing data
    private val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val emailKey = "email"
    private val tokenKey = "token"

    // Fetching data
    private val baseUrl =
        "https://pbd-backend-2024.vercel.app"
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl)
        .build()
    private val authNetwork: AuthNetwork by lazy {
        retrofit.create(AuthNetwork::class.java)
    }

    // Storing Data
    private fun saveCredentials(email: String, token: String) {
        sharedPreferences.edit().putString(emailKey, email).apply()
        sharedPreferences.edit().putString(tokenKey, token).apply()
    }
    private fun getToken(): String? {
        return sharedPreferences.getString(tokenKey, null)
    }

    // Fetching data
    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val request = LoginRequest(email, password)
            val response = authNetwork.login(request)
            val token = response.body()?.token
            if (token != null) {
                saveCredentials(email, token)
                Result.Success(email)
            } else {
                Result.Error(NullPointerException("Token is null"))
            }
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }
    suspend fun checkToken(): Result<String> {
        return try {
            val token = getToken()
            val bearerToken = "Bearer $token"
            val response = bearerToken.let { authNetwork.checkToken(it) }
            val exp = response.body()?.exp
            if (exp != null) {
                val currentTimeSeconds = System.currentTimeMillis() / 1000
                if (currentTimeSeconds < exp) {
                    Result.Success("Token has not expired")
                } else {
                    Result.Error(IOException("Token has expired"))
                }
            } else {
                Result.Error(IOException("Expiration time is null"))
            }
        } catch (e: Throwable) {
            Result.Error(IOException("Error checking token", e))
        }
    }
}
