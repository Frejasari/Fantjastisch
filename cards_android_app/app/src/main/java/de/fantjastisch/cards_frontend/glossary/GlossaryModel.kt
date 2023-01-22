package de.fantjastisch.cards_frontend.glossary

import de.fantjastisch.cards_frontend.card.CardRepository
import java.util.*

class GlossaryModel(
    private val cardRepository: CardRepository = CardRepository()
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
}