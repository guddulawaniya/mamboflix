package com.mamboflix.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

//    var BASE_URL =
//        "http://182.76.237.233/~max233testmambo/mamboflixapi/api/"

//172.16.0.238
//    103.54.2.115
//    103.54.2.116


    var BASE_URL =
//        "http://172.16.0.233/~max233testmambo/mamboflixapi/api/"
        "http://103.154.2.116/~max233testmambo/mamboflixapi/api/"

    var client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS).build()
    var retrofit: Retrofit? = null
    var gson = GsonBuilder()
        .setLenient()
        .create()!!
    fun getClient(): Retrofit {
        if (retrofit!=null)
            retrofit=null

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit as Retrofit
    }
}