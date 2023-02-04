package de.fantjastisch.cards_frontend.learning_system

import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.client
import de.fantjastisch.cards_frontend.infrastructure.enqueue
import de.fantjastisch.cards_frontend.infrastructure.toRepoResponse
import org.openapitools.client.apis.LearningSystemApi
import org.openapitools.client.models.*
import retrofit2.awaitResponse
import java.util.*


/**
 * Repository kommuniziert mit LearningSystemBackend
 *
 * @author Jessica Repty, Semjon Nirmann, Freja Sender
 */
class LearningSystemRepository {

    private val service = client.createService(LearningSystemApi::class.java)

    /**
     * Sendet eine Datenbankanfrage an das Backend und kriegt im Erfolgsfall für die
     * übergebene Id, das passende Lernsystem.
     *
     * @param id Id, des gesuchten Lernsystems.
     * @return RepoResponse<LearningSystemEntity> OnSuccess: Lernsystem als [LearningSystemEntity]-Entität.
     */
    suspend fun getLearningSystem(
        id: UUID,
    ): RepoResult<LearningSystemEntity> = service.getLearningSystem(id)
        .awaitResponse()
        .toRepoResponse()

    /**
     * Sendet eine Anfrage an das Backend und kriegt im Erfolgsfall alle
     * vorhandenen Lernsysteme zurück.
     *
     * @return RepoResult<List<LearningSystemEntity>> OnSuccess: Liste an
     *   Lernsystemen als [LearningSystemEntity]-Entität.
     */
    suspend fun getPage() = service.getLearningSystemList()
        .awaitResponse()
        .toRepoResponse()

    /**
     * Sendet eine Anfrage an das Backend, um ein Lernsystem in die Datenbank zu speichern.
     *
     * @param learningSystem Lernsystem, welches erzeugt werden soll.
     * @return RepoResponse<String> OnSuccess: die ID der eingefügten [CreateLearningSystemEntity]-Entität
     */
    suspend fun createLearningsystem(
        learningSystem: CreateLearningSystemEntity,
    ) = service.createLearningSystem(learningSystem)
        .awaitResponse()
        .toRepoResponse()

    suspend fun updateLearningSystem(
        learningSystem: UpdateLearningSystemEntity) = service.updateLearningSystem(learningSystem)
        .awaitResponse()
        .toRepoResponse()
}