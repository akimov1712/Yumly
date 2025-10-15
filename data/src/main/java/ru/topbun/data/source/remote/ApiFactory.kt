package ru.topbun.data.source.remote

import android.util.Log.v
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.topbun.data.BuildConfig
import ru.topbun.data.source.remote.api.account.AccountApi
import ru.topbun.data.source.remote.api.login.LoginApi
import ru.topbun.data.source.remote.api.recipe.RecipeApi
import ru.topbun.data.source.remote.api.signUp.SignUpApi
import ru.topbun.data.source.remote.api.verification.VerificationApi

class ApiFactory(
    private val authTokenProvider: AuthTokenInterceptor
) {

    private fun createOkHttpClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
        return OkHttpClient.Builder()
            .addInterceptor(authTokenProvider)
            .addInterceptor(logInterceptor)
            .build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()


    val signUpApi = retrofit.create<SignUpApi>()
    val loginApi = retrofit.create<LoginApi>()
    val verificationApi = retrofit.create<VerificationApi>()
    val accountApi = retrofit.create<AccountApi>()
    val recipeApi = retrofit.create<RecipeApi>()

}