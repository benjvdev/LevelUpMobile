package com.example.levelupmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelupmobile.ui.screens.HomeScreen
import com.example.levelupmobile.ui.screens.Login
import com.example.levelupmobile.ui.screens.LoginScreen
import com.example.levelupmobile.ui.screens.RegisterScreen
import com.example.levelupmobile.ui.theme.LevelUpMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LevelUpMobileTheme {

                // se crea el NavController
                val navController = rememberNavController()

                // se crea el NavHost
                NavHost(
                    navController = navController,
                    startDestination = "login" // pantalla inicial
                ) {

                    // primera ruta: "login"
                    composable(route = "login") {
                        LoginScreen(
                            // aquí se conecta con la pantalla de registro
                            onGoToRegister = {
                                navController.navigate("register")
                            },
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    // segunda ruta: "register"
                    composable(route = "register") {
                        RegisterScreen(
                            // aquí se conecta con la pantalla de login
                            onLoginClicked = {
                                navController.popBackStack()
                            },
                            onRegisterSuccess = {
                                navController.popBackStack() //navegamos de vuelta al login cuando el usuario se registra
                            }
                        )
                    }

                    composable(route = "home") {
                        HomeScreen(onNavigateToSearch = {})
                    }
                }
            }
        }
    }
}
