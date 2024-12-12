package com.mamboflix.model.alldata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AllQuizModel(
    var name: String,
    var test_id: String,
    var isCompleted: Int,
    var content_title: String,
    var time: String
): Parcelable