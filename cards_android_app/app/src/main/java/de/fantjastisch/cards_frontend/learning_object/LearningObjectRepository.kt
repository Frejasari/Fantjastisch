package de.fantjastisch.cards_frontend.learning_object

import de.fantjastisch.cards_frontend.config.AppDatabase
import org.openapitools.client.models.ErrorResponseEntity
import java.util.*


class LearningObjectRepository(
    private val repository: InternalLearningObjectRepository = InternalLearningObjectRepository(
        AppDatabase.database.learningObjectDao()
    )
) {

    fun getAll(
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

    fun findById(
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

    fun insert(
        learningObject: LearningObject,
        onSuccess: () -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        try {
            repository.insert(learningObject)
            onSuccess()
        } catch (ex: Throwable) {
            onFailure(null)
        }
    }

    fun delete(
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