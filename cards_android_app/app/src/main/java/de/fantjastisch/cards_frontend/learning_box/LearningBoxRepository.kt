package de.fantjastisch.cards_frontend.learning_box

import androidx.room.*
import org.openapitools.client.models.CardEntity
import java.util.*

class LearningBoxRepository(private val dao: LearningBoxDao): LearningBoxDao by dao

@Dao
interface LearningBoxDao {
    @Query("SELECT * FROM learning_box where learning_object_id = :learningObjectId")
    fun getAllBoxesForLearningObject(learningObjectId: UUID): List<LearningBox>

    @Query("SELECT card_ids FROM learning_box WHERE box_number = :boxNumber and learning_object_id = :learningObjectId")
    fun getAllCardIdsForBox(boxNumber: Int, learningObjectId: UUID): List<UUID>

    @Query("SELECT * FROM learning_box WHERE box_number = :boxNumber and learning_object_id = :learningObjectId")
    fun findById(boxNumber: Int, learningObjectId: UUID): LearningBox

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(learningBox: LearningBox)

    @Query("DELETE FROM learning_box WHERE box_number = :boxNumber and learning_object_id = :learningObjectId")
    fun delete(boxNumber: Int, learningObjectId: UUID)
}