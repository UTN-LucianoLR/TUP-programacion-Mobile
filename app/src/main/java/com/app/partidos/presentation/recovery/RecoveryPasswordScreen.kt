package com.app.partidos.presentation.recovery

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RecoveryPasswordScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecoveryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF101F3D)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Recuperar Contraseña",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Te enviaremos un correo con las instrucciones para restablecer tu contraseña.",
                color = Color.White
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("Email registrado") },
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

            if (uiState.error != null) {
                Text(uiState.error!!, color = Color.Yellow, modifier = Modifier.padding(top = 8.dp))
            }

            if (uiState.isSuccess) {
                Text("¡Correo enviado con éxito!", color = Color.Green, modifier = Modifier.padding(top = 8.dp))
            }

            Button(
                onClick = { viewModel.recuperarPassword() },
                enabled = !uiState.isLoading && !uiState.isSuccess,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE63946))
            ) {
                Text("Enviar Instrucciones")
            }

            TextButton(onClick = onNavigateBack, modifier = Modifier.padding(top = 8.dp)) {
                Text("Volver al Login", color = Color.White)
            }
        }
    }
}
