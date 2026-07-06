package com.app.partidos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partidos_remote_keys")
data class PartidoRemoteKeyEntity(
    @PrimaryKey val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)
