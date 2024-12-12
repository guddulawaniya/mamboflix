package com.mamboflix.ui.activity.reviews.reviewsmodel

class Reviews(

    val total_review : Int,
    val average_rating : String,
    val rating_list : ArrayList<rating_list>
   )

class rating_list(
    val id : Int,
    val content_id : Int,
    val content_type : Int,
    val from_id : Int,
    val rating : String,
    val description : String,
    val username : String, val title : String,
    val created_at : String,
    val updated_at : String

)
