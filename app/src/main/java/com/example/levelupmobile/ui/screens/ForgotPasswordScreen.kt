package com.example.levelupmobile.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit
    // añadir un viewModel
) {
    // estado simple
    var email by remember { mutableStateOf("") }

    var messageSent by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Contraseña", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF008AC2)
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (messageSent) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "¡Hecho!",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Si la dirección de correo es correcta, recibirás un email con instrucciones en breve.",
                    textAlign = TextAlign.Center
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ingresa tu email y te enviaremos un enlace para recuperar tu contraseña.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                EmailField(
                    email = email,
                    onValueChange = { email = it },
                    error = null
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        messageSent = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF008AC2)
                    )
                ) {
                    Text("Enviar enlace")
                }
            }
        }
    }
}