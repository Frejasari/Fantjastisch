package de.fantjastisch.cards_frontend.config

import androidx.room.Database
import androidx.room.RoomDatabase
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import de.fantjastisch.cards_frontend.learning_box.LearningBoxDao
import de.fantjastisch.cards_frontend.learning_object.LearningObjectDao
import de.fantjastisch.cards_frontend.learning_object.LearningObject


object AppDatabase {
    lateinit var database: Repository
        internal set
}

@Database(entities = [LearningObject::class, LearningBox::class], version = 1)
abstract class Repository : RoomDatabase() {
    abstract fun learningObjectDao(): LearningObjectDao
    abstract fun learningBoxDao(): LearningBoxDao
}

