package com.mamboflix.model

import com.google.gson.annotations.SerializedName

data class MoviesAndShowsCategory (
        @SerializedName("id") val id : Int,
        @SerializedName("title") val title : String,
        @SerializedName("description") val description : String,
        @SerializedName("status") val status : Int,
        @SerializedName("created_at") val created_at : String,
        @SerializedName("updated_at") val updated_at : String
)
