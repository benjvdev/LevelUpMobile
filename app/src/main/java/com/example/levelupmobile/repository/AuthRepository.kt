package com.example.levelupmobile.repository

import android.util.Log
import com.example.levelupmobile.model.RegisterUiState
import com.example.levelupmobile.remote.ApiService
import com.example.levelupmobile.remote.LoginRequest
import com.example.levelupmobile.remote.LoginResponse
import com.example.levelupmobile.remote.RegisterRequest
import com.example.levelupmobile.remote.User
import com.example.levelupmobile.utils.Result
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface AuthRepository {
    // usamos RegisterUiState para pasar todos los datos
    suspend fun registerUser(state: RegisterUiState): Result<Long>
    suspend fun loginUser(email: String, pass: String): Result<LoginResponse>
    suspend fun getProfile(): User?
}


class AuthRepositoryImpl(
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun registerUser(state: RegisterUiState): Result<Long> {
        val backendFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val uiFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        val localDate = LocalDate.parse(state.birthDate, uiFormatter)

        val request = RegisterRequest(
            nombres = state.firstName,
            apellidos = state.lastName,
            email = state.email,
            password = state.pass,
            rol = "USER",
            fechaNacimiento = localDate.format(backendFormatter),
            direccion = state.address
        )

        return try {
            //llama a 'register' si es 200 OK, devuelve el string y sigue
            val responseBody = apiService.register(request)
            Log.d("AuthRepositoryImpl", "Registro exitoso: $responseBody")
            Result.Success(1L) //exito

        } catch (e: Exception) {
            //si es 400, 500, etc, será una HttpException
            if (e is HttpException) {
                //leemos el mensaje de error de Spring
                val errorMsg = e.response()?.errorBody()?.string() ?: "Error de registro"
                Log.e("AuthRepositoryImpl", "Error HTTP en Registro: $errorMsg")
                Result.Error(Exception(errorMsg))
            } else {
                // error de red (sin conexión, CLEARTEXT, etc)
                Log.e("AuthRepositoryImpl", "Error de RED en Registro", e)
                Result.Error(Exception("Error de red: ${e.message}"))
            }
        }
    }

    override suspend fun loginUser(email: String, pass: String): Result<LoginResponse> {
        val request = LoginRequest(email = email, password = pass)
        return try {
            val response = apiService.login(request)
            Result.Success(response)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Error en Login", e)

            Result.Error(Exception("Email o contraseña incorrectos"))
        }
    }

    override suspend fun getProfile(): User? {
        return try {
            apiService.getProfile()
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Error al obtener perfil", e)
            null
        }
    }
}