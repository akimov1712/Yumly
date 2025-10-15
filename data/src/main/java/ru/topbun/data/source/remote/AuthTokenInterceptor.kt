package ru.topbun.data.source.remote

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.topbun.data.source.local.config.TokenManager

class AuthTokenInterceptor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        val token = tokenManager.getToken()
        requestBuilder.createAuthHeader(token)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    private fun Request.Builder.createAuthHeader(token: String?){
        if (token != null) header(HEADER_AUTHORIZATION, "Bearer $token")
    }

    private companion object{

        const val HEADER_AUTHORIZATION = "Authorization"

    }

}