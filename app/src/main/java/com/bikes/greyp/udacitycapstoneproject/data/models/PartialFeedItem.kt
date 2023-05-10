package com.bikes.greyp.udacitycapstoneproject.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

const val TITLE = "title"
const val LINK = "link"
const val DESCRIPTION = "description"
const val PUB_DATE = "pubDate"

@Entity(tableName = "local_rss_storage")
data class PartialFeedItem(

    @PrimaryKey(autoGenerate = false)
    val feedItemID: String,

    @ColumnInfo(name = "rss_source")
    val source: String,

    @ColumnInfo(name = "rss_title")
    val title: String,

    @ColumnInfo(name = "rss_link")
    val link: String,

    @ColumnInfo(name = "rss_description")
    val description: String,

    @ColumnInfo(name = "rss_image_url")
    val imageUrl: String,

    @ColumnInfo(name = "rss_publish_date")
    val pubDate: String
)