package de.fantjastisch.cards_frontend.learning_box.card_to_learning_box

import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.ErrorResponseEntity
import java.util.*


class CardToLearningBoxRepository(val repository: InternalCardToLearningBoxRepository) {


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
}