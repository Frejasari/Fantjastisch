package de.fantjastisch.cards_frontend.learning_box

import androidx.room.*
import java.util.*

class InternalLearningBoxRepository(private val dao: LearningBoxDao) : LearningBoxDao by dao

@Dao
interface LearningBoxDao {
    @Query("SELECT * FROM learning_box where learning_object_id = :learningObjectId")
    fun getAllBoxesForLearningObject(learningObjectId: UUID): List<LearningBox>

    @Query("SELECT * FROM learning_box WHERE id = :learningBoxId")
    fun findById(learningBoxId: UUID): LearningBox

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(learningBox: LearningBox)

    @Query("DELETE FROM learning_box WHERE box_number = :boxNumber and learning_object_id = :learningObjectId")
    fun delete(boxNumber: Int, learningObjectId: UUID)
}