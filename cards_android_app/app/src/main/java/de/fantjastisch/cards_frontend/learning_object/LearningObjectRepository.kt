package de.fantjastisch.cards_frontend.learning_object

import org.openapitools.client.models.ErrorResponseEntity


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

    fun findById(id: String): LearningObject {
        return repository.findById(id)
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

    fun delete(id: String) {
        return repository.delete(id)
    }
}