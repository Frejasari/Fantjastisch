package de.fantjastisch.cards_frontend.learning_object

import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.util.RepoResult
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
            RepoResult.Success(learningObjects)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
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
            RepoResult.Success(found)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
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
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
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
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }
}