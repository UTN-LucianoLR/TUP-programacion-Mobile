package com.app.partidos.data.mapper

import com.app.partidos.data.local.entity.UsuarioEntity
import com.app.partidos.domain.model.Usuario

fun UsuarioEntity.toDomain(): Usuario = Usuario(
    id = id,
    nombre = nombre,
    email = email,
    password = passwordHash
)