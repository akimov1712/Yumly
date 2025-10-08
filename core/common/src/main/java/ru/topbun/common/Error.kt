package ru.topbun.common

sealed interface Error

interface DataError: Error{

    enum class Network{
        // Default
        REQUEST_TIMEOUT,
        SERIALIZATION,
        SERVER_ERROR,
        NO_INTERNET,
        UNKNOWN,

        // Custom
        EXISTS,
        NOT_FOUND,
        INVALID_DATA,
        USER_NOT_VERIFIED,
        CODE_EXPIRED,
        UNAUTHORIZED,
        FORBIDDEN,
    }

    enum class Local{
        WRITE_TOKEN,
        READ_TOKEN
    }

}