package com.example.levelupmobile.model

data class RegisterUiState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val address: String = "",
    val addressError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val pass: String = "",
    val passError: String? = null,
    val confirmPass: String = "",
    val confirmPassError: String? = null,
    val isLoading: Boolean = false,
    val registerError: String? = null
    )