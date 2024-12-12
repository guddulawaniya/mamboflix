package com.mamboflix.model.contentdetails

import com.mamboflix.model.SubtitleModel

class ContentModel(
    var banner_status: String,
    var cat_id: String,
    var common_id: String,
    var id: String,
    var content_id: String,
    var episode_id: String,
    var content_duration: String,
    var content_mode: String,
    var content_type: String,
    var created_at: String,
    var category_name: String,
    var related_content_id: String,
    var description: String,
    var updated_at: String,
    val subscribed_uses: Int,
    var image: String,
    var path: String,
    var release_date: String,
    var session_id: String,
    var release_year: String,
    var status: String,
    var title: String,
    var trailer_duration: String,
    var trailer_path: String,
    var actor_name: String,
    var director_name: String,
    var writer_name: String,
    var type: String,
    var subtitle: ArrayList<SubtitleModel>?=null
    /*var subtitle: ArrayList<SubtitleModel>*/
)