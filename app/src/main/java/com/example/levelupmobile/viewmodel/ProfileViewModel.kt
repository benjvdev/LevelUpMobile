package com.example.levelupmobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupmobile.model.User
import com.example.levelupmobile.repository.AppDatabase
import com.example.levelupmobile.repository.AuthRepository
import com.example.levelupmobile.repository.AuthRepositoryImpl
import com.example.levelupmobile.session.SessionManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


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
        val dao = AppDatabase.getInstance(application).userDao()
        repository = AuthRepositoryImpl(dao)

        // cargar los datos del usuario al iniciar
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val email = sessionManager.getLoggedInEmail()
            if (email != null) {
                // Busca en la base de datos
                _user.value = repository.getUserByEmail(email)
            }else {
                logout() // si no hay un email forzamos logout
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