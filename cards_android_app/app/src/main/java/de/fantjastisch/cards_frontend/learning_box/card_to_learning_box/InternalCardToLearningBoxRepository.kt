package de.fantjastisch.cards_frontend.learning_box.card_to_learning_box

import androidx.room.*
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import org.openapitools.client.models.CardEntity
import java.util.*

class InternalCardToLearningBoxRepository(private val dao: CardToLearningBoxDao) :
    CardToLearningBoxDao by dao

@Dao
interface CardToLearningBoxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCardIntoBox(cardToLearningBox: CardToLearningBox)

    @Query("DELETE FROM card_to_learning_box WHERE card_id = :cardId and learning_box_id = :learningBoxId")
    fun deleteCardFromBox(cardId: UUID, learningBoxId: UUID)
}