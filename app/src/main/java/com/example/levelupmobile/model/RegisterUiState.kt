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
    val birthDate: String = "",
    val birthDateError: String? = null,
    val showDatePicker: Boolean = false,
    val isLoading: Boolean = false,
    val registerError: String? = null
    )