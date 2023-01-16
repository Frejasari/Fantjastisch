package de.fantjastisch.cards_frontend.learning_box

import androidx.room.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import java.util.*

class DataConverter {

    val moshi = Moshi
        .Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    val type =
        Types.newParameterizedType(
            List::class.java
        )
    val adapter: JsonAdapter<List<UUID>> = moshi.adapter(type)

    @TypeConverter
    fun fromUUIDList(uuidList: List<UUID>): String? {
        return adapter.toJson(uuidList)
    }

    @TypeConverter
    fun toUUIDList(uuidListString: String): List<UUID>? {
        val list: List<UUID> = adapter.fromJson(uuidListString)!!
        return list
    }
}
@TypeConverters(DataConverter::class)
@Entity(tableName = "learning_box",
    primaryKeys = ["learning_object_id","box_number"],
    foreignKeys = [ForeignKey(
        entity = LearningObject::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("learning_object_id"),
        onDelete = ForeignKey.CASCADE
    )])
data class LearningBox(
    @ColumnInfo(name = "learning_object_id") val learningObjectId: UUID,
    @ColumnInfo(name = "box_number") val boxNumber: Int,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "card_ids") val cardIds: List<UUID>
)
