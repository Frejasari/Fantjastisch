package de.fantjastisch.cards_frontend.learning_box.card_to_learning_box

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import java.util.*

@Entity(
    tableName = "card_to_learning_box",
    primaryKeys = ["learning_box_id", "card_id"],
    foreignKeys = [ForeignKey(
        entity = LearningBox::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("learning_box_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class CardToLearningBox(
    @ColumnInfo(name = "learning_box_id") val learningBoxId: UUID,
    @ColumnInfo(name = "card_id") val cardId: UUID,
)