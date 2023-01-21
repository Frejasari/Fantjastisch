package de.fantjastisch.cards_frontend.glossary

import de.fantjastisch.cards_frontend.card.CardRepository
import java.util.*

class GlossaryModel(
    private val cardRepository: CardRepository = CardRepository()
) {

    suspend fun getCards() = cardRepository.getPage(
        categoryIds = null,
        search = null,
        tag = null,
        sort = null
    )

    suspend fun deleteCard(cardId: UUID) = cardRepository.deleteCard(
        cardId = cardId
    )
}