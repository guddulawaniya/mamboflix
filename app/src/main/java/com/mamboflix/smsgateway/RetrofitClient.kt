package com.mamboflix.smsgateway

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://gw.selcommobile.com:8443/bin/"
    var loggingInterceptor = HttpLoggingInterceptor()
    private var okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY))
        .connectTimeout((60 * 5).toLong(), TimeUnit.SECONDS)
        .callTimeout(2, TimeUnit.MINUTES)
    .readTimeout((60 * 5).toLong(), TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)


    /*val loggingInterceptor = HttpLoggingInterceptor()

    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val builder = OkHttpClient.Builder()
        .connectTimeout((60 * 7).toLong(), TimeUnit.SECONDS)
        .readTimeout((60 * 7).toLong(), TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)*/

        .addInterceptor { chain ->

            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .addHeader("Content-Type", "application/json")
                .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()





    val instance: Api by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .addCallAdapterFactory(
                RxJavaCallAdapterFactory.create())
            .build()
        retrofit.create(Api::class.java)
    }
}