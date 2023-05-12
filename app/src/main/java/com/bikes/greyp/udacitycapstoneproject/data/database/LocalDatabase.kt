package com.bikes.greyp.udacitycapstoneproject.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bikes.greyp.udacitycapstoneproject.data.models.PartialFeedItem
import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot

@Database(entities = [PartialFeedItem::class, RidingSpot::class], version = 2, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract val rssDao: RssDao
    abstract val ridingSpotDao: RidingSpotDao

    companion object {

        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Application): LocalDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        LocalDatabase::class.java,
                        "local_bikerapp_database"
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