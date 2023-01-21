package de.fantjastisch.cards_frontend.learning_box

import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.ErrorResponseEntity
import java.util.*

class LearningBoxRepository(val repository: InternalLearningBoxRepository) {

    fun getAllBoxesForLearningObject(
        learningObjectId: UUID,
        onSuccess: (List<LearningBox>) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
            try {
                onSuccess(repository.getAllBoxesForLearningObject(learningObjectId))
            } catch (ex: Throwable) {
                onFailure(null)
            }
    }

    fun getCardsFromLearningBoxInLearningObject(
        learningObjectId: UUID,
        onSuccess: (List<Int>) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
        try {
            onSuccess(repository.getCardsFromLearningObject(learningObjectId))
        } catch  (ex: Throwable) {
            onFailure(null)
        }
    }


    fun findByBoxId(learningBoxId: UUID,
                 onSuccess: (LearningBox) -> Unit,
                 onFailure: (errors: ErrorResponseEntity?) -> Unit) {
        try {
            onSuccess(repository.findById(learningBoxId))
        } catch  (ex: Throwable) {
            onFailure(null)
        }
    }

    fun insert(
        learningBox: LearningBox,
        onSuccess: () -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) {
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