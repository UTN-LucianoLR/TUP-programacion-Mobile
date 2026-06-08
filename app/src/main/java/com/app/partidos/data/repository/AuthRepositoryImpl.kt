package com.app.partidos.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.app.partidos.data.local.dao.UsuarioDao
import com.app.partidos.data.local.entity.UsuarioEntity
import com.app.partidos.domain.model.Usuario
import com.app.partidos.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val usuarioDao: UsuarioDao
) : AuthRepository {

    override suspend fun registrarUsuario(
        nombre: String, apellido: String, email: String, passwordHash: String, telefono: String
    ): Result<Usuario> {
        return try {
            val entity = UsuarioEntity(
                nombre = nombre, // El UUID se genera solo en el id
                email = email,
                passwordHash = passwordHash,
                estaLogueado = false
            )
            usuarioDao.logoutTodos()
            usuarioDao.insertUsuario(entity)
            Result.success(entity.toDomain())
        } catch (e: SQLiteConstraintException) {
            Result.failure(Exception("El email ya está registrado."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, passwordHash: String): Result<Usuario> {
        return try {
            val usuarioEntity = usuarioDao.getUsuarioByEmail(email)
            if (usuarioEntity != null && usuarioEntity.passwordHash == passwordHash) {
                usuarioDao.logoutTodos()
                usuarioDao.updateUsuario(usuarioEntity.copy(estaLogueado = true))
                Result.success(usuarioEntity.toDomain())
            } else {
                Result.failure(Exception("Credenciales incorrectas."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() = usuarioDao.logoutTodos()

    override fun getUsuarioLogueadoFlow(): Flow<Usuario?> = usuarioDao.getUsuarioLogueadoFlow().map { it?.toDomain() }

    override suspend fun getUsuarioLogueado(): Usuario? = usuarioDao.getUsuarioLogueado()?.toDomain()

    override suspend fun recuperarPassword(email: String): Result<Unit> {
        val usuario = usuarioDao.getUsuarioByEmail(email)
        return if (usuario != null) {
            Result.success(Unit) // Simulamos que el correo se envía correctamente
        } else {
            Result.failure(Exception("Email no encontrado en la base de datos local"))
        }
    }
}
