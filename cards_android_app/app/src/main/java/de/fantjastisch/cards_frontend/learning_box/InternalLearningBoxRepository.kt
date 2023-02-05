package de.fantjastisch.cards_frontend.learning_box

import androidx.room.*
import java.util.*

/**
 * Internal Repository für Lernboxen. Enthält die Datenbankanfragen.
 *
 * @author Semjon Nirmann, Jessica Repty, Freja Sender
 */
class InternalLearningBoxRepository(private val dao: LearningBoxDao) : LearningBoxDao by dao

@Dao
interface LearningBoxDao {

    /**
     * Holt alle Lernboxen eines Lernobjektes mit der Anzahl an Karten aus der Datenbank.
     *
     * @param learningObjectId Id, des Lernobjektes.
     * @return Liste mit [LearningBoxWithNrOfCards]-Entität pro Lernbox, aufsteigend nach Lernbox-Nr. sortiert.
     */
    @Query(
        "SELECT lb.id, lb.box_number, lb.label, lb.learning_object_id, " +
                "(select count(*) from card_to_learning_box clb where clb.learning_box_id = lb.id) as nr_of_cards " +
                "FROM learning_box lb " +
                "where lb.learning_object_id = :learningObjectId " +
                "group by (lb.id) " +
                "ORDER BY box_number ASC"
    )
    suspend fun getAllBoxesForLearningObjectWithNrOfCards(learningObjectId: UUID): List<LearningBoxWithNrOfCards>

    /**
     * Fügt ein oder mehrere  Lernboxen in die Datenbank ein.
     *
     * @param learningBoxes Lernboxen, welche eingefügt werden soll.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(learningBoxes: List<LearningBox>)

    /**
     * Löscht
     *
     * @param learningObjectId Id, des zugehörigen Lernobjektes.
     */
    @Query("DELETE FROM learning_box WHERE learning_object_id = :learningObjectId")
    suspend fun deleteAllBoxesForObject(learningObjectId: UUID)

    /**
     * Holt die Anzahl an Karten eines Lernobjektes pro Lernbox aus der Datenbank.
     *
     * @param learningObjectId Id, des Lernobjektes.
     * @return  Liste von Integern, die die Anzahl der Karten in der jeweiligen Lernbox widerspiegeln.
     */
    @Query(
        "select count(card_id) " +
                "from learning_box " +
                "left join card_to_learning_box on learning_box_id = id " +
                "where learning_object_id = :learningObjectId " +
                "group by id, learning_object_id " +
                "order by box_number asc"
    )
    suspend fun getCardsFromLearningObject(learningObjectId: UUID): List<Int>
}