package de.fantjastisch.cards_frontend.infrastructure

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.openapitools.client.infrastructure.ApiClient
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.ErrorResponseEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


val client: ApiClient by lazy { ApiClient() }

// extension Function auf dem Typ Call<T>, welche die callbacks
// onsuccess und onfailure bei einem api-call aufruft
fun <T> Call<T>.enqueue(onSuccess: (T) -> Unit, onFailure: (errors: ErrorResponseEntity?) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val body = response.body()
            if (body != null) {
                onSuccess(body)
            } else {
                val errorBody = response.errorBody()
                if (errorBody != null) {

                    if (response.code() == 422 || response.code() == 404) {
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
                        onFailure(errors)
                    }
                }
                onFailure(null)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            t.printStackTrace()
            onFailure(null)
        }
    })
}
