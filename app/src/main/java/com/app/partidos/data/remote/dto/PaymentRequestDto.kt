package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PaymentRequestDto(
    @SerializedName("match_id") val matchId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("card_number") val cardNumber: String,
    @SerializedName("card_holder") val cardHolder: String,
    @SerializedName("expiry_date") val expiryDate: String,
    @SerializedName("cvv") val cvv: String
)
