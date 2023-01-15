package de.fantjastisch.cards_frontend.card

import android.util.Log
import org.openapitools.client.apis.CardApi
import org.openapitools.client.infrastructure.ApiClient
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.UpdateCardEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


val client: ApiClient by lazy { ApiClient() }

fun <T> Call<T>.enqueue(onSuccess: (T) -> Unit, onFailure: () -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            response.body()?.let(onSuccess) ?: onFailure()
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            t.printStackTrace()
            onFailure()
        }
    })
}


class CardRepository {

    val service = client.createService(CardApi::class.java)

    fun getCard(id: UUID, onSuccess: (CardEntity) -> Unit, onFailure: () -> Unit) =
        service.getCard(id).enqueue(onSuccess, onFailure)

    fun getPage(
        categoryIds: List<UUID>?,
        search: String?,
        tag: String?,
        sort: Boolean?,
        onSuccess: (List<CardEntity>) -> Unit,
        onFailure: () -> Unit
    ) =
        service.getCardPage(categoryIds, search, tag, sort).enqueue(onSuccess, onFailure)

    fun createCard(card: CreateCardEntity, onSuccess: (String) -> Unit, onFailure: () -> Unit) =
        service.createCard(card).enqueue(onSuccess, onFailure)

    fun updateCard(card: UpdateCardEntity, onSuccess: (Unit) -> Unit, onFailure: () -> Unit) =
        service.updateCard(card).enqueue(onSuccess, onFailure)
}