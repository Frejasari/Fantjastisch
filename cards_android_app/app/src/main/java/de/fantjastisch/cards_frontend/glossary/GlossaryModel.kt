package de.fantjastisch.cards_frontend.glossary

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.link.LinkRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.LinkEntity
import java.util.*

class GlossaryModel(
    //private val cardId: UUID,
    private val cardRepository: CardRepository = CardRepository(),
    //private val linkRepository: LinkRepository = LinkRepository()
) {

    suspend fun getCards(
        categoryIds: List<UUID>,
        search: String,
        tag: String,
        sort: Boolean
    ) = cardRepository.getPage(
        categoryIds = categoryIds.ifEmpty { null },
        search = search.ifEmpty { null },
        tag = tag.ifEmpty { null },
        sort = sort
    )

    suspend fun deleteCard(cardId: UUID) = cardRepository.deleteCard(
        cardId = cardId
    )

/*    data class Link(
        val links: List<LinkEntity>
    )*/

/*    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<GlossaryModel.Link> = coroutineScope {
        // Runs coroutines in parallel and waits until all of them are done
        val (linkResult) = awaitAll(
            async { linkRepository.getLinkPage(id = cardId)}
        )

        when {
                    linkResult is RepoResult.Success-> {
                val links = linkResult.result as List<LinkEntity>
                RepoResult.Success(
                    Link(
                        links = links
                    )
                )
            }
            else -> RepoResult.Error(emptyList())
        }

    }

    suspend fun deleteLink(linkId: UUID) = linkRepository.deleteLink(
        id = linkId
    )*/
}