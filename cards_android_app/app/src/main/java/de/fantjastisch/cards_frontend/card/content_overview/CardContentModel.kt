package de.fantjastisch.cards_frontend.card.content_overview

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.CardEntity
import java.util.*

class CardContentModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val id: UUID
) {

    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<CardEntity> = coroutineScope {
        // Runs coroutines in parallel and waits until all of them are done
        cardRepository.getCard(id = id)
    }
}