package com.ikp.transcribe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ikp.transcribe.AuthApplication
import com.ikp.transcribe.data.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface AuthUiState {
    data class Success(val message: String) : AuthUiState
    data object Error : AuthUiState
    data object Loading : AuthUiState
}

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private var authUiState: AuthUiState by mutableStateOf(AuthUiState.Loading)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authUiState = AuthUiState.Loading
            authUiState = try {
                val response = authRepository.login(email, password)
                if (response.isSuccessful) {
                    AuthUiState.Success("Login successful")
                } else {
                    AuthUiState.Error
                }
            } catch (e: IOException) {
                AuthUiState.Error
            } catch (e: HttpException) {
                AuthUiState.Error
            }
        }
    }

    fun checkToken(token: String) {
        viewModelScope.launch {
            authUiState = AuthUiState.Loading
            authUiState = try {
                val response = authRepository.checkToken(token)
                if (response.isSuccessful) {
                    AuthUiState.Success("Token check successful")
                } else {
                    AuthUiState.Error
                }
            } catch (e: IOException) {
                AuthUiState.Error
            } catch (e: HttpException) {
                AuthUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AuthApplication)
                val authRepository = application.container.authRepository
                AuthViewModel(authRepository = authRepository)
            }
        }
    }
}
