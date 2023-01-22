package de.fantjastisch.cards_frontend.learning_box

import androidx.room.*
import java.util.*

class InternalLearningBoxRepository(private val dao: LearningBoxDao) : LearningBoxDao by dao

@Dao
interface LearningBoxDao {

    @Query(
        "SELECT lb.id, lb.box_number, lb.label, lb.learning_object_id, count(*) as nr_of_cards FROM learning_box lb " +
                "left join card_to_learning_box clb on lb.id = clb.learning_box_id " +
                "where lb.learning_object_id = :learningObjectId " +
                "group by (lb.id) " +
                "ORDER BY box_number ASC"
    )
    suspend fun getAllBoxesForLearningObjectWithNrOfCards(learningObjectId: UUID): List<LearningBoxWitNrOfCards>

    @Query("SELECT * FROM learning_box where learning_object_id = :learningObjectId ORDER BY box_number ASC")
    suspend fun getAllBoxesForLearningObject(learningObjectId: UUID): List<LearningBox>

    @Query("SELECT * FROM learning_box WHERE id = :learningBoxId")
    suspend fun findById(learningBoxId: UUID): LearningBox

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(learningBox: LearningBox)

    @Query("DELETE FROM learning_box WHERE box_number = :boxNumber and learning_object_id = :learningObjectId")
    suspend fun delete(boxNumber: Int, learningObjectId: UUID)

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