package de.fantjastisch.cards_frontend.learning_object

import org.openapitools.client.models.ErrorResponseEntity


class LearningObjectRepository(val repository: InternalLearningObjectRepository) {

    fun getAll(): List<LearningObject>{
        return repository.getAll()
    }

    fun findById(id: String) : LearningObject {
        return repository.findById(id)
    }

    fun insert(learningObject: LearningObject,
               onSuccess: () -> Unit,
               onFailure: (errors: ErrorResponseEntity?) -> Unit){
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