package com.example.levelupmobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelupmobile.R
import com.example.levelupmobile.viewmodel.LoginViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupmobile.model.LoginUiState


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onGoToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigateToHome.collect {
            onLoginSuccess()
        }
    }
    Box(Modifier.fillMaxSize().padding(16.dp)){
        Login(
            modifier = Modifier.align(Alignment.Center),
            uiState = uiState,
            onEmailChange = viewModel::onEmailChanged,
            onPasswordChange = viewModel::onPasswordChanged,
            onLoginClick = viewModel::onLoginClicked,
            onForgotClick = { TODO() },
            onGoToRegister = onGoToRegister
            )
    }
}

@Composable
fun Login(
    modifier: Modifier,
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotClick: () -> Unit,
    onGoToRegister: () -> Unit
) {
    Column(modifier = modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField(email = uiState.email,onValueChange = onEmailChange, error = null)
        Spacer(modifier = Modifier.padding(4.dp))
        PasswordField(password = uiState.pass,onValueChange = onPasswordChange, error = null)
        Spacer(modifier = Modifier.padding(8.dp))
        ForgotPassword(Modifier.align(Alignment.CenterHorizontally),onClick = onForgotClick)
        Spacer(modifier = Modifier.padding(16.dp))
        LoginButton(isLoading = uiState.isLoading,onClick = onLoginClick)

        if (uiState.loginError != null) {
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = uiState.loginError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))
        GoToRegister(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onGoToRegister
        )
    }
}

@Composable
fun GoToRegister(modifier: Modifier, onClick: () -> Unit) {
    Text(
        text = "¿Aún no tienes cuenta? Regístrate aquí",
        modifier = modifier.clickable(onClick = onClick),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF50AACE)
    )
}
@Composable
fun LoginButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF008AC2),
            disabledContainerColor = Color(0xFF92B9D2),
            contentColor = Color.White,
            disabledContentColor = Color.White
            )
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
        } else {
            Text(text = "Iniciar sesión")
        }
    }
}

@Composable
fun ForgotPassword(modifier: Modifier,onClick: () -> Unit) {
    Text(text = "¿Olvidaste tu contraseña?",
        modifier = modifier.clickable(onClick = onClick),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF50AACE)
    )
}

@Composable
fun PasswordField(
    password: String,
    onValueChange: (String) -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = password,
        onValueChange = onValueChange,
        placeholder = {Text(text="Contraseña")},
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else
                Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
}

@Composable
fun EmailField(
    email: String,
    onValueChange: (String) -> Unit,
    error: String? = null
    ) {

    TextField(
        value = email,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        isError = (error != null),
        supportingText = {
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }
        }
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(painter = painterResource(id = R.drawable.levelup), contentDescription = "Logo", modifier = modifier)
}