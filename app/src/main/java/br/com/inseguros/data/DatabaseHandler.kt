package br.com.inseguros.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.inseguros.data.dao.QuoteVehicleDAO
import br.com.inseguros.data.model.QuoteVehicle

@Database(
    entities = [QuoteVehicle::class],
    version = 1,
    exportSchema = false
)

abstract class DatabaseHandler: RoomDatabase() {

    abstract fun quoteVehicleDAO(): QuoteVehicleDAO

    companion object {

        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getDatabase(context: Context): DatabaseHandler {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseHandler::class.java,
                    "in_seguros.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
