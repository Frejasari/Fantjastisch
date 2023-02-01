package de.fantjastisch.cards_frontend.learning_box

import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import org.openapitools.client.models.ErrorResponseEntity
import java.util.*

class LearningBoxRepository(
    private val repository: InternalLearningBoxRepository = InternalLearningBoxRepository(
        AppDatabase.database.learningBoxDao()
    )
) {

    suspend fun getAllBoxesForLearningObject(
        learningObjectId: UUID
    ): RepoResult<List<LearningBoxWitNrOfCards>> {
        return try {
            val allBoxesForLearningObject =
                repository.getAllBoxesForLearningObjectWithNrOfCards(learningObjectId)
            RepoResult.Success(allBoxesForLearningObject)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }

    suspend fun getCardsFromLearningBoxInLearningObject(
        learningObjectId: UUID
    ): RepoResult<List<Int>> {
        return try {
            val cardsFromLearningObject = repository.getCardsFromLearningObject(learningObjectId)
            RepoResult.Success(cardsFromLearningObject)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }


    suspend fun findByBoxId(
        learningBoxId: UUID,
        onSuccess: (LearningBox) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        try {
            onSuccess(repository.findById(learningBoxId))
        } catch (ex: Throwable) {
            onFailure(null)
        }
    }

    suspend fun insert(
        learningBox: LearningBox,
    ) : RepoResult<Unit> {
        return try {
            repository.insert(learningBox)
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }

    suspend fun delete(boxNumber: Int, learningObjectId: UUID) {
        return repository.delete(boxNumber, learningObjectId)
    }
}