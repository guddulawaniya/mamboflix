package com.mamboflix.model.hometab.mostwatched

class MostWatchedModel(
    val content : MostWatchContentModel,
    val cat_id : String,
    val content_id : String,
    val counter : String,
    val created_at : String,
    val episode_id : String? = null,
    val id : String,
    val updated_at : String,
    val user_id : String,
    val watch_duration : String,
    val who_watch_id : String)