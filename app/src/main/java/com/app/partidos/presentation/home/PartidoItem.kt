package com.app.partidos.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.partidos.domain.model.Partido

@Composable
fun PartidoItem(partido: Partido, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { onClick() }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${partido.equipoLocal} vs ${partido.equipoVisitante}", style = MaterialTheme.typography.titleLarge)
            Text("Estadio: ${partido.estadio}")
            Text("Fecha: ${partido.fecha} - ${partido.hora}")
            if (partido.disponible) {
                Text("Entradas disponibles: ${partido.entradasDisponibles}", color = MaterialTheme.colorScheme.primary)
            } else {
                Text("Entradas Agotadas", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
