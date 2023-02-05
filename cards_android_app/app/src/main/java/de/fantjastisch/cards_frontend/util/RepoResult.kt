package de.fantjastisch.cards_frontend.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.util.RepoResult.*
import kotlinx.coroutines.suspendCancellableCoroutine
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.ErrorResponseEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

/**
 * Klasse, die als standardisierte Rückgabe durch die Repositories genutzt wird.
 *
 * @param T der Rückgabe-Typ bei Erfolg
 *
 * @author Freja Sender
 */
sealed class RepoResult<T> {

    enum class UnexpectedErrorType {
        NETWORK_ERROR,
        UNEXPECTED_ERROR
    }

    data class Success<T>(val result: T) : RepoResult<T>()
    data class Error<T>(val errors: List<ErrorEntryEntity>) : RepoResult<T>()
    data class ServerError<T>(val cause: UnexpectedErrorType) : RepoResult<T>()
}

/**
 * Extension-Function auf dem [RepoResult] Object, welche ein einfaches Interface mit Callbacks zum Behandeln dieser zur Verfügung stellt.
 * Nur im Kontext eines ErrorHandlingViewModels nutzbar -> Standardfunktionalität zum Handeln von Validation Errors und Unexpected Errors
 *
 * @param T der Rückgabe-Typ bei Erfolg
 * @param onSuccess Callback der bei Erfolg aufgerufen wird.
 * @param onValidationError Callback, der bei einem Validierungsfehler aufgerufen wird.
 * @param onUnexpectedError Callback, der bei einem unerwarteten Fehler aufgerufen wird.
 *
 * @author Freja Sender
 */
context(ErrorHandlingViewModel)
fun <T> RepoResult<T>.fold(
    onSuccess: (T) -> Unit,
    onValidationError: (errors: List<ErrorEntryEntity>) -> Unit = ::setValidationErrors,
    onUnexpectedError: (cause: UnexpectedErrorType) -> Unit = ::setUnexpectedError
) {
    when (this) {
        is Success -> onSuccess(this.result)
        is Error -> onValidationError(this.errors)
        is ServerError -> onUnexpectedError(this.cause)
    }
}

/**
 * Extension-Function auf dem [Call] Object, welches einen API-Call absetzt & diesen direkt auf unser RepoResult-Object abbildet.
 *
 * @param T der Rückgabe-Typ bei Erfolg
 *
 * @author Freja Sender
 */
suspend fun <T> Call<T>.awaitResponse(): RepoResult<T> {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            cancel()
        }
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                continuation.resume(response.toRepoResult())
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resume(ServerError(UnexpectedErrorType.NETWORK_ERROR))
            }
        })
    }
}

/**
 * Extension-Function auf dem [RepoResult] Object, welches das Überprüfen auf einen Netzwerkfehler vereinfacht.
 *
 * @param T der Rückgabe-Typ bei Erfolg
 *
 * @author Freja Sender
 */
fun <T> RepoResult<T>.isNetworkError(): Boolean {
    return when {
        this is ServerError && this.cause == UnexpectedErrorType.NETWORK_ERROR -> true
        else -> false
    }
}

/**
 * Extension-Function auf dem Response Object, welches dieses zu unserer [RepoResult] Klasse mappt
 *
 * @param T der Rückgabe-Typ bei Erfolg
 *
 * @author Freja Sender
 */
private fun <T> Response<T>.toRepoResult(): RepoResult<T> {
    val body = body()
    if (body != null) {
        return Success(body)
    } else {
        val errorBody = errorBody()
        if (errorBody != null) {
            if (code() == 422 || code() == 404) {
                val moshi = Moshi
                    .Builder()
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
                val type =
                    Types.newParameterizedType(
                        ErrorResponseEntity::class.java,
                        ErrorEntryEntity::class.java
                    )
                val adapter: JsonAdapter<ErrorResponseEntity> = moshi.adapter(type)

                val errors: ErrorResponseEntity? = adapter.fromJson(errorBody.string())
                if (errors != null) {
                    return Error(errors.errors)
                }

            }
        }
    }
    return ServerError(UnexpectedErrorType.UNEXPECTED_ERROR)
}
