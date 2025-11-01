package com.example.levelupmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelupmobile.ui.screens.HomeScreen
import com.example.levelupmobile.ui.screens.LoginScreen
import com.example.levelupmobile.ui.screens.RegisterScreen
import com.example.levelupmobile.ui.theme.LevelUpMobileTheme
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.levelupmobile.ui.screens.CartScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LevelUpMobileTheme {

                // se crea el NavController
                val navController = rememberNavController()
                //obtener la ruta actual para mantener sincronizada la BottomBar
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // crear el NavHost
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
                                navController.popBackStack() //volver al login cuando el usuario se registra
                            }
                        )
                    }

                    composable(route = "home") {
                        HomeScreen(
                            onNavigateToSearch = { navController.navigate("search") },
                            navController = navController,
                            currentRoute = currentRoute
                        )
                    }
                    composable(route = "cart") {
                        CartScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
