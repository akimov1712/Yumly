package ru.topbun.data

import android.content.Context
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import ru.topbun.core.android.isInternetAvailable
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.Result
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class AppException: Exception()
internal class NoInternetException: AppException()
internal class UnauthorizedException: AppException()

internal suspend fun <T> Context.exceptionWrapper(data: T? = null, block: suspend () -> Result<T, DataError>): Result<T, DataError> =
    try {
        withInternetCheck(this){
            block()
        }
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

internal inline fun <D> withInternetCheck(
    context: Context,
    block: () -> Result<D, DataError>
): Result<D, DataError> {
    return if (!isInternetAvailable(context)) {
        throw NoInternetException()
    } else {
        block()
    }
}