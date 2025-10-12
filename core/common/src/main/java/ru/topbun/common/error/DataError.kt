package ru.topbun.common.error

interface DataError: Error{

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
        NOT_FOUND,
        USER_NOT_VERIFIED,
        CODE_EXPIRED,
        UNAUTHORIZED,
        FORBIDDEN,
    }

    enum class Local: DataError{
        WRITE_TOKEN,
        READ_TOKEN
    }

}