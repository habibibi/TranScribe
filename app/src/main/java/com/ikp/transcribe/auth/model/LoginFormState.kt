package com.ikp.transcribe.auth.model

data class LoginFormState (
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
