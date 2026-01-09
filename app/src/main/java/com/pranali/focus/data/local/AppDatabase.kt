package com.pranali.focus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pranali.focus.data.local.dao.SessionDao
import com.pranali.focus.data.local.entity.SessionEntity

@Database(entities = [SessionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Allow access to the Dao
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Singleton pattern to prevent multiple database instances opening at the same time
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "focus_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}