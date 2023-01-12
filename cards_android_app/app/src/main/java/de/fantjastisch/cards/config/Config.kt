package de.fantjastisch.cards.config

import androidx.room.Database
import androidx.room.RoomDatabase
import de.fantjastisch.cards.card.LearningObject
import de.fantjastisch.cards.card.LearningObjectDao


object AppDatabase {
    lateinit var database: Repository
        internal set
}

@Database(entities = [LearningObject::class], version = 1)
abstract class Repository : RoomDatabase() {
    abstract fun cardDao(): LearningObjectDao
}