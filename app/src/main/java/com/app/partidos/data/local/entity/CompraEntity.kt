package com.app.partidos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "compras")
data class CompraEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val usuarioId: String,
    val partidoId: String,
    val cantidadEntradas: Int,
    val total: Double,
    val fechaCompra: String
)
