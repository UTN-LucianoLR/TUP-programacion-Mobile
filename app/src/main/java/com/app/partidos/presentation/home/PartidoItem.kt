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
import androidx.compose.ui.res.stringResource
import com.app.partidos.R
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
            Text(stringResource(R.string.match_detail_stadium, partido.estadio), color = Color.White)
            Text(stringResource(R.string.match_detail_date, partido.fecha), color = Color.White)
            Text(stringResource(R.string.match_detail_time, partido.hora), color = Color.White)
            if (partido.entradasDisponibles > 0) {
                Text(stringResource(R.string.match_detail_available_tickets, partido.entradasDisponibles), color = Color.White)
            } else {
                Text(stringResource(R.string.match_detail_sold_out), color = Color.Yellow)
            }
        }
    }
}
