package de.fantjastisch.cards_frontend.learning_object

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "learning_object")
data class LearningObject(
    @PrimaryKey val id: UUID = generateId(),
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "learning_system_id") val learningSystemId: UUID
)

fun generateId() = UUID.randomUUID()