package ru.topbun.data

import android.content.Context
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import ru.topbun.android.isInternetAvailable
import ru.topbun.common.error.DataError
import ru.topbun.common.Result
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class AppException: Exception()
class NoInternetException: AppException()
class UnauthorizedException: AppException()

suspend fun <T> exceptionWrapper(data: T? = null,block: suspend () -> Result<T, DataError>): Result<T, DataError> =
    try {
        block()
    } catch (e: NoInternetException) {
        e.printStackTrace()
        Result.Error(DataError.Network.NO_INTERNET, data)
    } catch (e: UnauthorizedException) {
        e.printStackTrace()
        Result.Error(DataError.Network.UNAUTHORIZED, data)
    }catch (e: SocketTimeoutException) {
        e.printStackTrace()
        Result.Error(DataError.Network.REQUEST_TIMEOUT, data)
    } catch (e: JsonSyntaxException) {
        e.printStackTrace()
        Result.Error(DataError.Network.SERIALIZATION, data)
    } catch (e: JsonParseException) {
        e.printStackTrace()
        Result.Error(DataError.Network.SERIALIZATION, data)
    } catch (e: IllegalStateException) {
        e.printStackTrace()
        Result.Error(DataError.Network.SERIALIZATION, data)
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        Result.Error(DataError.Network.SERVER_ERROR, data)
    } catch (e: ConnectException) {
        e.printStackTrace()
        Result.Error(DataError.Network.SERVER_ERROR, data)
    } catch (e: SocketException) {
        e.printStackTrace()
        Result.Error(DataError.Network.SERVER_ERROR, data)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(DataError.Network.UNKNOWN, data)
    }

inline fun <D> withInternetCheck(
    context: Context,
    block: () -> Result<D, DataError>
): Result<D, DataError> {
    return if (!isInternetAvailable(context)) {
        throw NoInternetException()
    } else {
        block()
    }
}