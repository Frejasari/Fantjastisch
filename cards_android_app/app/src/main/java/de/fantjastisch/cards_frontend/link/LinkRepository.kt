package de.fantjastisch.cards_frontend.link

import de.fantjastisch.cards_frontend.infrastructure.client
import de.fantjastisch.cards_frontend.infrastructure.enqueue
import de.fantjastisch.cards_frontend.infrastructure.toRepoResponse
import org.openapitools.client.apis.CardApi
import org.openapitools.client.apis.LinkApi
import org.openapitools.client.models.*
import retrofit2.awaitResponse
import java.util.*


class LinkRepository {

    private val service = client.createService(LinkApi::class.java)

    suspend fun getLink(
        id: UUID,
    ) =
        service.getLink(id)
            .awaitResponse()
            .toRepoResponse()

    suspend fun getLinkPage(
        id: UUID
    ) =
        service.getLinkPage(id)
            .awaitResponse()
            .toRepoResponse()

    suspend fun createLink(
        link: CreateLinkEntity
    ) =
        service.createLink(link)
            .awaitResponse()
            .toRepoResponse()

    suspend fun updateLink(
        link: UpdateLinkEntity
    ) =
        service.updateLink(link)
            .awaitResponse()
            .toRepoResponse()

    suspend fun deleteLink(
        id: UUID
    ) =
        service.deleteLink(id)
            .awaitResponse()
            .toRepoResponse()

}
