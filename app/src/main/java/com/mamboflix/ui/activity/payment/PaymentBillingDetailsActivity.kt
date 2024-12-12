package com.mamboflix.ui.activity.payment


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.mamboflix.BaseActivity
import com.mamboflix.HomeActivity3.HomeActivity3
import com.mamboflix.R
import com.mamboflix.databinding.ActivityPaymentBillingDetailsBinding
import com.mamboflix.databinding.DialogMsgBinding
import com.mamboflix.databinding.DialogPaymentresponseBinding
import com.mamboflix.ui.activity.payment.model.PackageListModel
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException


@Suppress("UNREACHABLE_CODE")
class PaymentBillingDetailsActivity : BaseActivity(), View.OnClickListener /*, PaymentResultListener*/ {
    private var binding: ActivityPaymentBillingDetailsBinding? = null
    private var pMpdel: PackageListModel? = null
    var value: Int = 0
    var amount: String = ""
    var offerPercent: String = "0"
    var country_code: String = ""
    var country_name: String = ""
    private var id:String=""
    private var order_id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_billing_details)

        var bundle: Bundle? = intent.extras
        if (bundle != null) {
            pMpdel = bundle.get("model") as PackageListModel?
            country_code = intent.getStringExtra("country_code").toString()
            country_name = intent.getStringExtra("country_name").toString()
        }


        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE
        )

        try {

            binding!!.tvValidity.text = pMpdel!!.packages
            binding!!.expiredon.text = pMpdel!!.expiry_date
            binding!!.tvFAmount.text = "$amount Only"
            binding!!.tvDAmount.text = pMpdel!!.amount
            binding!!.tvDAmount.paintFlags =
                binding!!.tvDAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding!!.tvPackTime.text = pMpdel!!.packages
            binding!!.tvNoUser.text = "${pMpdel!!.share_limit} User"  /*(${pMpdel!!.description})*/
            Glide.with(this).load(pMpdel!!.image).into(binding!!.image)

            setToolBar()



            amount = pMpdel!!.amount

        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (country_code == "255") {
            binding!!.btnPayment.visibility = View.GONE
        } else {
            binding!!.btnPayment.visibility = View.VISIBLE
        }
    }


    private fun setToolBar() {
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.package_details)
        binding!!.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        init()
    }
    private fun init() {
        try {

            binding!!.btnPayment.setOnClickListener(this)
            binding!!.btnProcceed.setOnClickListener(this)
            binding!!.btnPaymentbymobilemoney.setOnClickListener(this)
            if (pMpdel!!.is_purchase == "1") {
                binding!!.lytNext1.visibility = GONE
                binding!!.purchsed.visibility = VISIBLE
            } else {
                binding!!.lytNext1.visibility = VISIBLE
                binding!!.purchsed.visibility = GONE
            }
            if (pMpdel?.package_type == "1") {
                binding?.pack?.text = resources.getString(R.string.single_user)

                if (country_code == "91") {
                    country_code = "91"
                } else {
                    country_code = "255"
                }

            } else {
                binding!!.pack.text = resources.getString(R.string.family_pack)

            }

            binding!!.tvOffer.text = "$offerPercent%"

            if (country_code == "91") {
                binding!!.tvPrice.text = pMpdel!!.amount + country_name
            } else {
                binding!!.tvPrice.text = pMpdel!!.amount + country_name

            }

            Log.d("TAG", "init: " + pMpdel!!.expiry_date.toString())


            try {
                if (country_code == "91") {
                    binding!!.tvDPrice.text = pMpdel!!.discount + country_name
                } else {
                    binding!!.tvDPrice.text = pMpdel!!.discount + country_name
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPayment -> {
                callPaymentAPIAPI()
            }
            R.id.btnPaymentbymobilemoney -> {
                if (userPref.user.mobile != null) {
                    callPaymentbyMobileMoney()
                } else {
                    utils.simpleAlert1(
                        this,
                        resources.getString(R.string.alert),
                        resources.getString(R.string.emptymobileno), 1
                    )
                }
            }
            R.id.btnProcceed -> {

                fetchPaymentUrl()

            }
        }
    }

    private fun callPaymentbyMobileMoney() {
        apiService.callmobilepayment(
            userPref.user.email,
            pMpdel!!.id.toString(),
            amount,
            "1",
            userPref.user.name,
            pMpdel!!.discount,
            userPref.user.id,
            userPref.user.mobile
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status == 1) {
                    if (commonResponse.mdata!!.final.resultcode == "000") {
                        simpleAlert1(
                            this,
                            "Success",
                            commonResponse.message!!
                        )
                        simpleAlert1(
                            this,
                            "Success",
                            commonResponse.mdata.order_id!!
                        )
                    }
                } else {
                    utils.simpleAlert1(
                        this,
                        "Error",
                        commonResponse.message.toString(), 1
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


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun fetchPaymentUrl() {
        val token = userPref.getToken()?.toString() ?: ""
        id = pMpdel?.id?.toString() ?: ""

        Log.d("paymentDetails", "Payment Model ID: $id")
        Log.d("paymentDetails", "User Token: $token")

        // Check if token or id is empty before making the API call
        if (token.isEmpty() || id.isEmpty()) {
            Log.e("fetchPaymentUrl", "Token or ID is null or empty")
            return
        }
        apiService.fetchPaymentUrl("Bearer " + userPref.getToken(), id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({newPayment_module ->
                if (newPayment_module.status == 1) {
                    order_id = newPayment_module.order_id.toString()
                    startActivity(
                        Intent(this, MakePaymentActivity::class.java)
                            .putExtra("url", newPayment_module.data.toString())
                            .putExtra("id", id.toString())
                            .putExtra("order_id", newPayment_module.order_id.toString())
                    )

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

    }


    private fun callPaymentAPIAPI() {
        apiService.callPaymentAPIAPI(
            userPref.user.id,
            userPref.user.email,
            pMpdel!!.discount,
            pMpdel!!.id.toString(),
            amount,
            userPref.user.name,
            userPref.user.mobile,
            userPref.user.name,
            userPref.user.name,
            "Noida",
            "Noida",
            "up",
            "25635",
            "TZ",
            userPref.user.mobile,
            userPref.user.name,
            userPref.user.name,
            "Noida",
            "Noida",
            "up",
            "25635",
            "TZ",
            userPref.user.mobile,
            "1",
            userPref.getFcmToken().toString()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.result == "Success" && commonResponse.mdata != null) {

                    Toast.makeText(this, "Payment Successfully", Toast.LENGTH_SHORT).show()

                    val intent= Intent(this,HomeActivity3::class.java)
                    startActivity(intent)

                } else {
                    utils.simpleAlert(
                        this,
                        "Error",
                        commonResponse.message.toString()
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
    }

    fun simpleAlert1(context: Context, title: String, message: String) {

        var cDialog = Dialog(context, R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogMsgBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_msg,
            null,
            false
        )
        cDialog.setContentView(bindingDialog.root)
        cDialog.setCancelable(false)
        cDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //}

        cDialog.show()
        bindingDialog.tvTitle.text = title
        bindingDialog.tvMsg.text = message
        bindingDialog.btCancel.text = context.resources.getString(R.string.okay)

        bindingDialog.btCancel.setOnClickListener {
            showProgressDialog()

            hideProgressDialog()

            showstatusDialog(1)

            cDialog.dismiss()
        }

    }

    private fun showstatusDialog(i: Int) {
        var cDialog = Dialog(this, R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogPaymentresponseBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_paymentresponse,
            null,
            false
        )
        cDialog.setContentView(bindingDialog.root)
        cDialog.setCancelable(false)
        cDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        cDialog.show()
        if (i == 1) {
            bindingDialog.titlemsg.text = resources.getString(R.string.titlemsg)
            bindingDialog.details.text = resources.getString(R.string.successmsg)
            bindingDialog.btCancel.text = this.resources.getString(R.string.okay)
            bindingDialog.popup.setBackgroundResource(R.drawable.popup1)
        } else if (i == 2) {
            bindingDialog.titlemsg.text = resources.getString(R.string.wait)
            bindingDialog.details.text = resources.getString(R.string.pending)
            bindingDialog.btCancel.text = this.resources.getString(R.string.okay)
            bindingDialog.popup.setBackgroundResource(R.drawable.popup2)
        } else {
            bindingDialog.titlemsg.text = resources.getString(R.string.fail)
            bindingDialog.details.text = resources.getString(R.string.failed)
            bindingDialog.btCancel.text = this.resources.getString(R.string.tryagain)
            bindingDialog.popup.setBackgroundResource(R.drawable.popup3)
        }

        bindingDialog.btCancel.setOnClickListener {
            if (i == 1 || i == 2) {
                startActivity(Intent(this, HomeActivity3::class.java))
            } else {
                startActivity(Intent(this, PaymentBillingActivity::class.java))
            }
            cDialog.dismiss()
        }
    }

}


