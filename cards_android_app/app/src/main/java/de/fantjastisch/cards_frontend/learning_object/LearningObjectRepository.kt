package de.fantjastisch.cards_frontend.learning_object

import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import org.openapitools.client.models.ErrorResponseEntity
import java.util.*


class LearningObjectRepository(
    private val repository: InternalLearningObjectRepository = InternalLearningObjectRepository(
        AppDatabase.database.learningObjectDao()
    )
) {

    suspend fun getAll(
        onSuccess: (List<LearningObject>) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        try {
            val learningObjects = repository.getAll()
            onSuccess(learningObjects)
        } catch (ex: Throwable) {
            onFailure(null)
        }
    }

    suspend fun findById(
        id: UUID,
        onSuccess: (LearningObject) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        try {
            onSuccess(repository.findById(id))
        } catch (ex: Throwable) {
            onFailure(null)
        }
    }

    suspend fun insert(
        learningObject: LearningObject
    ) : RepoResult<Unit> {
        return try {
            repository.insert(learningObject)
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }

    suspend fun delete(
        id: UUID,
        onSuccess: () -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        try {
            repository.delete(id)
            onSuccess()
        } catch (ex: Throwable) {
            onFailure(null)
        }
    }
}