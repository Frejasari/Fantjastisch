package de.fantjastisch.cards_frontend.learning_object

import androidx.room.*

class InternalLearningObjectRepository(private val dao: LearningObjectDao) : LearningObjectDao by dao

@Dao
interface LearningObjectDao {
    @Query("SELECT * FROM learning_object")
    fun getAll(): List<LearningObject>

    @Query("SELECT * FROM learning_object WHERE id = :id")
    fun findById(id: String): LearningObject

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(learningObject: LearningObject)

    @Query("delete from learning_object where id=:id")
    fun delete(id: String)
}