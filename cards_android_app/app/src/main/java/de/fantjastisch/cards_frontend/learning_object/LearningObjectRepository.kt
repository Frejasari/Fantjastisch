package de.fantjastisch.cards_frontend.learning_object

import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.learning_box.InternalLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBox
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.InternalCardToLearningBoxRepository
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.RepoResult.ServerError
import de.fantjastisch.cards_frontend.util.RepoResult.Success
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType.UNEXPECTED_ERROR
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
    ),
    private val learningBoxRepository: InternalLearningBoxRepository = InternalLearningBoxRepository(
        AppDatabase.database.learningBoxDao()
    ),
    private val cardToLearningBoxRepository: InternalCardToLearningBoxRepository
    = InternalCardToLearningBoxRepository(AppDatabase.database.cardToLearningBoxDao())
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
     * Holt, das Lernobjekt mit der ├╝bergebenen ID aus der Datenbank.
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
     * F├╝gt ein Lernobjekt inklusive Boxen und Karten in der ersten Box in die Datenbank ein.
     *
     * @param learningObject Lernobjekt, welches in die Datenbank eingef├╝gt werden soll.
     * @param boxLabels die Box Labels des dazugeh├Ârigen Lernsystems
     * @param cards die initialen Karten
     *
     * @return RepoResult Success/ServerError.
     */
    suspend fun insert(
        learningObject: LearningObject,
        boxLabels: List<String>,
        cards: MutableList<UUID>
    ): RepoResult<Unit> {
        return try {
            repository.insert(learningObject)

            val learningBoxes = boxLabels.mapIndexed { index, label ->
                LearningBox(learningObjectId = learningObject.id, boxNumber = index, label = label)
            }

            learningBoxRepository.insert(learningBoxes)

            val learningBoxList = cards.map { cardId ->
                CardToLearningBox(
                    learningBoxId = learningBoxes[0].id,
                    cardId = cardId
                )
            }.toList()

            cardToLearningBoxRepository.insertCards(learningBoxList)
            Success(Unit)
        } catch (ex: Throwable) {
            ServerError(UNEXPECTED_ERROR)
        }
    }

    /**
     * L├Âscht ein Lernobjekt aus der Datenbank.
     *
     * @param id Id, des Lernobjektes, welches gel├Âscht werden soll.
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