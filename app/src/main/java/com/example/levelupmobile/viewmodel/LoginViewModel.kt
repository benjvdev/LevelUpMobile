package com.example.levelupmobile.viewmodel

import com.example.levelupmobile.session.SessionManager
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupmobile.model.LoginUiState
import com.example.levelupmobile.remote.RetrofitClient
import com.example.levelupmobile.repository.AuthRepository
import com.example.levelupmobile.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.levelupmobile.utils.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel @JvmOverloads constructor(
    application: Application,
    authRepository: AuthRepository? = null,
    sessionManager: SessionManager? = null
) : AndroidViewModel(application) {

    private val repository: AuthRepository
    private val sessionMgr: SessionManager

    init {
        val apiService = RetrofitClient.getInstance(application)
        repository = authRepository ?: AuthRepositoryImpl(apiService)
        sessionMgr = sessionManager ?: SessionManager(application)
    }
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    private val _navigateToHome = MutableSharedFlow<Boolean>()
    val navigateToHome = _navigateToHome.asSharedFlow()
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
        _uiState.update { it.copy(isLoading = true, loginError = null) }

        viewModelScope.launch {
            val state = _uiState.value

            val result = repository.loginUser(state.email, state.pass)

            when (result) {
                is Result.Success -> {
                    val loginResponse = result.data
                    sessionMgr.saveLoginSession(
                        token = loginResponse.token,
                        email = loginResponse.user.email
                    )
                    _uiState.update { it.copy(isLoading = false) }
                    _navigateToHome.emit(true)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        loginError = result.exception.message
                    )}
                }
            }
        }
    }
}