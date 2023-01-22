package de.fantjastisch.cards_frontend.link.update

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.link.LinkRepository
import kotlinx.coroutines.*
import org.openapitools.client.models.*
import java.util.*

class UpdateLinkModel(
    private val linkId: UUID,
    private val sourceId: UUID,
    private val cardRepository: CardRepository = CardRepository(),
    private val linkRepository: LinkRepository = LinkRepository()
) {

    suspend fun update(
        name: String,
        targetId: UUID,
    ): RepoResult<Unit> = linkRepository.updateLink(
        UpdateLinkEntity(
            id = linkId,
            name = name,
            source = sourceId,
            target = targetId,
        )
    )

    data class UpdateLink(
        val id: UUID,
        val name: String,
        val sourceId: UUID,
        val targetId: UUID,
        val allCards: List<CardEntity>
    )

    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<UpdateLink> = coroutineScope {
        // Runs coroutines in parallel and waits until all of them are done
        val (cardResult, linkResult) = awaitAll(
            async { linkRepository.getLink(id = linkId) },
            async { cardRepository.getPage(null,null,null,false) }
        )

        when {
            cardResult is RepoResult.Success
                    && linkResult is RepoResult.Success -> {
                val link = linkResult.result as LinkEntity
                val cards = cardResult.result as List<CardEntity>
                RepoResult.Success(
                    UpdateLink(
                        id = linkId,
                        name = link.name!!,
                        sourceId = sourceId,
                        targetId = link.target!!,
                        allCards = cards,
                    )
                )
            }
            else -> RepoResult.Error(emptyList())
        }

    }

}

