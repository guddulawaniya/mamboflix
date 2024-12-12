package com.mamboflix.model.hometab.trending

class TrendingModel(
    val only_content : TrendingContentModel?=null,
    val cat_id : String,
    val content_id : String,
    val counter : String,
    val created_at : String,
    val id : String,
    val episode_id : String?=null,
    val updated_at : String,
    val sub_user_id : String,
    val user_id : String)