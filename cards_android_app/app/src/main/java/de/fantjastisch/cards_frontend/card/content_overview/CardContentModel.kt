package de.fantjastisch.cards_frontend.card.content_overview

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.util.RepoResult
import org.openapitools.client.models.CardEntity
import java.util.*

/**
 * Kapselt die Logik für das [CardContentViewModel].
 * Fungiert als Vermittler zwischen Repository und ViewModel.
 *
 * @author Semjon Nirmann
 */
class CardContentModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val id: UUID
) {

    /**
     * Holt eine Karte
     *
     * @return RepoResult<CardEntity> Repo Result mit der gefundenen Karte im Erfolgsfall
     *
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<CardEntity> = cardRepository.getCard(id = id)
}