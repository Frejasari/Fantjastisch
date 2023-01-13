package de.fantjastisch.cards_frontend

import android.app.Application
import androidx.room.Room
import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.config.Repository
import java.util.*

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        System.setProperty("org.openapitools.client.baseUrl", "http://10.0.2.2:8080")
        AppDatabase.database = Room.databaseBuilder(
            this,
            Repository::class.java, "database-name"
        ).allowMainThreadQueries().build()
    }
}