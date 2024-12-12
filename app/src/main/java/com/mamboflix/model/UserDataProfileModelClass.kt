package com.mamboflix.model

data class UserDataProfileModelClass(
    val `data`: UserDataProfileData,
    val message: String,
    val profile: String,
    val status: Int
)

data class UserDataProfileData(
    val account_type: String,
    val country: String,
    val country_code: String,
    val created_at: String,
    val device_id: Int,
    val device_name: String,
    val device_token: String,
    val device_type: String,
    val email: String,
    val email_verified_at: Any,
    val facebook_id: String,
    val google_id: String,
    val id: Int,
    val is_complete: Int,
    val is_subscribed: Int,
    val login_counter: Int,
    val login_date: Any,
    val mobile: Long,
    val name: String,
    val otp: Int,
    val password: String,
    val profile_image: String,
    val remember_token: String,
    val status: Int,
    val type: String,
    val updated_at: String,
    val watch_id: Int
)