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

    suspend fun getAll() : RepoResult<List<LearningObject>> {
        return try {
            val learningObjects = repository.getAll()
            RepoResult.Success(learningObjects)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }

    suspend fun findById(
        id: UUID
    ) : RepoResult<LearningObject> {
        return try {
            val found = repository.findById(id)
            RepoResult.Success(found)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
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
        id: UUID
    ) : RepoResult<Unit> {
        return try {
            repository.delete(id)
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }
}