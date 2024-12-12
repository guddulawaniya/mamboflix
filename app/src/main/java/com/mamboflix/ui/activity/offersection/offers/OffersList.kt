package com.mamboflix.ui.activity.offersection.offers

class OffersList(
    val id : String,
    val coupon_code : String,
    val description : String,
    val off_percentage : String,
    val status : String,
    val created_at : String,
    val updated_at : String)

class OffersApplied(
    val offer_percentage : String,
    val price : String,
    val discount_amount : String,
    val duration : String,
    val after_offer_amount : String
)
