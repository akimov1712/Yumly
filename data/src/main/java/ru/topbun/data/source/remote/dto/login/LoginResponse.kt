package ru.topbun.data.source.remote.dto.login

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type

sealed interface LoginResponse

internal data class SuccessLoginResponse(
    val token: String
): LoginResponse

internal data class UnauthorizedLoginResponse(
    val email: String,
): LoginResponse

class LoginResponseDeserializer : JsonDeserializer<LoginResponse> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LoginResponse {

        val obj = json.asJsonObject

        return when {
            obj.has("token") -> {
                context.deserialize<SuccessLoginResponse>(obj, SuccessLoginResponse::class.java)
            }

            obj.has("email") -> {
                context.deserialize<UnauthorizedLoginResponse>(obj, UnauthorizedLoginResponse::class.java)
            }

            else -> {
                throw JsonSyntaxException("Json not serialized")
            }
        }
    }
}