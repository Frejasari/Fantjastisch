package de.fantjastisch.cards_frontend.learning_object

import androidx.room.*
import java.util.*

/**
 * Internal Repository für Lernboxen. Enthält die Datenbankanfragen.
 *
 * @author Semjon Nirmann, Jessica Repty
 */
class InternalLearningObjectRepository(private val dao: LearningObjectDao) :
    LearningObjectDao by dao

@Dao
interface LearningObjectDao {
    @Query("SELECT * FROM learning_object")
    suspend fun getAll(): List<LearningObject>

    @Query("SELECT * FROM learning_object WHERE id = :id")
    suspend fun findById(id: UUID): LearningObject

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(learningObject: LearningObject)

    @Query("delete from learning_object where id=:id")
    suspend fun delete(id: UUID)
}