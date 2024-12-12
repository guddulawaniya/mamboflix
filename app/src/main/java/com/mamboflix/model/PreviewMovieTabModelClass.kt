package com.mamboflix.model

import com.mamboflix.model.hometab.preview.PreviewContentModel

data class PreviewMovieTabModelClass(val preview_content : PreviewContentModel?=null,
                                     val cat_id : String?=null,
                                     val content_id : String?=null,
                                     val created_at : String,
                                     val id : String,
                                     val status : String,
                                     val updated_at : String)
