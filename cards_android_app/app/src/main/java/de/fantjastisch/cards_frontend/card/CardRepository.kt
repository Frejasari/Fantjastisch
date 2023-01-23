package de.fantjastisch.cards_frontend.card

import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.client
import de.fantjastisch.cards_frontend.infrastructure.toRepoResponse
import org.openapitools.client.apis.CardApi
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.UpdateCardEntity
import retrofit2.awaitResponse
import java.util.*


class CardRepository {

    private val service = client.createService(CardApi::class.java)

    suspend fun getCard(
        id: UUID,
    ) =
        service.getCard(id)
            .awaitResponse()
            .toRepoResponse()

    suspend fun getPage(
        categoryIds: List<UUID>?,
        search: String?,
        tag: String?,
        sort: Boolean?
    ): RepoResult<List<CardEntity>> = service.getCardPage(
        categoryFilter = categoryIds,
        search = search,
        tag = tag,
        sort = sort
    ).awaitResponse()
        .toRepoResponse()


    suspend fun createCard(
        card: CreateCardEntity,
    ) = service.createCard(card).awaitResponse().toRepoResponse()

    suspend fun updateCard(
        card: UpdateCardEntity,
    ) =
        service
            .updateCard(card)
            .awaitResponse()
            .toRepoResponse()

    suspend fun deleteCard(
        cardId: UUID,
    ) =
        service.deleteCard(cardId).awaitResponse().toRepoResponse()

}
