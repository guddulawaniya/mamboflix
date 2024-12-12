package com.mamboflix.model.hometab.preview

class PreviewModel(
    val preview_content : PreviewContentModel?=null,
    val cat_id : String?=null,
    val episode_id : String?=null,
    val content_id : String?=null,
    val created_at : String,
    val id : String,
    val status : String,
    val updated_at : String
    )