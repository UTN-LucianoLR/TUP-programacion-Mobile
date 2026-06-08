package com.app.partidos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.partidos.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUsuario(usuario: UsuarioEntity) // Ya no devuelve Long porque el ID es String (UUID)

    @Update
    suspend fun updateUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUsuarioByEmail(email: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE estaLogueado = 1 LIMIT 1")
    fun getUsuarioLogueadoFlow(): Flow<UsuarioEntity?>

    @Query("SELECT * FROM usuarios WHERE estaLogueado = 1 LIMIT 1")
    suspend fun getUsuarioLogueado(): UsuarioEntity?

    @Query("UPDATE usuarios SET estaLogueado = 0")
    suspend fun logoutTodos()
}
