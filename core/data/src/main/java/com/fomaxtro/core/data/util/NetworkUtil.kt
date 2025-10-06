package com.fomaxtro.core.data.util

import com.fomaxtro.core.domain.util.Result
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import timber.log.Timber

suspend fun <T> safeRemoteCall(
    block: suspend () -> T
): Result<T, NetworkError> {
    return try {
        Result.Success(block())
    } catch (e: ClientRequestException) {
        val networkError = when (e.response.status) {
            HttpStatusCode.BadRequest -> NetworkError.BAD_REQUEST
            HttpStatusCode.Unauthorized -> NetworkError.UNAUTHORIZED
            HttpStatusCode.NotFound -> NetworkError.NOT_FOUND
            HttpStatusCode.Conflict -> NetworkError.CONFLICT
            HttpStatusCode.TooManyRequests -> NetworkError.TOO_MANY_REQUESTS
            else -> NetworkError.UNKNOWN
        }

        Result.Error(networkError)
    } catch (_: ServerResponseException) {
        Result.Error(NetworkError.SERVER_ERROR)
    } catch (e: UnresolvedAddressException) {
        Timber.tag("HttpClient").e(e)

        Result.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        Timber.tag("HttpClient").e(e)

        Result.Error(NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        Timber.tag("HttpClient").e(e)

        if (e is CancellationException) throw e

        Result.Error(NetworkError.UNKNOWN)
    }
}