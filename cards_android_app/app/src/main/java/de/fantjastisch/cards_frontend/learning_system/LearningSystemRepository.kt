package de.fantjastisch.cards_frontend.learning_system

import de.fantjastisch.cards_frontend.config.client
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.awaitResponse
import org.openapitools.client.apis.LearningSystemApi
import org.openapitools.client.models.CreateLearningSystemEntity
import org.openapitools.client.models.LearningSystemEntity
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
    suspend fun getLearningSystem(id: UUID): RepoResult<LearningSystemEntity> =
        service.getLearningSystem(id).awaitResponse()


    /**
     * Sendet eine Anfrage an das Backend und kriegt im Erfolgsfall alle
     * vorhandenen Lernsysteme zurück.
     *
     * @return RepoResult<List<LearningSystemEntity>> OnSuccess: Liste an
     *   Lernsystemen als [LearningSystemEntity]-Entität.
     */
    suspend fun getPage(): RepoResult<List<LearningSystemEntity>> =
        service.getLearningSystemList().awaitResponse()


    /**
     * Sendet eine Anfrage an das Backend, um ein Lernsystem in die Datenbank zu speichern.
     *
     * @param learningSystem Lernsystem, welches erzeugt werden soll.
     * @return RepoResponse<String> OnSuccess: die ID der eingefügten [CreateLearningSystemEntity]-Entität
     */
    suspend fun createLearningsystem(learningSystem: CreateLearningSystemEntity): RepoResult<String> =
        service.createLearningSystem(learningSystem).awaitResponse()

}