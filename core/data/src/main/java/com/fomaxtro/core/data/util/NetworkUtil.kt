package com.fomaxtro.core.data.util

import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.Result
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import timber.log.Timber

inline fun <T> safeRemoteCall(
    block: () -> T
): Result<T, DataError> {
    return try {
        Result.Success(block())
    } catch (e: ClientRequestException) {
        val networkError = when (e.response.status) {
            HttpStatusCode.BadRequest -> DataError.Validation.INVALID_INPUT
            HttpStatusCode.Unauthorized -> DataError.Network.UNAUTHORIZED
            HttpStatusCode.NotFound -> DataError.Resource.NOT_FOUND
            HttpStatusCode.Conflict -> DataError.Resource.CONFLICT
            HttpStatusCode.TooManyRequests -> DataError.Network.TOO_MANY_REQUESTS
            else -> DataError.Network.UNKNOWN
        }

        Result.Error(networkError)
    } catch (_: ServerResponseException) {
        Result.Error(DataError.Network.SERVICE_UNAVAILABLE)
    } catch (e: UnresolvedAddressException) {
        Timber.tag("HttpClient").e(e)

        Result.Error(DataError.Network.NO_CONNECTION)
    } catch (e: SerializationException) {
        Timber.tag("HttpClient").e(e)

        Result.Error(DataError.Network.UNKNOWN)
    } catch (e: Exception) {
        Timber.tag("HttpClient").e(e)

        if (e is CancellationException) throw e

        Result.Error(DataError.Network.UNKNOWN)
    }
}