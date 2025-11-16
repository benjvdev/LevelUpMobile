package com.example.levelupmobile.remote

import com.google.gson.annotations.SerializedName

//data classes que coinciden con el backend
//@SerializedName si los nombres de Kotlin y JSON no coinciden
data class Product(
    @SerializedName("id_producto")
    val id: Long,
    val image: String,
    @SerializedName("nombre")
    val name: String,
    @SerializedName("descripcion")
    val description: String,
    @SerializedName("precio")
    val price: Double,
    @SerializedName("categoria")
    val category: String,
    val disponible: Boolean
)
data class RegisterRequest(
    val nombres: String,
    val apellidos: String,
    val email: String,
    val password: String,
    val rol: String,
    val fechaNacimiento: String,
    val direccion: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: UserPayload
)

data class UserPayload(
    val rol: String,
    val nombreUsuario: String,
    val email: String
)

data class User(
    @SerializedName("id_usuario")
    val id: Long,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val rol: String,
    val fechaNacimiento: String,
    val direccion: String
)