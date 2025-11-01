package com.example.levelupmobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupmobile.model.RegisterUiState
import com.example.levelupmobile.repository.AppDatabase
import com.example.levelupmobile.repository.AuthRepository
import com.example.levelupmobile.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.levelupmobile.utils.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import android.util.Patterns

class RegisterViewModel(application: Application): AndroidViewModel(application) {
    private val repository: AuthRepository
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    private val _navigateToLogin = MutableSharedFlow<Boolean>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()

    init {
        val dao = AppDatabase.getInstance(application).userDao()
        repository = AuthRepositoryImpl(dao)
    }
    fun onFirstNameChanged(firstName: String) {
        _uiState.update { it.copy(firstName = firstName) }
    }

    fun onLastNameChanged(lastName: String) {
        _uiState.update { it.copy(lastName = lastName) }
    }

    fun onAddressChanged(address: String) {
        _uiState.update { it.copy(address = address) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(pass = password) }
    }

    fun onConfirmPasswordChanged(confirmPass: String) {
        _uiState.update { it.copy(confirmPass = confirmPass) }
    }

    fun onRegisterClicked() {
        _uiState.update { it.copy(
            firstNameError = null,
            lastNameError = null,
            addressError = null,
            emailError = null,
            passError = null,
            confirmPassError = null,
            registerError = null
        )}
        if (!validateFields()) {
            return
        }
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = repository.registerUser(_uiState.value)
            when (result) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false, registerError = null) }
                    _navigateToLogin.emit(true)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        emailError = result.exception.message
                    )}
                }
            }
        }
    }
    private fun validateFields(): Boolean {
        val state = _uiState.value
        var isValid = true

        if (state.firstName.isBlank()) {
            _uiState.update { it.copy(firstNameError = "El nombre es obligatorio") }
            isValid = false
        }

        if (state.lastName.isBlank()) {
            _uiState.update { it.copy(lastNameError = "El apellido es obligatorio") }
            isValid = false
        }

        if (state.email.isBlank()) {
            _uiState.update { it.copy(emailError = "El email es obligatorio") }
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.update { it.copy(emailError = "El formato del email no es válido") }
            isValid = false
        }

        if (state.pass.length < 6) {
            _uiState.update { it.copy(passError = "Mínimo 6 caracteres") }
            isValid = false
        }

        if (state.pass != state.confirmPass) {
            _uiState.update { it.copy(confirmPassError = "Las contraseñas no coinciden") }
            isValid = false
        }
        return isValid
    }
}
