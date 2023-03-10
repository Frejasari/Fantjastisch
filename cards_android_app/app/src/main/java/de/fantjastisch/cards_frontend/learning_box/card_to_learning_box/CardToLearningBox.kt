package de.fantjastisch.cards_frontend.learning_box.card_to_learning_box

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import java.util.*

/**
 * Tabellen-Beschreibung von CardToLEarningBox
 *
 * @property learningBoxId Id der Lernbox
 * @property cardId Id der Karteikarte
 *
 * @author Semjon Nirmann, Jessica Repty
 */
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
/**
 * Hält Daten über die Zuordnungen von Karten zu Lernboxen
 *
 * @param learningBoxId Die UUID der Lernbox, zu der eine Karte gehört.
 * @param cardId Die UUID einer Karte, die zu einer Lernbox gehört.
 *
 * @author Semjon Nirmann,Jessica Repty
 */
data class CardToLearningBox(
    @ColumnInfo(name = "learning_box_id") val learningBoxId: UUID,
    @ColumnInfo(name = "card_id") val cardId: UUID,
)