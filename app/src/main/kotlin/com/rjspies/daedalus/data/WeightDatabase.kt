package com.rjspies.daedalus.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WeightImpl::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
)
abstract class WeightDatabase : RoomDatabase() {
    abstract fun weightDao(): WeightDao

    companion object {
        @Volatile
        private var INSTANCE: WeightDatabase? = null

        fun getDatabase(context: Context): WeightDatabase = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context = context,
                klass = WeightDatabase::class.java,
                name = "weight_database",
            ).apply {
                fallbackToDestructiveMigration(false)
            }.build()
            INSTANCE = instance
            instance
        }
    }
}
