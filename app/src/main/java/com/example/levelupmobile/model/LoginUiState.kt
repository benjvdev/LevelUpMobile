package com.example.levelupmobile.model

data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val loginError: String? = null,
)