package com.app.partidos.presentation.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import com.app.partidos.R

@Composable
fun RegisterScreen(
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, context.getString(R.string.register_success), Toast.LENGTH_LONG).show()
            onNavigateBack()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF101F3D)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.login_create_account),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.LightGray,
                cursorColor = Color.White
            )

            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { 
                    val text = it.replace("\n", "")
                    if (text.length <= 45) viewModel.onNombreChanged(text) 
                },
                label = { Text(stringResource(R.string.register_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            OutlinedTextField(
                value = uiState.apellido,
                onValueChange = { 
                    val text = it.replace("\n", "")
                    if (text.length <= 45) viewModel.onApellidoChanged(text) 
                },
                label = { Text(stringResource(R.string.recovery_last_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { 
                    val text = it.replace("\n", "")
                    if (text.length <= 45) viewModel.onEmailChanged(text) 
                },
                label = { Text(stringResource(R.string.register_email)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { 
                    val text = it.replace("\n", "")
                    if (text.length <= 15) viewModel.onPasswordChanged(text) 
                },
                label = { Text(stringResource(R.string.register_password)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Ver contraseña", tint = Color.LightGray)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            OutlinedTextField(
                value = uiState.confirmarPassword,
                onValueChange = { 
                    val text = it.replace("\n", "")
                    if (text.length <= 15) viewModel.onConfirmarPasswordChanged(text) 
                },
                label = { Text(stringResource(R.string.register_confirm_password)) },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Ver contraseña", tint = Color.LightGray)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            if (uiState.error != null) {
                Text(uiState.error!!, color = Color.Yellow, modifier = Modifier.padding(top = 4.dp))
            }

            Button(
                onClick = { viewModel.registrar() },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE63946))
            ) {
                Text(stringResource(R.string.register_button))
            }

            TextButton(onClick = onNavigateBack) {
                Text(stringResource(R.string.back_to_login), color = Color.White)
            }
        }
    }
}
