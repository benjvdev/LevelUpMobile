package com.example.levelupmobile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.levelupmobile.model.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

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
        if (validateFields()) {
            // 3. Si es válida, procedemos con la lógica de registro
            // viewModelScope.launch {
            //     _uiState.update { it.copy(isLoading = true) }
            //     // ... llamar al repositorio ...
            // }
        }
        // Si no es válida, la función validateFields() ya habrá actualizado
        // el _uiState con los mensajes de error.
    }
    /**
     * Valida los campos del estado actual y actualiza el uiState con errores si los hay.
     * @return 'true' si todos los campos son válidos, 'false' si hay al menos un error.
     */
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
        }
        // (Aquí podrías añadir una validación de formato de email con Regex)

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
