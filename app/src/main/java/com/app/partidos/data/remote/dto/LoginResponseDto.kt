package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName(value = "Id", alternate = ["id"]) val id: Int,
    @SerializedName(value = "Token", alternate = ["token"]) val token: String,
    @SerializedName(value = "Roles", alternate = ["roles"]) val roles: List<String>
)
