package de.fantjastisch.cards_frontend.learning_system

import de.fantjastisch.cards_frontend.infrastructure.client
import de.fantjastisch.cards_frontend.infrastructure.enqueue
import org.openapitools.client.apis.LearningSystemApi
import org.openapitools.client.models.CreateLearningSystemEntity
import org.openapitools.client.models.ErrorResponseEntity
import org.openapitools.client.models.LearningSystemEntity
import org.openapitools.client.models.UpdateLearningSystemEntity
import java.util.*

class LearningSystemRepository {

    val service = client.createService(LearningSystemApi::class.java)

    fun getCategory(id: UUID,
                    onSuccess: (LearningSystemEntity) -> Unit,
                    onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) = service.getLearningSystem(id).enqueue(onSuccess, onFailure)

    fun getPage(
            onSuccess: (List<LearningSystemEntity>) -> Unit,
            onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) = service.getLearningSystemList().enqueue(onSuccess, onFailure)

    fun createLearningsystem(
            learningSystem: CreateLearningSystemEntity,
            onSuccess: (String) -> Unit,
            onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
            service.createLearningSystem(learningSystem).enqueue(onSuccess, onFailure)

    fun updateLearningSystem(
            learningSystem: UpdateLearningSystemEntity,
            onSuccess: (Unit) -> Unit,
            onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) =
            service.updateLearningSystem(learningSystem).enqueue(onSuccess, onFailure)
}