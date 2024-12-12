package com.mamboflix.model.FilterMovies

import com.google.gson.annotations.SerializedName
import com.mamboflix.model.hometab.preview.PreviewModel
import com.mamboflix.model.hometab.topbanner.TopBannerModel
import com.mamboflix.model.searchContent.SearchContentModel

class MoviesByCategoryModel(
    val content : Content?=null,
    val preview : ArrayList<PreviewModel>,
    val top_banner : TopBannerModel
)



data class Content (
        @SerializedName("id") val id : Int,
        @SerializedName("title") val title : String,
        @SerializedName("description") val description : String,
        @SerializedName("status") val status : Int,
        @SerializedName("created_at") val created_at : String,
        @SerializedName("updated_at") val updated_at : String,
        @SerializedName("all_content") val all_content : ArrayList<SearchContentModel>
)