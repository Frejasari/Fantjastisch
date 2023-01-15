package de.fantjastisch.cards_frontend.config

import androidx.room.Database
import androidx.room.RoomDatabase
import de.fantjastisch.cards_frontend.card.LearningObjectDao
import de.fantjastisch.cards_frontend.learning_object.LearningObject


object AppDatabase {
    lateinit var database: Repository
        internal set
}

@Database(entities = [LearningObject::class], version = 1)
abstract class Repository : RoomDatabase() {
    abstract fun cardDao(): LearningObjectDao
}