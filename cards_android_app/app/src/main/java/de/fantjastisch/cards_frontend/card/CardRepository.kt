package de.fantjastisch.cards_frontend.card

import de.fantjastisch.cards_frontend.infrastructure.client
import de.fantjastisch.cards_frontend.infrastructure.toRepoResponse
import de.fantjastisch.cards_frontend.infrastructure.enqueue
import org.openapitools.client.apis.CardApi
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.ErrorResponseEntity
import org.openapitools.client.models.UpdateCardEntity
import retrofit2.awaitResponse
import java.util.*


class CardRepository {

    private val service = client.createService(CardApi::class.java)

    suspend fun getCard(
        id: UUID,
//        onSuccess: (CardEntity) -> Unit,
//        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.getCard(id)
            .awaitResponse()
            .toRepoResponse()

    suspend fun getPage(
        categoryIds: List<UUID>?,
        search: String?,
        tag: String?,
        sort: Boolean?,
//        onSuccess: (List<CardEntity>) -> Unit,
//        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) = service.getCardPage(
        categoryIds,
        search,
        tag,
        sort
    )
        .awaitResponse()
        .toRepoResponse()


    fun createCard(
        card: CreateCardEntity,
        onSuccess: (String) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.createCard(card).enqueue(onSuccess, onFailure)

    suspend fun updateCard(
        card: UpdateCardEntity,
    ) =
        service
            .updateCard(card)
            .awaitResponse()
            .toRepoResponse()

    fun deleteCard(
        cardId: UUID,
        onSuccess: (Unit) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.deleteCard(cardId).enqueue(onSuccess, onFailure)

}
