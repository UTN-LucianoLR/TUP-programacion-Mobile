package com.app.partidos.presentation.validation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.partidos.domain.model.Compra

@Composable
fun SuccessScreen(
    compra: Compra,
    onNavigateToHome: () -> Unit
) {
    Column(
        modifier = Modifier.padding(32.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Compra Exitosa!", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Text("ID Transacción: ${compra.id}")
        Text("Total abonado: \$${compra.total}")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNavigateToHome, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al Inicio")
        }
    }
}
