package com.mamboflix.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RegistrationRequestModel(
    var email: String,
    var password: String,
    var full_name: String,
    var telephone: String,
    var qualification: String,
    var study_status: String,
    var device_id: String,
    var device_type: String,
    var ref_id: String
): Parcelable