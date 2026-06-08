package com.app.partidos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.partidos.data.local.dao.CompraDao
import com.app.partidos.data.local.dao.PartidoDao
import com.app.partidos.data.local.dao.UsuarioDao
import com.app.partidos.data.local.entity.CompraEntity
import com.app.partidos.data.local.entity.PartidoEntity
import com.app.partidos.data.local.entity.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class, PartidoEntity::class, CompraEntity::class],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun partidoDao(): PartidoDao
    abstract fun compraDao(): CompraDao
}
