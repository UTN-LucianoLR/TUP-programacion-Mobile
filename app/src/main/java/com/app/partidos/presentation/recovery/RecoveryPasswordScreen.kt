package com.app.partidos.presentation.recovery

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.app.partidos.R

@Composable
fun RecoveryPasswordScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecoveryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val isFormValid = uiState.email.isNotBlank() &&
            uiState.nombre.isNotBlank() &&
            uiState.apellido.isNotBlank() &&
            uiState.nuevaPassword.isNotBlank() &&
            uiState.confirmarPassword.isNotBlank()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF101F3D)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.login_recover_password),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.recovery_instructions),
                color = Color.White
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it.replace("\n", "")) },
                label = { Text(stringResource(R.string.recovery_email)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.onNombreChanged(it.replace("\n", "")) },
                label = { Text(stringResource(R.string.recovery_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = uiState.apellido,
                onValueChange = { viewModel.onApellidoChanged(it.replace("\n", "")) },
                label = { Text(stringResource(R.string.recovery_last_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = uiState.nuevaPassword,
                onValueChange = { viewModel.onNuevaPasswordChanged(it.replace("\n", "")) },
                label = { Text(stringResource(R.string.recovery_new_password)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Ver contraseña", tint = Color.LightGray)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = uiState.confirmarPassword,
                onValueChange = { viewModel.onConfirmarPasswordChanged(it.replace("\n", "")) },
                label = { Text(stringResource(R.string.recovery_confirm_password)) },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Ver contraseña", tint = Color.LightGray)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White
                )
            )

            if (uiState.error != null) {
                Text(uiState.error!!, color = Color.Yellow, modifier = Modifier.padding(top = 8.dp))
            }

            if (uiState.isSuccess) {
                Text(stringResource(R.string.recovery_success), color = Color.Green, modifier = Modifier.padding(top = 8.dp))
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000)
                    onNavigateBack()
                }
            }

            Button(
                onClick = { viewModel.recuperarPassword() },
                enabled = isFormValid && !uiState.isLoading && !uiState.isSuccess,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE63946))
            ) {
                Text(stringResource(R.string.recovery_send_instructions))
            }

            TextButton(onClick = onNavigateBack, modifier = Modifier.padding(top = 8.dp)) {
                Text(stringResource(R.string.back_to_login), color = Color.White)
            }
        }
    }
}
