package com.bikes.greyp.udacitycapstoneproject.data.network

import android.content.Context
import android.widget.ImageView
import com.bikes.greyp.udacitycapstoneproject.R
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

private const val PLACEHOLDER = "https://www.google.com"

private val retrofitWithScalars: Retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(PLACEHOLDER)
    .build()

interface RssApiService {
    @GET
    //no need for get parameters, since we enter rss feed url directly. Get parameters would be appended
    fun getRssFeed(@Url feedUrl: String): Call<String>
}

object RssNetworkApi {
    val retrofitApiService: RssApiService by lazy {
        retrofitWithScalars.create(RssApiService::class.java)
    }
}

object RssImageLoader{
    fun loadVtWIthGlide(context: Context, imageUrl: String, imageView: ImageView){
        Glide.with(context)
            .asBitmap()
            .placeholder(R.drawable.vital_icon)
            .load(imageUrl)
            .into(imageView)
    }

    fun loadPbWIthGlide(context: Context, imageUrl: String, imageView: ImageView){
        Glide.with(context)
            .asBitmap()
            .placeholder(R.drawable.pinkbike_icon)
            .load(imageUrl)
            .into(imageView)
    }
}