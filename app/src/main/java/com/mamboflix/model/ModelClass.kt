package com.mamboflix.model

data class ModelClass(
    val `data`: List<ModelData>,
    val message: String,
    val status: Int
)

data class ModelData(
    val create_at: String,
    val id: Int,
    val location_name: String,
    val location_name_b: String,
    val status: Int,
    val update_at: Any
)