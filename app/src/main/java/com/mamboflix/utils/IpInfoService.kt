package com.mamboflix.utils

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface IpInfoService {
    @GET("/json")
    suspend fun getIpInfo(): IpInfoResponse
}

data class IpInfoResponse(
    @SerializedName("country") val country: String
)