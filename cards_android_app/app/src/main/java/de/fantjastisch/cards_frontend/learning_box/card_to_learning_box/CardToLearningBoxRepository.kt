package de.fantjastisch.cards_frontend.learning_box.card_to_learning_box

import androidx.room.Transaction
import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.util.RepoResult
import java.util.*

/**
 * Repository, welches Karten in Lernboxen speichert.
 *
 * @property repository Das entsprechende InternalRepository.
 *
 * @author Semjon Nirmann, Jessica Repty, Freja Sender
 */
class CardToLearningBoxRepository(
    private val repository: InternalCardToLearningBoxRepository
    = InternalCardToLearningBoxRepository(AppDatabase.database.cardToLearningBoxDao())
) {

    /**
     * Sendet eine Anfrage an [InternalCardToLearningBoxRepository] und kriegt im Erfolgsfall für die
     * übergebene Id, die passenden Karten aus der Lernbox.
     *
     * @param learningBoxId Id, der Lernbox.
     * @return RepoResult<List<UUID>> OnSuccess: Liste an UUID's für Karten.
     */
    suspend fun getCardIdsForBox(
        learningBoxId: UUID
    ): RepoResult<List<UUID>> {
        return try {
            val cardIdsForBox = repository.getCardIdsForBox(learningBoxId)
            RepoResult.Success(cardIdsForBox)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }

    /**
     * Sendet eine Anfrage an [InternalCardToLearningBoxRepository] um eine Liste an
     * Karten UUID's verknüpft mit der Lernbox-Id in die Datenbank zu speichern.
     *
     * @param cardIds Liste an Karten UUID'S, welche in Lernbox gespeichert werden sollen.
     * @param learningBoxId Lernbox, in welche die Karten-Id's gespeichert wird.
     * @return RepoResponse<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun insertCards(
        cardIds: List<UUID>,
        learningBoxId: UUID,
    ): RepoResult<Unit> {
        return try {
            repository.insertCards(
                cardIds.map {
                    CardToLearningBox(
                        learningBoxId = learningBoxId,
                        cardId = it
                    )
                })
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }

    /**
     * Sendet eine Anfrage an [InternalCardToLearningBoxRepository] um eine Liste an
     * Karten UUID's in eine Lernbox zu speichern, die Lernbox wird vorab geleert.
     *
     * @param selected Liste an Karten UUID'S, welche neu in die Lernbox gespeichert werden.
     * @param learningBoxId Lernbox, in welche die neuen Karten-Id's gespeichert wird.
     * @return RepoResponse<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    @Transaction
    suspend fun updateBoxCards(
        selected: List<UUID>,
        learningBoxId: UUID,
    ): RepoResult<Unit> {
        return try {
            repository.deleteAllCardsFromLearningBox(learningBoxId = learningBoxId)
            repository.insertCards(selected.map {
                CardToLearningBox(
                    learningBoxId = learningBoxId,
                    cardId = it
                )
            })
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }

    /**
     * Sendet eine Anfrage an [InternalCardToLearningBoxRepository] und kriegt im Erfolgsfall für die
     * übergebene Id des Lernobjekt, die passenden Karten aus dem Lernobjekt.
     *
     * @param learningObjectId Id, des Lernobjektes.
     * @return RepoResult<List<UUID>> OnSuccess: Liste an UUID's für Karten.
     */
    suspend fun getAllCardsForLearningObject(
        learningObjectId: UUID,
    ): RepoResult<List<UUID>> {
        return try {
            val allCardsForLearningObject =
                repository.getAllCardsForLearningObject(learningObjectId = learningObjectId)
            RepoResult.Success(allCardsForLearningObject)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }

    /**
     * Sendet eine Anfrage an [InternalCardToLearningBoxRepository] um die übergebenen Karten-Id's
     * von eine Lernbox in eine andere Lernbox zu schieben.
     *
     * @param from Id, der Lernbox aus welcher die Karten kommen.
     * @param to Id, der Lernbox in welche die Karten verschoben werden.
     * @param cardIds Liste an Karten-Id's, welche verschoben werden sollen.
     * @return RepoResponse<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    @Transaction
    suspend fun moveCards(from: UUID, to: UUID, cardIds: List<UUID>): RepoResult<Unit> {
        return try {
            repository.deleteCardsFromBox(cardIds = cardIds, learningBoxId = from)
            repository.insertCards(cardIds.map {
                CardToLearningBox(
                    learningBoxId = to,
                    cardId = it
                )
            })
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }

    /**
     * Löscht eine Karte aus allen Lernboxen.
     *
     * @param cardId Id, der zu löschenden Karte.
     * @return RepoResponse<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun deleteCard(cardId: UUID): RepoResult<Unit> {
        return try {
            repository.deleteCardFromAllBoxes(cardId = cardId)
            RepoResult.Success(Unit)
        } catch (ex: Throwable) {
            RepoResult.ServerError()
        }
    }
}