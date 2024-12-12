package com.mamboflix.ui.activity.payment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.mamboflix.BaseActivity
import com.mamboflix.HomeActivity3.HomeActivity3
import com.mamboflix.R
import com.mamboflix.databinding.ActivityMakePaymentBinding
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class MakePaymentActivity : BaseActivity() {
    private lateinit var binding: ActivityMakePaymentBinding
    private var url:String=""
    private var id:String=""
    private var order_id:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_make_payment)
        if(intent.hasExtra("url")){
            url= intent.getStringExtra("url")!!
            id= intent.getStringExtra("id")!!
            order_id= intent.getStringExtra("order_id")!!
            init()
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init() {
        val webSettings = binding.wvPayment.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        binding.wvPayment.setPadding(0, 0, 0, 0)

        binding.wvPayment.webViewClient= object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()

                // Check if the URL matches the success URL
                if (url.contains("success")) {
                    handlePaymentSuccess()
                    return true
                } else if (url.contains("failure")) {
                    handlePaymentFailure()
                    return true
                } else if (url.contains("cancel")) {
                    handlePaymentCancel()
                    return true
                }

                return super.shouldOverrideUrlLoading(view, request)
            }
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                binding.pbPayment.visibility = View.GONE
                //binding.wvPayment.goBack()
                //finish()
            }
        }


//        binding.wvPayment.webViewClient = object: WebViewClient(){
//            override fun shouldOverrideUrlLoading(webView: WebView?, url: String): Boolean {
//                binding.pbPayment.visibility = View.VISIBLE
//                if (url.contains(Constants.BASE_IP) || url.contains("uatdemofpx.paynet.com.my")) {
//                    if(url.contains("${Constants.BASE_URL}unsuccessful")){
//                        binding.root.snackbar(getString(R.string.transaction_canceled))
//                        setResult(0)
//                        finish()
//                    }
//                    else if(url.contains("${Constants.BASE_URL}success")){
//                        binding.root.snackbar(getString(R.string.transaction_completed))
//
//                        setResult(Activity.RESULT_OK)
//                        finish()
//                    }
//                    return false
//                }
//
//                //startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
//                return super.shouldOverrideUrlLoading(webView, url)
//                //return true
//            }
//
//            override fun onPageCommitVisible(view: WebView?, url: String?) {
//                super.onPageCommitVisible(view, url)
//                binding.pbPayment.visibility = View.GONE
//                //binding.wvPayment.goBack()
//                //finish()
//            }
//        }
        binding.wvPayment.loadUrl(url)
        binding.wvPayment.addJavascriptInterface(object : Any() {
            @JavascriptInterface
            fun performClick(strl: String?) {

                Toast.makeText(this@MakePaymentActivity, strl, Toast.LENGTH_LONG).show()
            }
        }, "ok")
    }

    private fun handlePaymentSuccess() {
        // Handle payment success
        // For example, show a success message or navigate to another screen
        println("Payment Successful")
        fetchPaymentUrl()
    }

    private fun handlePaymentFailure() {
        // Handle payment failure
        // For example, show a failure message or navigate to another screen
        println("Payment Failed")
        utils.simpleAlert(
            this,
            "Payment",
            "Payment Failed"
        )
//        Toast.makeText(this, "Payment Successfully", Toast.LENGTH_SHORT).show()
    }

    private fun handlePaymentCancel() {
        // Handle payment cancellation
        // For example, show a cancellation message or navigate to another screen
        println("Payment Cancelled")

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
       /* if (keyCode != 4 || !binding.wvPayment.canGoBack()) {
            if(intent.hasExtra("plan")){
                return super.onKeyDown(keyCode, event)
            }
            return false
        }
        binding.wvPayment.goBack()*/

        if (event!!.action === KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (binding.wvPayment.canGoBack()) {
                        binding.wvPayment.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
        //return true
    }

    fun fetchPaymentUrl() {
        val token = userPref.getToken()?.toString() ?: ""

        Log.d("paymentDetails", "Payment Model ID: $id")
        Log.d("paymentDetails", "User Token: $token")

        // Check if token or id is empty before making the API call
        if (token.isEmpty() || id.isEmpty()) {
            Log.e("fetchPaymentUrl", "Token or ID is null or empty")
            return
        }
        apiService.paymentCallback("Bearer " + userPref.getToken(), id,order_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe(this::showProgressDialog)
//            .doOnCompleted(this::hideProgressDialog)
            .subscribe({newPayment_module ->
                if (newPayment_module.status == 1) {
//                    startActivity(
//                        Intent(this, MakePaymentActivity::class.java)
//                            .putExtra("url", newPayment_module.data.toString())
//                    )

                    utils.simpleAlert(
                        this,
                        "Payment",
                        "Payment Cancelled"
                    )
//                    Toast.makeText(this, "Payment Successfully", Toast.LENGTH_SHORT).show()

                    val intent= Intent(this, HomeActivity3::class.java)
                    startActivity(intent)

                } else {
                    utils.simpleAlert(
                        this,
                        "Error",
                        newPayment_module.message.toString()
                    )
                    hideProgressDialog()
                }
            }, { throwable ->
                hideProgressDialog()
                if (throwable is ConnectException) {
                    utils.simpleAlert(
                        this,
                        "Error",
                        getString(R.string.check_network_connection)
                    )
                } else {
                    utils.simpleAlert(
                        this,
                        "Error",
                        throwable.message.toString()
                    )
                }
            })

        // Make the API call
        /*  apiService.fetchPaymentUrl(token, id)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .doOnSubscribe { showProgressDialog() } // Using lambda for showProgressDialog
              .doOnCompleted(this::hideProgressDialog)// Using doFinally for hideProgressDialog
              .subscribe({ newPayment_module ->
                  // Handle the response
                  if (newPayment_module.status == 1) {
                      Log.d("paymentDetails", "Payment URL: ${newPayment_module.data}")
                      // Process newPayment_module.data as needed
                  } else {
                      Log.e("fetchPaymentUrl", "Error: Status is not 1")
                      // Handle other statuses if needed
                  }
              }, { error ->
                  // Handle API call error
                  Log.e("fetchPaymentUrl", "API Error: ${error.message}")
                  // Handle error condition, e.g., show error message
              })*/
    }
}