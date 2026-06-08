package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Respuesta del endpoint POST /payments.
 * [transactionId] y [purchaseDate] son null cuando [success] es false.
 */
data class PaymentResponseDto(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("transaction_id")
    val transactionId: String?,

    @SerializedName("message")
    val message: String,

    @SerializedName("total_amount")
    val totalAmount: Double,

    @SerializedName("currency")
    val currency: String,

    @SerializedName("purchase_date")
    val purchaseDate: String?
)
