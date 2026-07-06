package com.app.partidos.domain.repository

import com.app.partidos.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * Contrato para la gestión de usuarios y autenticación.
 */
interface AuthRepository {
    
    suspend fun registrarUsuario(nombre: String, apellido: String, email: String, passwordHash: String, telefono: String): Result<Usuario>
    
    suspend fun login(email: String, passwordHash: String): Result<Usuario>
    
    suspend fun logout()
    
    fun getUsuarioLogueadoFlow(): Flow<Usuario?>
    
    suspend fun getUsuarioLogueado(): Usuario?

    suspend fun recuperarPassword(email: String, nombre: String, apellido: String, nuevaPassword: String): Result<Unit>
}
