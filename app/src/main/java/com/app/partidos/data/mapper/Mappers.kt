package com.app.partidos.data.mapper

import com.app.partidos.data.local.entity.PartidoEntity
import com.app.partidos.data.remote.dto.PartidoDto


fun PartidoDto.toEntity(): PartidoEntity {
    val fechaStr = this.fecha ?: "Fecha a confirmar"
    val horaStr = this.hora ?: "Hora a confirmar"
    val eqLocal = this.equipoLocal ?: "Local"
    val eqVis = this.equipoVisitante ?: "Visitante"

    return PartidoEntity(
        id = this.id.toString(),
        equipoLocal = eqLocal,
        codigoLocal = eqLocal.take(3).uppercase(),
        equipoVisitante = eqVis,
        codigoVisitante = eqVis.take(3).uppercase(),
        fecha = fechaStr,
        hora = horaStr,
        estadio = this.estadioNombre ?: "A confirmar",
        precio = 100.0,
        entradasDisponibles = 10000,
        dateIso = "$fechaStr $horaStr"
    )
}




