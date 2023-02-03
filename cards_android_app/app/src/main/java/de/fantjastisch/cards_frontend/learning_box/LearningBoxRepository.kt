package de.fantjastisch.cards_frontend.learning_box

import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import org.openapitools.client.models.ErrorResponseEntity
import java.util.*

/**
 * Repository, welches Lernboxen speichert.
 *
 * @property repository Das entsprechende InternalRepository.
 *
 * @author Semjon Nirmann, Jessica Repty, Freja Sender
 */
class LearningBoxRepository(
    private val repository: InternalLearningBoxRepository = InternalLearningBoxRepository(
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
                repository.getAllBoxesForLearningObjectWithNrOfCards(learningObjectId)
            RepoResult.Success(allBoxesForLearningObject)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
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
            val cardsFromLearningObject = repository.getCardsFromLearningObject(learningObjectId)
            RepoResult.Success(cardsFromLearningObject)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }


//    suspend fun findByBoxId(
//        learningBoxId: UUID,
//        onSuccess: (LearningBox) -> Unit,
//        onFailure: (errors: ErrorResponseEntity?) -> Unit
//    ) {
//        try {
//            onSuccess(repository.findById(learningBoxId))
//        } catch (ex: Throwable) {
//            onFailure(null)
//        }
//    }

    /**
     * Fügt eine Lernbox in die Datenbank ein.
     *
     * @param learningBox Lernbox, welche eingefügt werden soll.
     * @return RepoResult Succes/ServerError.
     */
    suspend fun insert(
        learningBox: LearningBox,
    ) : RepoResult<Unit> {
        return try {
            repository.insert(learningBox)
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }

    /**
     * Löscht eine Lernbox aus der Datenbank.
     *
     * @param boxNumber Nummer der zu löschenden Lernbox.
     * @param learningObjectId Id des zugehörigen Lernobjektes, der Lernbox.
     */
    suspend fun delete(boxNumber: Int, learningObjectId: UUID) {
        return repository.delete(boxNumber, learningObjectId)
    }
}