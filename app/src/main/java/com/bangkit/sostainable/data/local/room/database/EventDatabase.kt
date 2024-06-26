package com.bangkit.sostainable.data.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkit.sostainable.data.local.room.dao.BookmarkDao
import com.bangkit.sostainable.data.local.room.dao.EventDao
import com.bangkit.sostainable.data.local.room.dao.HistoryDonateDao
import com.bangkit.sostainable.data.local.room.dao.JoinEventDao
import com.bangkit.sostainable.data.local.room.dao.RemoteKeysDao
import com.bangkit.sostainable.data.local.room.entities.Bookmark
import com.bangkit.sostainable.data.local.room.entities.HistoryDonateEvent
import com.bangkit.sostainable.data.local.room.entities.JoinEvent
import com.bangkit.sostainable.data.local.room.entities.RemoteKeys
import com.bangkit.sostainable.data.remote.response.event.DataItem

@Database(
    entities = [DataItem::class, RemoteKeys::class, Bookmark::class, JoinEvent::class, HistoryDonateEvent::class],
    version = 4,
    exportSchema = false
)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun joinEventDao(): JoinEventDao
    abstract fun historyDonateDao(): HistoryDonateDao
    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): EventDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java, "db_events"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}