package de.fantjastisch.cards_frontend.learning_overview.delete

import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import java.util.*

/**
 * Kapselt die Logik für das [DeleteLearningObjectViewModel]
 *
 * @property learningObjectRepository Lernobjekt Repository
 *
 * @author
 */
class DeleteLearningObjectModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
) {


    /**
     * Löscht ein Lernobjekt, indem eine Anfrage an das Repository gesendet wird.
     *
     * @param learningObjectId Die UUID des zu löschenden Lernobjekts.
     * @return RepoResult<Unit> TODO
     */
    suspend fun deleteLearningObject(learningObjectId: UUID): RepoResult<Unit> =
        learningObjectRepository.delete(id = learningObjectId)
    // TODO delete boxes
}