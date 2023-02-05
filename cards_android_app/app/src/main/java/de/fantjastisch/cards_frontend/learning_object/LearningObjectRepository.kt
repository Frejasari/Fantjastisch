package de.fantjastisch.cards_frontend.learning_object

import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.RepoResult.*
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType.*
import java.util.*

/**
 * Repository, welches Lernobjekte speichert.
 * Kommuniziert mit [InternalLearningObjectRepository]
 *
 * @property repository Das entsprechende InternalRepository.
 *
 * @author Semjon Nirmann, Jessica Repty, Freja Sender
 */
class LearningObjectRepository(
    private val repository: InternalLearningObjectRepository = InternalLearningObjectRepository(
        AppDatabase.database.learningObjectDao()
    )
) {

    /**
     * Holt alle Lernobjekte aus der Datenbank.
     *
     * @return Liste von allen Lernobjekten.
     */
    suspend fun getAll(): RepoResult<List<LearningObject>> {
        return try {
            val learningObjects = repository.getAll()
            Success(learningObjects)
        } catch (ex: Throwable) {
            ServerError(UNEXPECTED_ERROR)
        }
    }

    /**
     * Holt, das Lernobjekt mit der übergebenen ID aus der Datenbank.
     *
     * @param id Id, des gesuchten Lernobjektes.
     * @return Gesuchtes Lernobjekt.
     */
    suspend fun findById(
        id: UUID
    ): RepoResult<LearningObject> {
        return try {
            val found = repository.findById(id)
            Success(found)
        } catch (ex: Throwable) {
            ServerError(UNEXPECTED_ERROR)
        }
    }

    /**
     * Fügt ein Lernobjekt in die Datenbank ein.
     *
     * @param learningObject Lernobjekt, welches in die Datenbank eingefügt werden soll.
     * @return RepoResult Succes/ServerError.
     */
    suspend fun insert(
        learningObject: LearningObject
    ): RepoResult<Unit> {
        return try {
            repository.insert(learningObject)
            Success(Unit)
        } catch (ex: Throwable) {
            ServerError(UNEXPECTED_ERROR)
        }
    }

    /**
     * Löscht ein Lernobjekt aus der Datenbank.
     *
     * @param id Id, des Lernobjektes, welches gelöscht werden soll.
     * @return RepoResult Succes/ServerError.
     */
    suspend fun delete(
        id: UUID
    ): RepoResult<Unit> {
        return try {
            repository.delete(id)
            Success(Unit)
        } catch (ex: Throwable) {
            ServerError(UNEXPECTED_ERROR)
        }
    }
}