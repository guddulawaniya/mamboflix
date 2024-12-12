package com.mamboflix.model.alldata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AllVideoModel(
    var content_id: String,
    var created_date: String,
    var document_name: String,
    var doc_type: String,
    var id: String,
    var subject_name: String,
    var chapter_name: String,
    var class_name: String,
    var content_title: String,
    var updated_date: String
): Parcelable