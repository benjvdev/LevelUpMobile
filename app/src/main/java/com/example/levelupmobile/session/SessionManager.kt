package com.example.levelupmobile.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    // archivo de preferencias
    private var prefs: SharedPreferences =
        context.getSharedPreferences("MyAppSession", Context.MODE_PRIVATE)

    companion object {
        const val USER_LOGGED_IN = "USER_LOGGED_IN"
        const val USER_EMAIL = "USER_EMAIL"
    }


    // se llama cuando el usuario inicia sesión exitosamente
    fun saveLoginState(email: String) {
        val editor = prefs.edit()
        editor.putBoolean(USER_LOGGED_IN, true)
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    // se llama para cerrar sesión
    fun clearLoginState() {
        val editor = prefs.edit()
        editor.putBoolean(USER_LOGGED_IN, false)
        editor.putString(USER_EMAIL, null)
        editor.apply()
    }


    //se llama desde MainActivity para decidir la pantalla inicial
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(USER_LOGGED_IN, false)
    }
    // obtener el email del usuario logueado
    fun getLoggedInEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }
}