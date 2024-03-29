package com.ikp.transcribe.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ikp.transcribe.auth.data.AuthRepository
import com.ikp.transcribe.auth.data.CryptoUtils

class LoginViewModelFactory(
    private val context: LoginActivity,
    private val cryptoUtils: CryptoUtils
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                authRepository = AuthRepository(context, cryptoUtils)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
