package com.bikes.greyp.udacitycapstoneproject.data.models

enum class RssSource(val url: String) {
    Vital("http://feeds.vitalmtb.com/VitalMtbSpotlights"),
    Pinkbike("https://www.pinkbike.com/pinkbike_xml_feed.php")
}