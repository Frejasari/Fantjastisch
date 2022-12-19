package de.fantjastisch.cards.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "cards")
data class Card(
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "answer") val answer: String,

    @PrimaryKey val id: UUID = generateCardId()
)

fun generateCardId() = UUID.randomUUID()