package com.app.partidos.data.repository

import com.app.partidos.data.local.entity.CompraEntity
import com.app.partidos.data.local.entity.PartidoEntity
import com.app.partidos.data.local.entity.UsuarioEntity
import com.app.partidos.data.remote.dto.MatchDto
import com.app.partidos.data.remote.dto.StadiumDto
import com.app.partidos.data.remote.dto.TeamDto
import com.app.partidos.domain.model.Compra
import com.app.partidos.domain.model.Partido
import com.app.partidos.domain.model.Usuario

fun UsuarioEntity.toDomain(): Usuario = Usuario(
    id = id,
    nombre = nombre,
    email = email,
    password = passwordHash
)

fun MatchDto.toEntity(homeTeam: TeamDto?, awayTeam: TeamDto?, stadium: StadiumDto?): PartidoEntity {
    val venueName = stadium?.stadiumName ?: "Estadio a confirmar"
    val capacidad = stadium?.capacity ?: 50000

    return PartidoEntity(
        id = idMatch.toString(),
        equipoLocal = homeTeam?.name ?: "Por definirse",
        codigoLocal = homeTeam?.code ?: "N/A",
        equipoVisitante = awayTeam?.name ?: "Por definirse",
        codigoVisitante = awayTeam?.code ?: "N/A",
        fecha = date ?: "Fecha a confirmar",
        hora = time ?: "Hora a confirmar",
        estadio = venueName,
        precio = 100.0,
        entradasDisponibles = capacidad,
        dateIso = date ?: ""
    )
}

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

fun CompraEntity.toDomain(): Compra = Compra(
    id = id,
    usuarioId = usuarioId,
    partidoId = partidoId,
    cantidadEntradas = cantidadEntradas,
    total = total,
    fechaCompra = fechaCompra
)
