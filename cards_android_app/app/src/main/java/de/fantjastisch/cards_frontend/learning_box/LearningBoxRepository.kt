package de.fantjastisch.cards_frontend.learning_box

import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.RepoResult.ServerError
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType.UNEXPECTED_ERROR
import java.util.*

/**
 * Repository, welches Lernboxen speichert.
 *
 * @property learningBoxRepository Das entsprechende InternalRepository.
 *
 * @author Semjon Nirmann, Jessica Repty, Freja Sender
 */
class LearningBoxRepository(
    private val learningBoxRepository: InternalLearningBoxRepository = InternalLearningBoxRepository(
        AppDatabase.database.learningBoxDao()
    )
) {

    /**
     * Holt alle Lernboxen eines Lernobjektes mit der Anzahl an Karten aus der Datenbank.
     *
     * @param learningObjectId Id des Lernobjektes, zu welchem die Lernboxen geholt werden.
     * @return Liste von Lernboxen inkl. der Anzahl an Karten enthalten.
     */
    suspend fun getAllBoxesForLearningObject(
        learningObjectId: UUID
    ): RepoResult<List<LearningBoxWitNrOfCards>> {
        return try {
            val allBoxesForLearningObject =
                learningBoxRepository.getAllBoxesForLearningObjectWithNrOfCards(learningObjectId)
            RepoResult.Success(allBoxesForLearningObject)
        } catch (ex: Throwable) {
            ServerError(UNEXPECTED_ERROR)
        }
    }

    /**
     * Holt die Anzahl an Karten von den Lernboxen des Lernobjektes aus der Datenbank.
     *
     * @param learningObjectId Id des Lernobjektes, zu welchem die Anzahl der Karten geholt werden.
     * @return Liste von Integern, die die Anzahl der Karten in der jeweiligen Lernbox widerspiegeln.
     */
    suspend fun getCardsFromLearningBoxInLearningObject(
        learningObjectId: UUID
    ): RepoResult<List<Int>> {
        return try {
            val cardsFromLearningObject =
                learningBoxRepository.getCardsFromLearningObject(learningObjectId)
            RepoResult.Success(cardsFromLearningObject)
        } catch (ex: Throwable) {
            ServerError(UNEXPECTED_ERROR)
        }
    }


    /**
     * Löscht alle Lernboxen zu einem Lernobjekt
     *
     * @param learningObjectId Id des zugehörigen Lernobjektes
     */
    suspend fun deleteAllBoxesForObject(learningObjectId: UUID) {
        return learningBoxRepository.deleteAllBoxesForObject(learningObjectId = learningObjectId)
    }
}