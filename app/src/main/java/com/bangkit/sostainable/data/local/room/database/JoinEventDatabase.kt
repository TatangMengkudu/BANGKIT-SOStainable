package com.bangkit.sostainable.data.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkit.sostainable.data.local.room.dao.EventDao
import com.bangkit.sostainable.data.local.room.entities.JoinEvent

@Database(entities = [JoinEvent::class], version = 1)
abstract class JoinEventDatabase: RoomDatabase() {
    abstract fun joinEventDao(): EventDao
    companion object {
        @Volatile
        private var INSTANCE: JoinEventDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): JoinEventDatabase {
            if (INSTANCE == null) {
                synchronized(JoinEventDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        JoinEventDatabase::class.java, "join_database")
                        .build()
                }
            }
            return INSTANCE as JoinEventDatabase
        }
    }
}