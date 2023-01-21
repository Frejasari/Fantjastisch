package de.fantjastisch.cards_frontend.learning_box.card_to_learning_box

import androidx.room.*
import java.util.*

class InternalCardToLearningBoxRepository(private val dao: CardToLearningBoxDao) :
    CardToLearningBoxDao by dao

@Dao
interface CardToLearningBoxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCardIntoBox(cardToLearningBox: CardToLearningBox)

    @Query("DELETE FROM card_to_learning_box WHERE card_id = :cardId and learning_box_id = :learningBoxId")
    fun deleteCardFromBox(cardId: UUID, learningBoxId: UUID)

    @Query("SELECT COUNT(*) from learning_box " +
            "join card_to_learning_box " +
            "on learning_box_id = id " +
            "group by learning_box_id, learning_object_id " +
            "having learning_box_id = :learningBoxId")
    fun getNumOfCardsFromLearningBoxId(learningBoxId: UUID) :Int

    @Query("SELECT card_id FROM card_to_learning_box WHERE learning_box_id = :learningBoxId")
    fun getCardIdsForBox(learningBoxId: UUID): List<UUID>

}