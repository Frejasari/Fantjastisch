package de.fantjastisch.cards_frontend.learning_object

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// TODO ADD CORRECT FIELDS
@Entity(tableName = "learning_object")
data class LearningObject(
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "answer") val answer: String,

    @PrimaryKey val id: UUID = generateId()
)

fun generateId() = UUID.randomUUID()