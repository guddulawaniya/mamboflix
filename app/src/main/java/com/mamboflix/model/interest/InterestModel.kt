package com.mamboflix.model.interest

data class InterestModel(
    var description: String,
    var created_at: String,
    var id: String,
    var title: String,
    var status: String,
    var content_count: Int,
    var isSelected: Boolean = false
)