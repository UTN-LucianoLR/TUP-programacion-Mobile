package com.app.partidos.presentation.recovery

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RecoveryPasswordScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecoveryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Recuperar Contraseña", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Te enviaremos un correo con las instrucciones para restablecer tu contraseña.")

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Email registrado") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        if (uiState.error != null) {
            Text(uiState.error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        if (uiState.isSuccess) {
            Text("¡Correo enviado con éxito!", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
        }

        Button(
            onClick = { viewModel.recuperarPassword() },
            enabled = !uiState.isLoading && !uiState.isSuccess,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Enviar Instrucciones")
        }

        TextButton(onClick = onNavigateBack, modifier = Modifier.padding(top = 8.dp)) { 
            Text("Volver al Login") 
        }
    }
}
