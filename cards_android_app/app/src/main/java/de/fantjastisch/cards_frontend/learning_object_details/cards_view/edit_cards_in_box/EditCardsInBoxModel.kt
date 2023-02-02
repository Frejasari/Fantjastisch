package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.*
import java.util.*

class EditCardsInBoxModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository()
) {

    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(
        learningBoxId: UUID,
        learningObjectId: UUID
    ): RepoResult<List<CardSelectItem>> = coroutineScope {
        val (cardsResult, cardsInLearningBoxResult, cardsInLearningObjectResult) = awaitAll(
            async {
                cardRepository.getPage(
                    categoryIds = null,
                    search = null,
                    tag = null,
                    sort = false
                )
            },
            async {
                cardToLearningBoxRepository.getCardIdsForBox(
                    learningBoxId = learningBoxId
                )
            },
            async {
                cardToLearningBoxRepository.getAllCardsForLearningObject(
                    learningObjectId = learningObjectId
                )
            }
        )
        when {
            cardsResult is RepoResult.Success
                    && cardsInLearningBoxResult is RepoResult.Success
                    && cardsInLearningObjectResult is RepoResult.Success -> {
                val cards = (cardsResult.result) as List<CardEntity>
                val cardsInLearningBox = (cardsInLearningBoxResult.result) as List<UUID>
                val cardsInLearningObject = (cardsInLearningObjectResult.result) as List<UUID>
                val containedCards = filterContainedAndSelectableCards(
                    listOfCardIdsInBox = cardsInLearningBox,
                    listOfCardIdsInObject = cardsInLearningObject,
                    allCards = cards
                )
                RepoResult.Success(containedCards)
            }
            else -> RepoResult.Error(emptyList())
        }
    }

    private fun filterContainedAndSelectableCards(
        listOfCardIdsInBox: List<UUID>,
        listOfCardIdsInObject: List<UUID>,
        allCards: List<CardEntity>
    ): List<CardSelectItem> {
        val cardsPresentInOtherBoxes =
            listOfCardIdsInObject.filter { id ->
                !listOfCardIdsInBox.contains(id)
            }

        val selectableCardsInBox = allCards.filter { card ->
            listOfCardIdsInBox.contains(card.id) || !cardsPresentInOtherBoxes.contains(card.id)
        }.map { card ->
            CardSelectItem(
                card = card,
                isChecked = listOfCardIdsInBox.contains(card.id)
            )
        }.sortedByDescending { it.isChecked }
        return selectableCardsInBox
    }

    suspend fun updateCardsInBox(
        selectedCardsIds: List<UUID>,
        learningBoxId: UUID
    ): RepoResult<Unit> {
        return cardToLearningBoxRepository.updateBoxCards(
            selected = selectedCardsIds,
            learningBoxId = learningBoxId,
        )
    }
}