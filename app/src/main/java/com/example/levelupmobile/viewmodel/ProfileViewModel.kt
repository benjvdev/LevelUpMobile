package com.example.levelupmobile.viewmodel

import SessionManager
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupmobile.remote.User
import com.example.levelupmobile.repository.AuthRepository
import com.example.levelupmobile.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.levelupmobile.remote.RetrofitClient

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuthRepository
    private val sessionManager: SessionManager

    // estado para guardar los datos del usuario
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    // evento para navegar al login
    private val _navigateToLogin = MutableSharedFlow<Boolean>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()

    init {
        sessionManager = SessionManager(application)

        val apiService = RetrofitClient.getInstance(application)
        repository = AuthRepositoryImpl(apiService)

        // cargar los datos del usuario al iniciar
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            //llama a la función del repositorio
            val profileData = repository.getProfile()

            if (profileData != null) {
                // si tenemos datos, los mostramos en la UI
                _user.value = profileData
            } else {
                // si getProfile devuelve null (token inválido) forzamos el cierre de sesión
                logout()
            }
        }
    }

    // se llama con el boton cerrar sesión
    fun logout() {
        viewModelScope.launch {
            sessionManager.clearLoginState()
            _navigateToLogin.emit(true)
        }
    }
}