package de.fantjastisch.cards_frontend.learning_box

import androidx.room.*
import org.openapitools.client.models.CardEntity
import java.util.*

class InternalLearningBoxRepository(private val dao: LearningBoxDao) : LearningBoxDao by dao

@Dao
interface LearningBoxDao {
    @Query("SELECT * FROM learning_box where learning_object_id = :learningObjectId ORDER BY box_number ASC")
    fun getAllBoxesForLearningObject(learningObjectId: UUID): List<LearningBox>

    @Query("SELECT * FROM learning_box WHERE id = :learningBoxId")
    fun findById(learningBoxId: UUID): LearningBox

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(learningBox: LearningBox)

    @Query("DELETE FROM learning_box WHERE box_number = :boxNumber and learning_object_id = :learningObjectId")
    fun delete(boxNumber: Int, learningObjectId: UUID)

    @Query("    select count(*)" +
            "    from learning_box " +
            "    join card_to_learning_box on learning_box_id = id " +
            "    group by learning_box_id, learning_object_id " +
            "    having learning_object_id = :learningObjectId" +
            "    order by box_number asc")
    fun getCardsFromLearningObject(learningObjectId: UUID): List<Int>
}