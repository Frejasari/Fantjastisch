package de.fantjastisch.cards_frontend.learning_system

import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.client
import de.fantjastisch.cards_frontend.infrastructure.enqueue
import de.fantjastisch.cards_frontend.infrastructure.toRepoResponse
import org.openapitools.client.apis.LearningSystemApi
import org.openapitools.client.models.CreateLearningSystemEntity
import org.openapitools.client.models.ErrorResponseEntity
import org.openapitools.client.models.LearningSystemEntity
import org.openapitools.client.models.UpdateLearningSystemEntity
import retrofit2.awaitResponse
import java.util.*

class LearningSystemRepository {

    private val service = client.createService(LearningSystemApi::class.java)

    suspend fun getLearningSystem(
        id: UUID,
    ): RepoResult<LearningSystemEntity> = service.getLearningSystem(id)
        .awaitResponse()
        .toRepoResponse()

    suspend fun getPage(
        onSuccess: (List<LearningSystemEntity>) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) = service.getLearningSystemList().enqueue(onSuccess, onFailure)

    suspend fun createLearningsystem(
        learningSystem: CreateLearningSystemEntity,
        onSuccess: (String) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) = service.createLearningSystem(learningSystem).enqueue(onSuccess, onFailure)

    suspend fun updateLearningSystem(
        learningSystem: UpdateLearningSystemEntity,
        onSuccess: (Unit) -> Unit,
        onFailure: (errors: ErrorResponseEntity?) -> Unit
    ) = service.updateLearningSystem(learningSystem).enqueue(onSuccess, onFailure)
}