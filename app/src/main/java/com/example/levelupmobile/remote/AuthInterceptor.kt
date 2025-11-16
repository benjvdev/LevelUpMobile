package com.example.levelupmobile.remote

import SessionManager
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {

    // Usamos el ApplicationContext para evitar fugas de memoria
    private val sessionManager = SessionManager(context.applicationContext)

    override fun intercept(chain: Interceptor.Chain): Response {
        //obtiene la petición original
        val originalRequest = chain.request()

        //obtiene el token guardado
        val token = sessionManager.getToken()

        //si hay un token, añade la cabecera "Authorization"
        val requestBuilder = originalRequest.newBuilder()
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        //construye y procede con la nueva petición
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}