package ru.topbun.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.topbun.data.source.remote.ApiFactory
import ru.topbun.data.source.remote.AuthTokenInterceptor
import ru.topbun.data.source.remote.dto.login.LoginResponse
import ru.topbun.data.source.remote.dto.login.LoginResponseDeserializer
import ru.topbun.data.source.remote.dto.verification.ConfirmVerificationDeserializer
import ru.topbun.data.source.remote.dto.verification.ConfirmVerificationResponse
import java.time.LocalDateTime

internal inline fun <reified T> Module.api(noinline provider: ApiFactory.() -> T) {
    single { get<ApiFactory>().provider() }
}

val apiModule = module {
    factory<Gson> {
        GsonBuilder()
            .registerTypeAdapter(LoginResponse::class.java, LoginResponseDeserializer())
            .registerTypeAdapter(ConfirmVerificationResponse::class.java, ConfirmVerificationDeserializer())
            .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ -> LocalDateTime.parse(json.asString) })
            .create()
    }
    factoryOf(::AuthTokenInterceptor)
    factoryOf(::ApiFactory)
    api { accountApi }
    api { favoriteApi }
    api { followApi }
    api { gptApi }
    api { historyApi }
    api { loginApi }
    api { notificationApi }
    api { recipeApi }
    api { signUpApi }
    api { uploadApi }
    api { verificationApi }
}
