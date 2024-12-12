package com.mamboflix.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TermsModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("titile")
    val titile: String,
    @SerializedName("description")
    val description: String
) : Parcelable


