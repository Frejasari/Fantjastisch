package de.fantjastisch.cards.card

import androidx.room.*

class LearningObjectRepository(private val dao: LearningObjectDao): LearningObjectDao by dao

@Dao
interface LearningObjectDao {
    @Query("SELECT id, answer, question FROM cards")
    fun getAll(): List<LearningObject>

    @Query("SELECT c.id, c.answer, c.question FROM cards c WHERE id=:id")
    fun findById(id: String): LearningObject

    @Insert
    fun insert(card: LearningObject)

    @Query("delete from learning_objects where id=:id")
    fun delete(id: String)
}