package com.example.levelupmobile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.levelupmobile.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    fun onEmailChanged(email: String) {
        _uiState.update { currentState ->
            currentState.copy(email = email)
        }
    }
    fun onPasswordChanged(password: String) {
        _uiState.update { currentState ->
            currentState.copy(pass = password)
        }
    }

    fun onLoginClicked() {
        // Aquí falta lógica de login
    }
}