package com.bikes.greyp.udacitycapstoneproject.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bikes.greyp.udacitycapstoneproject.data.models.PartialFeedItem

@Database(entities = [PartialFeedItem::class], version = 1, exportSchema = false)
abstract class RssDatabase : RoomDatabase() {

    abstract val rssDao: RssDao

    companion object {

        @Volatile
        private var INSTANCE: RssDatabase? = null

        fun getInstance(context: Application): RssDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        RssDatabase::class.java,
                        "local_rss_feed_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}