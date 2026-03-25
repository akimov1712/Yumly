package ru.topbun.data.source.remote.dto.verification

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import ru.topbun.domain.entity.verification.VerificationStatusType
import java.lang.reflect.Type

sealed interface ConfirmVerificationResponse

data class TokenResponse(
    val token: String
) : ConfirmVerificationResponse

data class VerificationStatusResponse(
    val status: VerificationStatusType
) : ConfirmVerificationResponse

class ConfirmVerificationDeserializer : JsonDeserializer<ConfirmVerificationResponse> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ConfirmVerificationResponse {

        val obj = json.asJsonObject

        return when {
            obj.has("token") -> {
                context.deserialize<TokenResponse>(
                    obj,
                    TokenResponse::class.java
                )
            }

            obj.has("status") -> {
                context.deserialize<VerificationStatusResponse>(
                    obj,
                    VerificationStatusResponse::class.java
                )
            }

            else -> {
                throw JsonSyntaxException("Unknown confirm verification response: $json")
            }
        }
    }
}