package de.fantjastisch.cards_frontend.config

import androidx.room.Database
import androidx.room.RoomDatabase
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import de.fantjastisch.cards_frontend.learning_box.LearningBoxDao
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBox
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxDao
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.LearningObjectDao


object AppDatabase {
    lateinit var database: Repository
        internal set
}

@Database(
    entities = [LearningObject::class, LearningBox::class, CardToLearningBox::class],
    version = 1
)
abstract class Repository : RoomDatabase() {
    abstract fun learningObjectDao(): LearningObjectDao
    abstract fun learningBoxDao(): LearningBoxDao
    abstract fun cardToLearningBoxDao(): CardToLearningBoxDao
}

