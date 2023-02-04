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

    /**
     * Holt alle Lernobjekte aus der Datenbank.
     *
     * @return List<LearningObject> Liste aller [LearningObject].
     */
    @Query("SELECT * FROM learning_object")
    suspend fun getAll(): List<LearningObject>

    /**
     * Holt ein Lernobjekt aus der Datenbank.
     *
     * @param id Die UUID des Lernobjekts, das geholt werden muss.
     * @return wenn es ein Lernobjekt mit der UUID gibt dann ein Lernobjekt als [LearningObject]
     *  sonst null.
     */
    @Query("SELECT * FROM learning_object WHERE id = :id")
    suspend fun findById(id: UUID): LearningObject

    /**
     * Fügt ein Lernobjekt in die Datenbank ein.
     *
     * @param learningObject Das Lernobjekt, das eingefügt wird.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(learningObject: LearningObject)

    /**
     * Löscht ein Lernobjekt aus der Datenbank.
     *
     * @param id Die UUID des Lernobjekts, das gelöscht werden soll.
     */
    @Query("delete from learning_object where id=:id")
    suspend fun delete(id: UUID)
}