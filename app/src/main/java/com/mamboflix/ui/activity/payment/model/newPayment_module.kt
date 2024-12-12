package com.mamboflix.ui.activity.payment.model

data class newPayment_module(
    val amount: String,
    val `data`: String,
    val message: String,
    val order_id: String,
    val status: Int
)