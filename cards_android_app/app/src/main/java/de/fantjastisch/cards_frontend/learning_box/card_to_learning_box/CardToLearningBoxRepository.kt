package de.fantjastisch.cards_frontend.learning_box.card_to_learning_box

import org.openapitools.client.models.ErrorResponseEntity
import java.util.*


class CardToLearningBoxRepository(val repository: InternalCardToLearningBoxRepository) {

    fun getCardIdsForBox(
        learningBoxId: UUID,
        onSuccess: (List<UUID>) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        try {
            onSuccess(repository.getCardIdsForBox(learningBoxId))
        } catch (ex: Throwable) {
            onFailure(null)
        }
    }
    fun insertCardsForBox(
        cardIds: List<UUID>,
        learningBoxId: UUID,
        onSuccess: () -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        try {
            cardIds.forEach { cardId ->
                repository.insertCardIntoBox(
                    CardToLearningBox(
                        learningBoxId = learningBoxId,
                        cardId = cardId
                    )
                )
            }
            onSuccess()
        } catch (ex: Throwable) {
            onFailure(null)
        }
    }

    fun deleteCardFromBox(
        cardToLearningBox: CardToLearningBox,
        onSuccess: () -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        try {
            repository.deleteCardFromBox(
                cardId = cardToLearningBox.cardId,
                learningBoxId = cardToLearningBox.learningBoxId
            )
            onSuccess()
        } catch (ex: Throwable) {
            onFailure(null)
        }
    }

    fun getNumOfCardsFromLearningBoxId(
        learningBoxId: UUID,
        onSuccess: (Int) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit

    ) {
        try {
            onSuccess(repository.getNumOfCardsFromLearningBoxId(learningBoxId))
        } catch (ex: Throwable) {
            onFailure(null)
        }
    }
}