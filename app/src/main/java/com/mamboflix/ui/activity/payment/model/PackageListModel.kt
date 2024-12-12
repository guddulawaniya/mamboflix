package com.mamboflix.ui.activity.payment.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PackageListModel(
    var id: String?=null,
    var description: String,
    var share_limit: String,
    var packages: String,
    var package_type: String,
    var amount: String,
    var status: String,
    var details: String,
    var created_at: String,
    var image: String,
    var expiry_date: String,
    var discount: String,
    var is_purchase: String?=null,
    var updated_at: String
) : Parcelable