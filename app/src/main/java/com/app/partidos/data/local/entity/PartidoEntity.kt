package com.app.partidos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partidos")
data class PartidoEntity(
    @PrimaryKey
    val id: String,
    val equipoLocal: String,
    val codigoLocal: String,
    val equipoVisitante: String,
    val codigoVisitante: String,
    val fecha: String,
    val hora: String,
    val estadio: String,
    val precio: Double,
    val entradasDisponibles: Int,
    val dateIso: String,
    val cachedAt: Long = System.currentTimeMillis()
)
