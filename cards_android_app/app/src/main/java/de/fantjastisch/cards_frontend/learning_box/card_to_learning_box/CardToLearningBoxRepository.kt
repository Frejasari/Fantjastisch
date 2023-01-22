package de.fantjastisch.cards_frontend.learning_box.card_to_learning_box

import androidx.room.Transaction
import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import org.openapitools.client.models.ErrorResponseEntity
import java.util.*


class CardToLearningBoxRepository(
    private val repository: InternalCardToLearningBoxRepository
    = InternalCardToLearningBoxRepository(AppDatabase.database.cardToLearningBoxDao())
) {

    suspend fun getCardIdsForBox(
        learningBoxId: UUID
    ): RepoResult<List<UUID>> {
        return try {
            val cardIdsForBox = repository.getCardIdsForBox(learningBoxId)
            RepoResult.Success(cardIdsForBox)
        } catch (ex: Throwable) {
            // TODO wo sind Validatoren, die den input validieren?
            RepoResult.ServerError()
        }
    }

    suspend fun insertCards(
        cardIds: List<UUID>,
        learningBoxId: UUID,
    ): RepoResult<Unit> {
        return try {
            repository.insertCards(
                cardIds.map {
                    CardToLearningBox(
                        learningBoxId = learningBoxId,
                        cardId = it
                    )
                })
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            // TODO wo sind Validatoren, die den input validieren?
            RepoResult.ServerError()
        }
    }

    @Transaction
    suspend fun updateBoxCards(
        selected: List<UUID>,
        learningBoxId: UUID,
    ): RepoResult<Unit> {
        return try {
            repository.deleteAllCardsFromLearningBox(learningBoxId = learningBoxId)
            repository.insertCards(selected.map {
                CardToLearningBox(
                    learningBoxId = learningBoxId,
                    cardId = it
                )
            })
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
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
    ): RepoResult<List<UUID>> {
        return try {
            val allCardsForLearningObject =
                repository.getAllCardsForLearningObject(learningObjectId = learningObjectId)
            RepoResult.Success(allCardsForLearningObject)
        } catch (ex: Throwable) {
            // TODO wo sind Validatoren, die den input validieren?
            RepoResult.ServerError()
        }
    }

    @Transaction
    suspend fun moveCards(from: UUID, to: UUID, cardIds: List<UUID>): RepoResult<Unit> {
        return try {
            repository.deleteCardsFromBox(cardIds = cardIds, learningBoxId = from)
            repository.insertCards(cardIds.map {
                CardToLearningBox(
                    learningBoxId = to,
                    cardId = it
                )
            })
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }
}