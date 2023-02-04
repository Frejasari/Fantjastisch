package de.fantjastisch.cards_frontend.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.ErrorResponseEntity
import retrofit2.Response

/**
 * Klasse, die als standardisierte Rückgabe durch die Repositories genutzt wird.
 *
 * @param T der Rückgabe-Typ bei Erfolg
 *
 * @author Freja Sender
 */
sealed class RepoResult<T> {
    data class Success<T>(val result: T) : RepoResult<T>()
    data class Error<T>(val errors: List<ErrorEntryEntity>) : RepoResult<T>()

    class ServerError<T> : RepoResult<T>()
}

/**
 * Extension-Function auf dem RepoResult Object, welche ein einfaches Interface mit Callbacks zum Behandeln dieser zur Verfügung stellt.
 *
 * @param T der Rückgabe-Typ bei Erfolg
 * @param onSuccess Callback der bei Erfolg aufgerufen wird.
 * @param onValidationError Callback, der bei einem Validierungsfehler aufgerufen wird.
 * @param onUnexpectedError Callback, der bei einem unerwarteten Fehler aufgerufen wird.
 *
 * @author Freja Sender
 */
fun <T> RepoResult<T>.fold(
    onSuccess: (T) -> Unit,
    onValidationError: (errors: List<ErrorEntryEntity>) -> Unit,
    onUnexpectedError: () -> Unit
) {
    when (this) {
        is RepoResult.Success -> onSuccess(this.result)
        is RepoResult.Error -> onValidationError(this.errors)
        is RepoResult.ServerError -> onUnexpectedError()
    }
}

/**
 * Extension-Function auf dem Response Object, welches dieses zu unserer [RepoResult] Klasse mappt
 *
 * @param T der Rückgabe-Typ bei Erfolg
 *
 * @author Freja Sender
 */
fun <T> Response<T>.toRepoResult(): RepoResult<T> {
    val body = body()
    if (body != null) {
        return RepoResult.Success(body)
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
                    return RepoResult.Error(errors.errors)
                }

            }
        }
    }
    return RepoResult.ServerError()
}