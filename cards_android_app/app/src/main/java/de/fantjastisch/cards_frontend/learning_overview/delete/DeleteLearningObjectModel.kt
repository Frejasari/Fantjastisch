package de.fantjastisch.cards_frontend.learning_overview.delete

import androidx.room.Transaction
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import de.fantjastisch.cards_frontend.util.RepoResult
import java.util.*

/**
 * Kapselt die Logik für das [DeleteLearningObjectViewModel]
 *
 * @property learningObjectRepository Lernobjekt Repository
 *
 * @author Semjon Nirmann
 */
class DeleteLearningObjectModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
) {

    /**
     * Löscht ein Lernobjekt, indem eine Anfrage an das Repository gesendet wird.
     *
     * @param learningObjectId Die UUID des zu löschenden Lernobjekts.
     * @return RepoResult<Unit> Ein parametrisiertes Objekt, das darstellt, ob alle Persistenzoperationen erfolgreich durchgeführt
     * werden konnten, oder nicht.
     */
    @Transaction
    suspend fun deleteLearningObject(learningObjectId: UUID): RepoResult<Unit> {
        return try {
            learningObjectRepository.delete(id = learningObjectId)
            learningBoxRepository.deleteAllBoxesForObject(learningObjectId = learningObjectId)
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }
}