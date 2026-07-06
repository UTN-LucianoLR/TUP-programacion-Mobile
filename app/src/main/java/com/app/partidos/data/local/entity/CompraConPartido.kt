package com.app.partidos.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CompraConPartido(
    @Embedded val compra: CompraEntity,
    @Relation(
        parentColumn = "partidoId",
        entityColumn = "id"
    )
    val partido: PartidoEntity?
)
