package de.fantjastisch.cards_frontend.learning_object

import org.openapitools.client.models.ErrorResponseEntity
import java.util.*


class LearningObjectRepository(val repository: InternalLearningObjectRepository) {

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

    fun delete(id: UUID) {
        return repository.delete(id)
    }
}