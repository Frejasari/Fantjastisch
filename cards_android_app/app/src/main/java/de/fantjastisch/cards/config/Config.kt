package de.fantjastisch.cards.config

import androidx.room.Database
import androidx.room.RoomDatabase
import de.fantjastisch.cards.card.Card
import de.fantjastisch.cards.card.CardDao


object AppDatabase {
    lateinit var database: Repository
        internal set
}

@Database(entities = [Card::class], version = 1)
abstract class Repository : RoomDatabase() {
    abstract fun cardDao(): CardDao
}