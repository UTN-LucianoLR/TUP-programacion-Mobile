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

fun com.app.partidos.data.local.entity.CompraConPartido.toDomain(): Compra = Compra(
    id = compra.id,
    usuarioId = compra.usuarioId,
    partidoId = compra.partidoId,
    cantidadEntradas = compra.cantidadEntradas,
    total = compra.total,
    fechaCompra = compra.fechaCompra,
    equipoLocal = partido?.equipoLocal ?: "",
    equipoVisitante = partido?.equipoVisitante ?: "",
    fechaPartido = partido?.fecha ?: ""
)