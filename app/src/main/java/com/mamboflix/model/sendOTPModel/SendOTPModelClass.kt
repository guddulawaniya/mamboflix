package com.mamboflix.model.sendOTPModel

data class SendOTPModelClass(
    val `data`: SendOTPData,
    val message: String,
    val status: Int
)

data class SendOTPData(
    val otp: String,
    val username: String
)