package com.app.partidos.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToRecovery: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onNavigateToHome()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
        
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState.error != null) {
            Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { viewModel.login() },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Ingresar")
        }

        TextButton(onClick = onNavigateToRegister) { Text("Crear Cuenta") }
        TextButton(onClick = onNavigateToRecovery) { Text("Recuperar Contraseña") }
    }
}
