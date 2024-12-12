package com.mamboflix.model.contentdetails

class MovieContentDetailsModel(
    val content : ContentModel?=null,
    val related_content : ArrayList<RelatedContentModel>,
    val subtitle_e : SubTitleContent?=null,
    val episode : ArrayList<SessionList>,
    val content_mode : String,
    val subscribed_uses : String,
    val like_status : String,
    val my_list_status : Int,
    val download_status : String
    /*val episode : String*/
)

class SessionList(
    var seasion_id: String,
    var seasion: String,
    var name: String
)