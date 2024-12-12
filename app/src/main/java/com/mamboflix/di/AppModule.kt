package com.mamboflix.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mamboflix.api.ApiService
import com.mamboflix.prefs.UserPref
import com.mamboflix.utils.Constants
import com.mamboflix.utils.Utils

import java.util.concurrent.TimeUnit

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

import retrofit2.converter.gson.GsonConverterFactory




@Module
internal class AppModule {

    @Provides
    fun providesContext(app: Application): Context {
        return app.getApplicationContext()
    }

    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
                .connectTimeout((60 * 5).toLong(), TimeUnit.SECONDS)
            .callTimeout(2, TimeUnit.MINUTES)
                .readTimeout((60 * 5).toLong(), TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)

        builder.addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
            requestBuilder.header("Content-Type", "application/json")
            requestBuilder.method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)

        }

        return builder.build()
    }


    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        val builder = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

       /* okHttpClient.dispatcher().executorService().shutdown()
        okHttpClient.connectionPool().evictAll()
        okHttpClient.cache()!!.close()*/



        return builder.build()
    }

    @Provides
    fun providesApiServices(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java!!)
    }

    @Provides
    fun providesUtils(context: Context): Utils {
        return Utils(context)
    }

    @Provides
    fun provideUserPref(context: Context): UserPref {
        return UserPref(context)
    }
}