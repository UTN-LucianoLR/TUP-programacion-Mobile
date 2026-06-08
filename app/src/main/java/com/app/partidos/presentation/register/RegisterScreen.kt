package com.app.partidos.presentation.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreen(
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "Cuenta creada exitosamente. Por favor, iniciá sesión.", Toast.LENGTH_LONG).show()
            onNavigateBack()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = { viewModel.onNombreChanged(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
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
            onClick = { viewModel.registrar() },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Registrarse")
        }

        TextButton(onClick = onNavigateBack) { Text("Volver al Login") }
    }
}
