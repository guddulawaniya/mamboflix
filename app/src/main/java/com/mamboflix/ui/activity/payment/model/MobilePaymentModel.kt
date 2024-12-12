package com.mamboflix.ui.activity.payment.model

class MobilePaymentModel(
    var final: Final,
    var order_id: String
)
class Final(
        var reference: String,
        var transid: String,
        var resultcode: String,
        var result: String,
        var message: String
)