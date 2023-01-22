package de.fantjastisch.cards_frontend.learning_box.card_to_learning_box

import androidx.room.Transaction
import de.fantjastisch.cards_frontend.config.AppDatabase
import org.openapitools.client.models.ErrorResponseEntity
import java.util.*


class CardToLearningBoxRepository(
    private val repository: InternalCardToLearningBoxRepository
    = InternalCardToLearningBoxRepository(AppDatabase.database.cardToLearningBoxDao())
) {

    suspend fun getCardIdsForBox(
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

    suspend fun insertCardsForBox(
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

    suspend fun deleteCardsFromBox(
        cardIds: List<UUID>,
        learningBoxId: UUID,
        onSuccess: () -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        try {
            cardIds.forEach { cardId ->
                repository.deleteCardFromBox(cardId = cardId, learningBoxId = learningBoxId)
            }
            onSuccess()
        } catch (ex: Throwable) {
            onFailure(null)
        }
    }

    @Transaction
    suspend fun insertAndDeleteInBox(
        selected: List<UUID>,
        unselected: List<UUID>,
        learningBoxId: UUID,
        onSuccess: () -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        insertCardsForBox(
            cardIds = selected,
            learningBoxId = learningBoxId,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
        deleteCardsFromBox(
            cardIds = unselected,
            learningBoxId = learningBoxId,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    suspend fun getNumOfCardsFromLearningBoxId(
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

    suspend fun getAllCardsForLearningObject(
        learningObjectId: UUID,
        onSuccess: (List<UUID>) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        try {
            onSuccess(repository.getAllCardsForLearningObject(learningObjectId = learningObjectId))
        } catch (ex: Throwable) {
            onFailure(null)
        }

    }
}