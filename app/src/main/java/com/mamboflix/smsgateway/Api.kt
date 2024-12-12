package com.mamboflix.smsgateway

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import rx.Observable

interface  Api {


    @POST("SendSmsMessage")
    fun callloginSmsAPI(@Body request: JsonObject): Observable<CommonSmsResponse>


    @GET("send.json")
    fun callSmsAPI(
            @Query("USERNAME")  USERNAME :String,
            @Query("PASSWORD")  PASSWORD:String,
            @Query("DESTADDR")  DESTADDR:String,
            @Query("MESSAGE")  MESSAGE:String): Observable<CommonSmsResponse>

}