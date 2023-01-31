package de.fantjastisch.cards_frontend.glossary

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.*

class GlossaryModel(
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val cardRepository: CardRepository = CardRepository(),
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


    suspend fun deleteCard(cardId: UUID): RepoResult<Unit> = coroutineScope {
        val (apiResult, dbResult) = awaitAll(
            async { cardRepository.deleteCard(cardId = cardId) },
            async {
                cardToLearningBoxRepository.deleteCard(
                    cardId = cardId
                )
            },
        )
        when {
            apiResult is RepoResult.Success
                    && dbResult is RepoResult.Success -> {
                RepoResult.Success(Unit)
            }
            else -> RepoResult.Error(emptyList())
        }
    }


}