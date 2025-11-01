package com.example.levelupmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.text.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupmobile.model.RegisterUiState
import com.example.levelupmobile.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onLoginClicked: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigateToLogin.collect {
            onRegisterSuccess()
        }
    }
    Box(Modifier.fillMaxSize().padding(16.dp)) {
        Register(
            modifier = Modifier.align(Alignment.Center),
            uiState = uiState,
            onFirstNameChange = viewModel::onFirstNameChanged,
            onLastNameChange = viewModel::onLastNameChanged,
            onAddressChange = viewModel::onAddressChanged,
            onEmailChange = viewModel::onEmailChanged,
            onPasswordChange = viewModel::onPasswordChanged,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChanged,
            onRegisterClick = viewModel::onRegisterClicked,
            onLoginClick = onLoginClicked
        )
    }
}

@Composable
fun Register(
    modifier: Modifier,
    uiState: RegisterUiState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))

        FirstNameField(
            firstName = uiState.firstName,
            onValueChange = onFirstNameChange,
            error = uiState.firstNameError
        )

        LastNameField(
            lastName = uiState.lastName,
            onValueChange = onLastNameChange,
            error = uiState.lastNameError
        )

        AddressField(
            address = uiState.address,
            onValueChange = onAddressChange,
            error = uiState.addressError
        )

        EmailField(
            email = uiState.email,
            onValueChange = onEmailChange,
            error = uiState.emailError
        )

        PasswordField(
            password = uiState.pass,
            onValueChange = onPasswordChange,
            placeholder = "Contraseña",
            error = uiState.passError
        )

        PasswordField(
            password = uiState.confirmPass,
            onValueChange = onConfirmPasswordChange,
            placeholder = "Confirmar contraseña",
            error = uiState.confirmPassError
        )

        Spacer(modifier = Modifier.padding(16.dp))

        RegisterButton(
            isLoading = uiState.isLoading,
            onClick = onRegisterClick
        )
        Spacer(modifier = Modifier.padding(16.dp))

        GoToLogin(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onLoginClick
        )
    }
}
@Composable
fun FirstNameField(
    firstName: String,
    onValueChange: (String) -> Unit,
    error: String? = null
) {
    TextField(
        value = firstName,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Nombres") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words
        ),
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
fun LastNameField(
    lastName: String,
    onValueChange: (String) -> Unit,
    error: String? = null
) {
    TextField(
        value = lastName,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Apellidos") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words
        ),
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
fun AddressField(
    address: String,
    onValueChange: (String) -> Unit,
    error: String? = null
) {
    TextField(
        value = address,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Dirección") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences
        ),
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
fun RegisterButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
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
            Text(text = "Registrarse")
        }
    }
}
@Composable
fun GoToLogin(modifier: Modifier, onClick: () -> Unit) {
    Text(
        text = "¿Ya tienes cuenta? Inicia sesión",
        modifier = modifier.clickable(onClick = onClick),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF50AACE)
    )
}
@Composable
fun PasswordField(
    password: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Contraseña",
    error: String? = null
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = password,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder) },
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
        },
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
