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

}

object GetDatabase {

    private const val databaseName = "in_seguros.db"

    fun getDatabaseBuilder(context: Context): DatabaseHandler {
        return Room.databaseBuilder(context, DatabaseHandler::class.java, databaseName).build()
    }
}