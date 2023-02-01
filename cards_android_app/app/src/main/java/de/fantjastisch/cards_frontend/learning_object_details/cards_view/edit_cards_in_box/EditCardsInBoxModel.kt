package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
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
        val cards = getCards()

        if (cards != null) {
            val containedCards = loadContainedCards(
                allCards = cards,
                learningBoxId = learningBoxId,
                learningObjectId = learningObjectId
            )
            if (containedCards != null) {
                return@coroutineScope RepoResult.Success(containedCards)
            }
        }
        return@coroutineScope RepoResult.ServerError()

    }

    suspend fun getCards(): List<CardEntity>? {
        val result = cardRepository.getPage(
            categoryIds = null,
            search = null,
            tag = null,
            sort = false
        )
        return when (result) {
            is RepoResult.Success -> result.result
            is RepoResult.Error,
            is RepoResult.ServerError -> null // TODO
        }
    }

    suspend fun loadContainedCards(
        allCards: List<CardEntity>,
        learningBoxId: UUID,
        learningObjectId: UUID
    ): List<CardSelectItem>? {
        val result = cardToLearningBoxRepository.getCardIdsForBox(learningBoxId = learningBoxId)

        if (result is RepoResult.Success) {
            val listOfCardIdsInBox = result.result
            val listOfCardIdsInObject =
                getCardsInLearningObject(learningObjectId = learningObjectId)
                    ?: return null

            return filterContainedAndSelectableCards(
                listOfCardIdsInBox = listOfCardIdsInBox,
                listOfCardIdsInObject = listOfCardIdsInObject,
                allCards = allCards
            )
        }
        return null
    }

    suspend fun getCardsInLearningObject(learningObjectId: UUID): List<UUID>? {
        val listOfCardIdsInObject =
            cardToLearningBoxRepository.getAllCardsForLearningObject(learningObjectId = learningObjectId)

        return when (listOfCardIdsInObject) {
            is RepoResult.Success -> listOfCardIdsInObject.result
            is RepoResult.Error,
            is RepoResult.ServerError -> null
        }
    }

    fun filterContainedAndSelectableCards(
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

    suspend fun updateCardsInBox(selectedCardsIds: List<UUID>, learningBoxId: UUID): RepoResult<Unit> {
        return cardToLearningBoxRepository.updateBoxCards(
            selected = selectedCardsIds,
            learningBoxId = learningBoxId,
        )
    }
}