package com.example.levelupmobile.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //Productos (Público)
    @GET("productos")
    suspend fun getProducts(): List<Product>

    //Buscar productos (Público)
    @GET("productos/search")
    suspend fun searchProducts(@Query("query") query: String): List<Product>

    //Autenticación (Público)
    @POST("usuarios/registro")
    suspend fun register(@Body request: RegisterRequest): String
    //Login (Público)
    @POST("usuarios/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    //Perfil (Privado)
    @GET("usuarios/perfil")
    suspend fun getProfile(): User
}