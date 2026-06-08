package com.app.partidos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val email: String,
    val passwordHash: String,
    val estaLogueado: Boolean = false
)
