package de.fantjastisch.cards

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.fantjastisch.cards.card.LearningObject
import de.fantjastisch.cards.card.LearningObjectDao

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.database = Room.databaseBuilder(
            this,
            Repository::class.java, "database-name"
        ).allowMainThreadQueries().build()
    }
}

object AppDatabase {
    lateinit var database: Repository
    internal set
}

@Database(entities = [LearningObject::class], version = 1)
abstract class Repository : RoomDatabase() {
    abstract fun cardDao(): LearningObjectDao
}
