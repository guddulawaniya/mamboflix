package com.mamboflix.model

import com.google.gson.annotations.SerializedName

open class CommonDataResponse<T> {

    @SerializedName("data")
    val mdata: T? = null

    @SerializedName("skip_duration")
    val skipduration: String? = null

    @SerializedName("user_data")
    val user_data: T? = null

    @SerializedName("profile")
    val profile: T? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("otp")
    val otp: String? = null

    @SerializedName("type")
    val type: String? = null


    @SerializedName("subscribed")
    val is_subscribed: Int? = null

    @SerializedName("status")
    val status: Int = 0

    @SerializedName("country")
    val country: String? = null

    @SerializedName("country_code")
    val country_code: String? = null


    @SerializedName("login_session")
    val login_session: Int? = null

    @SerializedName("user_status")
    val user_status: Int? = null

}