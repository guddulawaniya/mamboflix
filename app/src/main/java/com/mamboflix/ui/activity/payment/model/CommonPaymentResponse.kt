package com.mamboflix.ui.activity.payment.model

import com.google.gson.annotations.SerializedName

 open class CommonPaymentResponse<T> {

    @SerializedName("data")
    val mdata: T? = null

    @SerializedName("resultcode")
    val resultcode: String? = null

    @SerializedName("result")
    val result: String? = null

    @SerializedName("message")
    val message: String? = null
}