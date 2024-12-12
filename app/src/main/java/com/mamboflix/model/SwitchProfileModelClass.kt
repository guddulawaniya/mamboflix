package com.mamboflix.model

data class SwitchProfileModelClass(
    val `data`: List<SwitchProfileData>,
    val message: String,
    val status: Int
)

data class SwitchProfileData(
    val image_s: String,
    val name_s: String,
    val type: Int
)