package com.comphy.photo.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.local.room.location.LocationDao

@Database(
    entities = [CityEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ComphyDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {
        private var database: ComphyDatabase? = null

        fun instance(context: Context): ComphyDatabase {
            if (database == null) {
                synchronized(ComphyDatabase::class) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        ComphyDatabase::class.java,
                        "comphy_db.db"
                    ).build()
                }
            }
            return database!!
        }

        fun destroyInstance() {
            database = null
        }
    }

}