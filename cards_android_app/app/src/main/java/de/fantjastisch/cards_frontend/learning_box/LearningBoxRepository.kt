package de.fantjastisch.cards_frontend.learning_box

import org.openapitools.client.models.ErrorResponseEntity
import java.util.*

class LearningBoxRepository(val repository: InternalLearningBoxRepository) {

    fun getAllBoxesForLearningObject(learningObjectId: UUID): List<LearningBox> {
        return repository.getAllBoxesForLearningObject(learningObjectId)
    }

    fun findById(learningBoxId: UUID): LearningBox {
        return repository.findById(learningBoxId)
    }

    fun insert(learningBox: LearningBox,
               onSuccess: () -> Unit,
               onFailure: (errors: ErrorResponseEntity?) -> Unit){
        try {
            repository.insert(learningBox)
            onSuccess()
        } catch (ex: Throwable) {
            onFailure(null)
        }
    }

    fun delete(boxNumber: Int, learningObjectId: UUID) {
        return repository.delete(boxNumber, learningObjectId)
    }
}