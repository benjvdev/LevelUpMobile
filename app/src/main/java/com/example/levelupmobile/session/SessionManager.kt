import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences("MyAppSession", Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "USER_TOKEN"
        const val USER_EMAIL = "USER_EMAIL"
    }

    //Se llama en el Login
    fun saveLoginSession(token: String, email: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    //se llama en el logout
    fun clearLoginState() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(USER_EMAIL)
        editor.apply()
    }

    //se usa en MainActivity para saber si mostrar Login o Home
    fun isLoggedIn(): Boolean {
        // Si hay un token guardado, el usuario está logueado
        return prefs.getString(USER_TOKEN, null) != null
    }

    fun getLoggedInEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    //función para que el interceptor lea el token
    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}