package de.fantjastisch.cards_frontend.learning_object

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import org.openapitools.client.models.LearningSystemEntity
import java.util.UUID

@Entity(tableName = "learning_object")
data class LearningObject(
    @PrimaryKey val id: UUID = generateId(),
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "learning_system_id") val learningSystemId: UUID
)

fun generateId() = UUID.randomUUID()