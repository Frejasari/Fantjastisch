package de.fantjastisch.cards_frontend.learning_box.card_to_learning_box

import androidx.room.*
import java.util.*

class InternalCardToLearningBoxRepository(private val dao: CardToLearningBoxDao) :
    CardToLearningBoxDao by dao

@Dao
interface CardToLearningBoxDao {
    @Insert
    suspend fun insertCards(cardToLearningBoxes: List<CardToLearningBox>)

    @Query("DELETE FROM card_to_learning_box WHERE learning_box_id = :learningBoxId;")
    suspend fun deleteAllCardsFromLearningBox(learningBoxId: UUID)

    @Query("DELETE FROM card_to_learning_box WHERE learning_box_id = :learningBoxId and card_id in (:cardIds);")
    suspend fun deleteCardsFromBox(cardIds: List<UUID>, learningBoxId: UUID)

    @Query("DELETE FROM card_to_learning_box WHERE card_id = :cardId;")
    suspend fun deleteCardFromAllBoxes(cardId: UUID)

    @Query(
        "SELECT COUNT(*) from learning_box " +
                "join card_to_learning_box " +
                "on learning_box_id = id " +
                "group by learning_box_id, learning_object_id " +
                "having learning_box_id = :learningBoxId;"
    )
    suspend fun getNumOfCardsFromLearningBoxId(learningBoxId: UUID): Int

    @Query("SELECT card_id FROM card_to_learning_box WHERE learning_box_id = :learningBoxId;")
    suspend fun getCardIdsForBox(learningBoxId: UUID): List<UUID>

    @Query("SELECT card_id FROM learning_box JOIN card_to_learning_box on id = learning_box_id WHERE learning_object_id = :learningObjectId;")
    suspend fun getAllCardsForLearningObject(learningObjectId: UUID): List<UUID>
}