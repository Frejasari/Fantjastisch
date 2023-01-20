package de.fantjastisch.cards_frontend.learning_box

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.generateId
import java.util.*

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