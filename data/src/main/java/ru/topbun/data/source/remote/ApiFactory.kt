package ru.topbun.data.source.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.topbun.data.BuildConfig
import ru.topbun.data.source.remote.api.LoginApi
import ru.topbun.data.source.remote.api.SignUpApi

object ApiFactory {

    private fun createDefaultOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(createDefaultOkHttpClient())
        .build()


    val signUpApi = retrofit.create<SignUpApi>()
    val loginApi = retrofit.create<LoginApi>()

}