package com.bikes.greyp.udacitycapstoneproject.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bikes.greyp.udacitycapstoneproject.data.models.PartialFeedItem

@Dao
interface RssDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(partialFeedItem: PartialFeedItem)

    @Query("SELECT * FROM local_rss_storage WHERE rss_source =:source")
    fun getAllFeedsForGivenSource(source: String): List<PartialFeedItem>
}