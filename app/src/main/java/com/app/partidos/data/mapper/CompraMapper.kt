package com.app.partidos.data.mapper

import com.app.partidos.data.local.entity.CompraEntity
import com.app.partidos.domain.model.Compra

fun CompraEntity.toDomain(): Compra = Compra(
    id = id,
    usuarioId = usuarioId,
    partidoId = partidoId,
    cantidadEntradas = cantidadEntradas,
    total = total,
    fechaCompra = fechaCompra
)