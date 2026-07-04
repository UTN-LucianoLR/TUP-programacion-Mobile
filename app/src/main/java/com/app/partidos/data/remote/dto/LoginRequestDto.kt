package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("Email") val email: String,
    @SerializedName("Password") val password: String
)
