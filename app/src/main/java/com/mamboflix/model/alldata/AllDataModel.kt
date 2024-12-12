package com.mamboflix.model.alldata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AllDataModel(
    var video: ArrayList<AllVideoModel>,
    var document: ArrayList<AllPdfModel>,
    var content_id: String,
    var quiz: String,
    var quiz_name: ArrayList<AllQuizModel>
): Parcelable