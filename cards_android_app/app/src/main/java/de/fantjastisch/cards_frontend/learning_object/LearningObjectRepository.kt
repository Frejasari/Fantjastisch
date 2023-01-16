package de.fantjastisch.cards_frontend.learning_object

import androidx.room.*

class LearningObjectRepository(private val dao: LearningObjectDao): LearningObjectDao by dao

@Dao
interface LearningObjectDao {
    @Query("SELECT id, answer, question FROM learning_object")
    fun getAll(): List<LearningObject>

    @Query("SELECT c.id, c.answer, c.question FROM learning_object c WHERE id=:id")
    fun findById(id: String): LearningObject

    @Insert
    fun insert(card: LearningObject)

    @Query("delete from learning_object where id=:id")
    fun delete(id: String)
}