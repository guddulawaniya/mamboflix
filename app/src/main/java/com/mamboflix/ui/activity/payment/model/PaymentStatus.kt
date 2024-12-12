package com.mamboflix.ui.activity.payment.model

class PaymentStatus(
    var data: List<Paymentdata>,
    var reference: String,
    var resultcode: String,
    var result: String,
    var message: String
)
class Paymentdata(
        var order_id: String,
        var creation_date: String,
        var amount: String,
        var payment_status: String,
        var transid: String
)
