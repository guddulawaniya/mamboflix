package com.mamboflix.model.recentView

import com.google.gson.annotations.SerializedName

data class RecentViewModel(@SerializedName("id") val id : Int,
                           @SerializedName("title") val title : String,
                           @SerializedName("description") val description : String,
                           @SerializedName("image") val image : String,
                           @SerializedName("path") val path : String,
                           @SerializedName("trailer_path") val trailer_path : String,
                           @SerializedName("cat_id") val cat_id : String,
                           @SerializedName("content_type") val content_type : Int,
                           @SerializedName("status") val status : Int,
                           @SerializedName("type") val type : Int,
                           @SerializedName("release_date") val release_date : String,
                           @SerializedName("release_year") val release_year : Int,
                           @SerializedName("banner_status") val banner_status : Int,
                           @SerializedName("content_duration") val content_duration : String,
                           @SerializedName("trailer_duration") val trailer_duration : String,
                           @SerializedName("content_mode") val content_mode : Int,
                           @SerializedName("related_content_id") val related_content_id : Int,
                           @SerializedName("created_at") val created_at : String,
                           @SerializedName("updated_at") val updated_at : String,
                           @SerializedName("content_created_date") val content_created_date : String,
                           @SerializedName("content_watch_id") val content_watch_id : Int)
