package com.mamboflix.ui.activity.payment.model

import com.google.gson.annotations.SerializedName

data class UnsubscribResponse(
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : UnsubscribResponseData?   = UnsubscribResponseData()
)
data class UnsubscribResponseData(
    @SerializedName("is_subscribed" ) var isSubscribed : Int? = null
)