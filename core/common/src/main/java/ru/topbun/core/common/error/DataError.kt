package ru.topbun.core.common.error

sealed interface DataError: Error{

    enum class Network: DataError{
        // Default
        REQUEST_TIMEOUT,
        SERIALIZATION,
        SERVER_ERROR,
        NO_INTERNET,
        UNKNOWN,

        // Custom
        EXISTS,
        INVALID_DATA,
        BAD_REQUEST,
        NOT_FOUND,
        NOT_VERIFIED,
        UNAUTHORIZED,
        FORBIDDEN,
    }

}