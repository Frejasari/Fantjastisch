package de.fantjastisch.cards_frontend.learning_overview

import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository


/**
 * Kapselt die Logik für das [LearningOverviewViewModel]
 * Fungiert als Vermittler zwischen Repository und ViewModel
 *
 * @property learningObjectRepository Lernobjekt Repository
 *
 * @author Freja Sender, Semjon Nirmann
 */
class LearningOverviewModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository()
) {

    /**
     * Holt alle Lernobjekte, indem eine Anfrage an das Repository gesendet wird.
     * Im Erfolgsfall werden alle Lernobjekte zurückgegeben.
     *
     * @return RepoResult<List<LearningObject>> OnSuccess: Liste an Lernobjekten
     *   als [LearningObject]-Entitäten
     */
    suspend fun initializePage(): RepoResult<List<LearningObject>> =
        learningObjectRepository.getAll()
}