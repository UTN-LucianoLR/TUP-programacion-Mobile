package com.app.partidos.data.repository

import com.app.partidos.data.local.preferences.AuthPreferences
import com.app.partidos.data.remote.AuthApiService
import com.app.partidos.data.remote.dto.LoginRequestDto
import com.app.partidos.data.remote.dto.RegistroRequestDto
import com.app.partidos.domain.model.Usuario
import com.app.partidos.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override suspend fun registrarUsuario(
        nombre: String, apellido: String, email: String, passwordHash: String, telefono: String
    ): Result<Usuario> {
        return try {
            val response = authApiService.registrar(
                RegistroRequestDto(nombre = nombre, apellido = apellido, email = email, password = passwordHash, confirmarPassword = passwordHash)
            )
            if (response.isSuccessful) {
                Result.success(Usuario(id = "0", nombre = nombre, email = email, password = ""))
            } else {
                Result.failure(Exception("Error al registrar: ${response.code()}"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("Error de red: ${e.message}"))
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar al servidor"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, passwordHash: String): Result<Usuario> {
        return try {
            val response = authApiService.login(
                LoginRequestDto(email = email, password = passwordHash)
            )
            authPreferences.saveSession(response.token, response.id)
            Result.success(Usuario(id = response.id.toString(), nombre = "Usuario", email = email, password = ""))
        } catch (e: HttpException) {
            Result.failure(Exception("Credenciales incorrectas o error de servidor"))
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar al servidor"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        authPreferences.clearSession()
    }

    override fun getUsuarioLogueadoFlow(): Flow<Usuario?> {
        return authPreferences.userIdFlow.map { userId ->
            if (userId != null) Usuario(id = userId.toString(), nombre = "User", email = "", password = "") else null
        }
    }

    override suspend fun getUsuarioLogueado(): Usuario? {
        val userId = authPreferences.userIdFlow.firstOrNull()
        return if (userId != null) Usuario(id = userId.toString(), nombre = "User", email = "", password = "") else null
    }

    override suspend fun recuperarPassword(email: String, nombre: String, apellido: String, nuevaPassword: String): Result<Unit> {
        return try {
            val response = authApiService.recuperarPassword(
                com.app.partidos.data.remote.dto.RecuperarPasswordDto(
                    email = email,
                    nombre = nombre,
                    apellido = apellido,
                    nuevaPassword = nuevaPassword
                )
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al recuperar contraseña. Verifique los datos."))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("Error de red: ${e.message}"))
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar al servidor"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
