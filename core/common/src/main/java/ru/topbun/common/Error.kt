package ru.topbun.common

sealed interface Error

interface DataError: Error{

    enum class Network{
        REQUEST_TIMEOUT,
        SERIALIZATION,
        SERVER_ERROR,
        NO_INTERNET,
        UNKNOWN,

        USER_EXISTS,
        USER_NOT_FOUND,
        INVALID_DATA,
    }

    enum class Local{
        WRITE_TOKEN,
        READ_TOKEN
    }

}