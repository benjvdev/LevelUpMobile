package com.example.levelupmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupmobile.viewmodel.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit // evento para navegar al login
) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    // escucha el evento de logout del ViewModel
    LaunchedEffect(key1 = true) {
        viewModel.navigateToLogin.collect {
            onLogout()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = Color.White) },
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
        // columna principal con scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // mostramos los datos cuando el user sea distinto de null
            if (user != null) {
                Spacer(modifier = Modifier.height(16.dp))
                ProfileInfoRow("Nombres", user!!.firstName)
                ProfileInfoRow("Apellidos", user!!.lastName)
                ProfileInfoRow("Dirección", user!!.address)
                ProfileInfoRow("Email", user!!.email)
                ProfileInfoRow("Contraseña", "**********")

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.logout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF008AC2)
                    )
                ) {
                    Text("Cerrar sesión")
                }

                Spacer(modifier = Modifier.height(16.dp))

            } else {
                // indicador de carga mientras se obtienen los datos
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}