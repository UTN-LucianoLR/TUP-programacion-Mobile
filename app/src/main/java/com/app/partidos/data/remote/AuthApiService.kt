package com.app.partidos.data.remote

import com.app.partidos.data.remote.dto.LoginRequestDto
import com.app.partidos.data.remote.dto.LoginResponseDto
import com.app.partidos.data.remote.dto.RegistroRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @POST("api/auth/registrar")
    suspend fun registrar(@Body request: RegistroRequestDto): Response<Unit>

    @POST("api/auth/recuperar-password")
    suspend fun recuperarPassword(@Body request: com.app.partidos.data.remote.dto.RecuperarPasswordDto): Response<Unit>
}
