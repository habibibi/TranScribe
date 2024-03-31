package com.ikp.transcribe.auth.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikp.transcribe.R
import com.ikp.transcribe.auth.data.AuthRepository
import com.ikp.transcribe.auth.data.Result
import com.ikp.transcribe.auth.model.LoginFormState
import com.ikp.transcribe.auth.model.LoginResult

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    suspend fun login(email: String, password: String) {
        val result = authRepository.login(email, password)
        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = email)
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    suspend fun checkToken(): Result<String> {
        return authRepository.checkToken()
    }

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotBlank()
    }
}
