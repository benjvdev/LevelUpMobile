package com.example.levelupmobile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.levelupmobile.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel /* @Inject constructor(private val repository: AuthRepository) */ : ViewModel() {

    // --- EL ESTADO (STATEFLOW) ---

    // 1. MutableStateFlow (Privado): Solo el ViewModel puede *modificar* el estado.
    private val _uiState = MutableStateFlow(LoginUiState())

    // 2. StateFlow (Público): La UI solo puede *leer* el estado. Es inmutable.
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    // --- LOS EVENTOS (FUNCIONES) ---

    // La UI llamará a estas funciones para notificar acciones del usuario.

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
        // Aquí iría la lógica de login, ej:
        // viewModelScope.launch {
        //     _uiState.update { it.copy(isLoading = true) }
        //     val result = repository.login(uiState.value.email, uiState.value.pass)
        //     // ...manejar resultado (éxito o error)
        //     _uiState.update { it.copy(isLoading = false) }
        // }
    }
}