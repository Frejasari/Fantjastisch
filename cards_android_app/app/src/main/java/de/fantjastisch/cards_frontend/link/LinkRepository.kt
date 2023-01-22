package de.fantjastisch.cards_frontend.link

import de.fantjastisch.cards_frontend.infrastructure.client
import de.fantjastisch.cards_frontend.infrastructure.enqueue
import org.openapitools.client.apis.CardApi
import org.openapitools.client.apis.LinkApi
import org.openapitools.client.models.*
import java.util.*


class LinkRepository {

    private val service = client.createService(LinkApi::class.java)

    fun getLink(
        id: UUID,
        onSuccess: (LinkEntity) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.getLink(id).enqueue(onSuccess, onFailure)

    fun getLinkPage(
        id: UUID,
        onSuccess: (List<LinkEntity>) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.getLinkPage(id).enqueue(onSuccess, onFailure)

    fun createLink(
        link: CreateLinkEntity,
        onSuccess: (String) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.createLink(link).enqueue(onSuccess, onFailure)

    fun updateLink(
        link: UpdateLinkEntity,
        onSuccess: (Unit) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.updateLink(link).enqueue(onSuccess, onFailure)

    fun deleteLink(
        id: UUID,
        onSuccess: (Unit) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
        service.deleteLink(id).enqueue(onSuccess, onFailure)

}
