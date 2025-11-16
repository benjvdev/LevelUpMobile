package com.example.levelupmobile

import SessionManager
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
import com.example.levelupmobile.ui.screens.ForgotPasswordScreen
import com.example.levelupmobile.ui.screens.ProfileScreen
import com.example.levelupmobile.ui.screens.SearchScreen

class MainActivity : ComponentActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        enableEdgeToEdge()

        setContent {
            LevelUpMobileTheme {

                // se crea el NavController
                val navController = rememberNavController()

                //obtener la ruta actual para mantener sincronizada la BottomBar
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                //decidir la pantalla inicial
                val startDestination = if (sessionManager.isLoggedIn()) {
                    "home"
                } else {
                    "login"
                }
                // crear el NavHost
                NavHost(
                    navController = navController,
                    startDestination = startDestination // pantalla inicial
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
                            },
                            onForgotClick = {
                                navController.navigate("forgot_password")
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
                            },
                            navController = navController,
                            currentRoute = currentRoute
                        )
                    }
                    composable(route = "profile") {
                        ProfileScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            },
                            navController = navController,
                            currentRoute = currentRoute
                        )
                    }
                    composable(route = "forgot_password") {
                        ForgotPasswordScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(route = "search") {
                        SearchScreen(
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
