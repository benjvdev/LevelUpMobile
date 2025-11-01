package com.example.levelupmobile.repository

import com.example.levelupmobile.model.RegisterUiState
import com.example.levelupmobile.model.User
import com.example.levelupmobile.utils.Result
interface AuthRepository {
    // Usamos RegisterUiState para pasar todos los datos
    suspend fun registerUser(state: RegisterUiState): Result<Long>
    suspend fun loginUser(email: String, pass: String): Result<User>
}


class AuthRepositoryImpl(
    private val dao: UserDao
) : AuthRepository {

    override suspend fun registerUser(state: RegisterUiState): Result<Long> {
        return try {
            val newUser = User(
                firstName = state.firstName,
                lastName = state.lastName,
                address = state.address,
                email = state.email,
                password = state.pass
            )
            val newUserId = dao.registerUser(newUser)
            Result.Success(newUserId)

        } catch (e: Exception) {
            Result.Error(Exception("El email ya está registrado.", e))
        }
    }

    override suspend fun loginUser(email: String, pass: String): Result<User> {
        return try {
            val user = dao.login(email, pass)
            if (user != null) {
                Result.Success(user)
            } else {
                throw Exception("Email o contraseña incorrectos.")
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}