package com.app.partidos.data.mapper

import com.app.partidos.data.local.entity.PartidoEntity
import com.app.partidos.domain.model.Partido

fun PartidoEntity.toDomain(): Partido = Partido(
    id = id,
    equipoLocal = equipoLocal,
    codigoLocal = codigoLocal,
    equipoVisitante = equipoVisitante,
    codigoVisitante = codigoVisitante,
    fecha = fecha,
    hora = hora,
    estadio = estadio,
    precio = precio,
    entradasDisponibles = entradasDisponibles
)