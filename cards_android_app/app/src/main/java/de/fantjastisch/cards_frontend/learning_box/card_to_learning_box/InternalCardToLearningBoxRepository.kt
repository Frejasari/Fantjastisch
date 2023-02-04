package de.fantjastisch.cards_frontend.learning_box.card_to_learning_box

import androidx.room.*
import java.util.*

/**
 * Internal Repository für Karten in Lernboxen. Enthält die Datenbankanfragen.
 *
 * @author Semjon Nirmann, Jessica Repty, Freja Sender
 */
class InternalCardToLearningBoxRepository(private val dao: CardToLearningBoxDao) :
    CardToLearningBoxDao by dao

@Dao
interface CardToLearningBoxDao {

    /**
     * Fügt die übergebene Liste an [CardToLearningBox]-Entitäten in die Datenbank ein.
     *
     * @param cardToLearningBoxes Einzufügende Liste an [CardToLearningBox]-Entitäten.
     */
    @Insert
    suspend fun insertCards(cardToLearningBoxes: List<CardToLearningBox>)

    /**
     * Löscht alle Dateneinträge der übergebenen Lernbox-Id.
     *
     * @param learningBoxId Id, der zu löschenden Lernbox.
     */
    @Query("DELETE FROM card_to_learning_box WHERE learning_box_id = :learningBoxId;")
    suspend fun deleteAllCardsFromLearningBox(learningBoxId: UUID)

    /**
     * Löscht eine Liste an UUID´s (Karten) von der übergebenen Lernbox.
     *
     * @param cardIds Zu löschenden Id´s der Karten als Liste.
     * @param learningBoxId Id, der Lernbox aus welcher die Karten gelöscht werden sollen.
     */
    @Query("DELETE FROM card_to_learning_box WHERE learning_box_id = :learningBoxId and card_id in (:cardIds);")
    suspend fun deleteCardsFromBox(cardIds: List<UUID>, learningBoxId: UUID)

    /**
     * Löscht die übergebene Karte-Id aus allen Lernboxen.
     *
     * @param cardId Id, der überall zu löschenden Karte.
     */
    @Query("DELETE FROM card_to_learning_box WHERE card_id = :cardId;")
    suspend fun deleteCardFromAllBoxes(cardId: UUID)

    /**
     * Holt für eine Lernbox-Id die Anzahl, der in der Lernbox vorhandenen, Karten aus der Datenbank.
     *
     * @param learningBoxId Id, der Lernbox bei welcher die Anzahl der Karten zurückgegeben werden.
     * @return Anzahl der, in der Lernbox vorhandenen, Karten.
     */
    @Query(
        "SELECT COUNT(*) FROM learning_box " +
                "JOIN card_to_learning_box " +
                "ON learning_box_id = id " +
                "GROUP BY learning_box_id, learning_object_id " +
                "HAVING learning_box_id = :learningBoxId;"
    )
    suspend fun getNumOfCardsFromLearningBoxId(learningBoxId: UUID): Int

    /**
     * Holt für eine Lernbox, alle in jener enthaltende Karten-Id's.
     *
     * @param learningBoxId Id, der Lernbox bei welcher die Karten-Id's gesucht werden.
     * @return Liste an UUID's für Karten.
     */
    @Query("SELECT card_id FROM card_to_learning_box WHERE learning_box_id = :learningBoxId;")
    suspend fun getCardIdsForBox(learningBoxId: UUID): List<UUID>

    /**
     * Holt für ein Lernobjekt, alle in jener enthaltenden Karten-Id's.
     *
     * @param learningObjectId Id, des Lernobjektes bei welcher die Karten-Id's gesucht werden.
     * @return Liste an UUID's für Karten.
     */
    @Query("SELECT card_id FROM learning_box JOIN card_to_learning_box on id = learning_box_id WHERE learning_object_id = :learningObjectId;")
    suspend fun getAllCardsForLearningObject(learningObjectId: UUID): List<UUID>
}