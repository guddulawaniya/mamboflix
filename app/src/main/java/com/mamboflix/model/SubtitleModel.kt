package com.mamboflix.model

import android.os.Parcel
import android.os.Parcelable

data class SubtitleModel(val language:String?=null, val subtitle:String?=null):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(language)
        parcel.writeString(subtitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubtitleModel> {
        override fun createFromParcel(parcel: Parcel): SubtitleModel {
            return SubtitleModel(parcel)
        }

        override fun newArray(size: Int): Array<SubtitleModel?> {
            return arrayOfNulls(size)
        }
    }
}
