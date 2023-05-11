package com.bikes.greyp.udacitycapstoneproject.utils

import com.bikes.greyp.udacitycapstoneproject.data.models.DESCRIPTION
import com.bikes.greyp.udacitycapstoneproject.data.models.LINK
import com.bikes.greyp.udacitycapstoneproject.data.models.PUB_DATE
import com.bikes.greyp.udacitycapstoneproject.data.models.PartialFeedItem
import com.bikes.greyp.udacitycapstoneproject.data.models.TITLE

private const val ITEM_OPEN = "<item>"
private const val ITEM_CLOSE = "</item>"
private const val SLASH = "/"
private const val DOUBLE_SLASH = "//"
private const val IMAGE_SOURCE_START = "src="

class Parser {

    fun extractSource(feedUrl: String): String {
        val clearFirstPart = feedUrl.substring(feedUrl.indexOf(DOUBLE_SLASH) + 2)
        return clearFirstPart.substring(0, clearFirstPart.indexOf(SLASH))
    }

    fun extractItems(body: String): List<String> {
        var tmpBody = body

        val listOfItems = ArrayList<String>()
        var itemStart = tmpBody.indexOf(ITEM_OPEN)
        var itemEnd = tmpBody.indexOf(ITEM_CLOSE)

        if(itemStart == -1 || itemEnd == -1){
            throw IllegalArgumentException("Malformed RSS feed")
        }

        do {
            listOfItems.add(tmpBody.substring(itemStart, itemEnd + 7))
            tmpBody = tmpBody.substring(itemEnd + 7)

            itemStart = tmpBody.indexOf(ITEM_OPEN)
            itemEnd = tmpBody.indexOf(ITEM_CLOSE)
        } while (itemEnd != -1)

        return listOfItems
    }

    fun createPartialFeedItems(source: String, itemList: List<String>): List<PartialFeedItem> {
        val listOfPartialFeedItems = ArrayList<PartialFeedItem>()

        for (item in itemList) {
            val title = extractTitle(item)
            val hashedKey = Crypto.getSHA256hash(source + title)
            val description = extractDescription(item)
            val imageUrl = extractImage(description)
            listOfPartialFeedItems.add(
                PartialFeedItem(
                    hashedKey,
                    source,
                    title,
                    extractLink(item),
                    description,
                    imageUrl,
                    extractPubDate(item)
                )
            )
        }
        return listOfPartialFeedItems
    }

    private fun extractTitle(item: String): String {
        return extractElement(TITLE, item)
    }

    private fun extractLink(item: String): String {
        return extractElement(LINK, item)
    }

    private fun extractDescription(item: String): String {
        return extractElement(DESCRIPTION, item)
    }

    private fun extractPubDate(item: String): String {
        return extractElement(PUB_DATE, item)
    }

    private fun extractElement(element: String, item: String): String {
        val elementStart = item.indexOf("<$element>")
        val elementEnd = item.indexOf("</$element>")
        return item.substring(elementStart + element.length + 2, elementEnd)
    }

    private fun extractImage(description: String): String{
        val imageStart = description.indexOf(IMAGE_SOURCE_START) +5
        val imageEnd = description.indexOf("\"", imageStart)
        return description.substring(imageStart, imageEnd)
    }
}