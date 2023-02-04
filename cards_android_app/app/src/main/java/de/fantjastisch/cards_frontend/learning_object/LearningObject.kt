package de.fantjastisch.cards_frontend.learning_object

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Erstellt ein [LearningObject]-Entität
 *
 * @property id Die UUID eines Lernobjekts.
 * @property label Die Bezeichnung eines Lernobjekts.
 * @property learningSystemId Die UUID des dazugehörigen Lernsystems.
 */
@Entity(tableName = "learning_object")
data class LearningObject(
    @PrimaryKey val id: UUID = generateId(),
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "learning_system_id") val learningSystemId: UUID
)

/**
 * Generiert zufällige UUIDs für Lernobjekte.
 *
 * @return Die generierte UUID.
 */
fun generateId() = UUID.randomUUID()