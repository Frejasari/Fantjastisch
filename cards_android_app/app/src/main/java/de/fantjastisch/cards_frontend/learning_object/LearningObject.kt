package de.fantjastisch.cards_frontend.learning_object

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import java.util.UUID

@Entity(tableName = "learning_object")
data class LearningObject(
    @ColumnInfo(name = "label") val label: String,
    @PrimaryKey val id: UUID = generateId()
)

fun generateId() = UUID.randomUUID()