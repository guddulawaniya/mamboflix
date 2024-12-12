package com.mamboflix.ui.activity.purchsedhistory.paymenthistory

import com.google.gson.annotations.SerializedName

class history(
    val id : Int,
    val package_id : Int,
    val user_id : Int,
    val order_id : String,
    val package_name : String,
    val duration : String,
    val package_type : Int,
    val payment_status : String,
    val access_limit : Int,
    val amount : String,
    val expiry_date : String,
    val status : Int,
    val created_at : String,
    val updated_at : String,
    val username : String,
    val description : String,
    @SerializedName("package")
    val package1 : String
)
