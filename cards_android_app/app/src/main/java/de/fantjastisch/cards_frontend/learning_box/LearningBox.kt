package de.fantjastisch.cards_frontend.learning_box

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.generateId
import java.util.*

/**
 * Tabellen-Beschreibung von LearningBox
 *
 * @property id die Id der Lernbox
 * @property learningObjectId die Id des Lernobjektes
 * @property boxNumber die Nr der Lernbox
 * @property label das Label der Lernbox
 *
 * @author Semjon Nirmann, Jessica Repty
 */
@Entity(
    tableName = "learning_box",
    foreignKeys = [ForeignKey(
        entity = LearningObject::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("learning_object_id"),
        onDelete = ForeignKey.CASCADE
    )]
)

data class LearningBox(
    @PrimaryKey
    val id: UUID = generateId(),
    @ColumnInfo(name = "learning_object_id") val learningObjectId: UUID,
    @ColumnInfo(name = "box_number") val boxNumber: Int,
    @ColumnInfo(name = "label") val label: String
)

/**
 * Data Transfer Object, um Learning Boxes mit der Anzahl ihrer Karten zu laden
 *
 * @property id die Id der Lernbox
 * @property learningObjectId die Id des Lernobjektes
 * @property boxNumber die Nr der Lernbox
 * @property label das Label der Lernbox
 * @property nrOfCards Anzahl der Karten in der Box
 *
 * * @author Freja Sender
 */
data class LearningBoxWitNrOfCards(
    @ColumnInfo val id: UUID = generateId(),
    @ColumnInfo(name = "learning_object_id") val learningObjectId: UUID,
    @ColumnInfo(name = "box_number") val boxNumber: Int,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "nr_of_cards") val nrOfCards: Int
)