package de.fantjastisch.cards_frontend.card

import de.fantjastisch.cards_frontend.infrastructure.client
import de.fantjastisch.cards_frontend.infrastructure.enqueue
import org.openapitools.client.apis.CardApi
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.ErrorResponseEntity
import org.openapitools.client.models.UpdateCardEntity
import java.util.*


class CardRepository {

    private val service = client.createService(CardApi::class.java)

    fun getCard(
        id: UUID,
        onSuccess: (CardEntity) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.getCard(id).enqueue(onSuccess, onFailure)

    fun getPage(
        categoryIds: List<UUID>?,
        search: String?,
        tag: String?,
        sort: Boolean?,
        onSuccess: (List<CardEntity>) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.getCardPage(
            categoryIds,
            search,
            tag,
            sort
        ).enqueue(onSuccess, onFailure)

    fun createCard(
        card: CreateCardEntity,
        onSuccess: (String) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.createCard(card).enqueue(onSuccess, onFailure)

    fun updateCard(
        card: UpdateCardEntity,
        onSuccess: (Unit) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.updateCard(card).enqueue(onSuccess, onFailure)

    fun deleteCard(
        cardId: UUID,
        onSuccess: (Unit) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.deleteCard(cardId).enqueue(onSuccess, onFailure)

}
