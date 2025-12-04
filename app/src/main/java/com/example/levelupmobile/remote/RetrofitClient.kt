package com.example.levelupmobile.remote

import android.content.Context
import com.example.levelupmobile.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    // ip para apuntar al localhost(xampp) desde el emulador o desde la ip del dispositivo
    private const val BASE_URL = "http://192.168.1.2:8080/"

    //instancia 'lazy' para que solo se cree una vez
    @Volatile
    private var apiService: ApiService? = null

    fun getInstance(context: Context): ApiService {
        // doble-check para seguridad en hilos (singleton)
        return apiService ?: synchronized(this) {
            apiService ?: buildApiService(context).also { apiService = it }
        }
    }
    private fun buildApiService(context: Context): ApiService {
        //para ver llamadas en Logcat
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        //interceptor de autenticación
        val authInterceptor = AuthInterceptor(context.applicationContext)

        //cliente de OkHttp que usa ambos interceptors
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        //constructor de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient) // usa el nuevo cliente
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}