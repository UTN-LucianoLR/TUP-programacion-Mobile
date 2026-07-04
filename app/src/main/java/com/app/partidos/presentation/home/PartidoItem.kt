package com.app.partidos.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.partidos.domain.model.Partido

@Composable
fun PartidoItem(partido: Partido, isPast: Boolean = false, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = if (isPast) Color.Gray else Color(0xFFE63946)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "${partido.equipoLocal} ⚽ ${partido.equipoVisitante}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Text("🏟 Estadio: ${partido.estadio}", color = Color.White)
            Text("📅 Fecha: ${partido.fecha}", color = Color.White)
            Text("🕒 Hora: ${partido.hora}", color = Color.White)
            if (partido.disponible) {
                Text("🎟 Entradas disponibles: ${partido.entradasDisponibles}", color = Color.White)
            } else {
                Text("❌ Agotado", color = Color.Yellow)
            }
        }
    }
}
