package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("Id") val id: Int,
    @SerializedName("Token") val token: String,
    @SerializedName("Roles") val roles: List<String>
)
