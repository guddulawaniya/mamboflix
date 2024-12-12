package com.mamboflix.ui.activity.offersection

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mamboflix.R
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {
    private var cvvToknizationButton: Button? = null
    private var cardTokenizationButton: Button? = null

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        cvvToknizationButton = findViewById(R.id.button_cvv_tokenization)
        cardTokenizationButton = findViewById(R.id.button_buy)


//        initItemUI(ConstantsCheckout.PAYMENT_AMOUNT,cardTokenizationButton!!)


    /*    findViewById<View>(R.id.button_buy).setOnClickListener { view: View ->
            onBuyButtonClicked(
                view
            )
        }
        cvvToknizationButton!!.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    CVVTokenizationActivity::class.java
                )
            )
        })*/
    }

    private fun initItemUI(
        price: Long,
        cvvToknizationButton: Button
    ) {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        cvvToknizationButton!!.text =
            getText(R.string.button_buy).toString() + numberFormat.format(price.toDouble() / 100)
    }

  /*  private fun onBuyButtonClicked(view: View) {
        startActivity(Intent(this, DemoActivity::class.java))
    }*/
}