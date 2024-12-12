package com.mamboflix.ui.activity.forgotpassword

import `in`.aabhasjindal.otptextview.OTPListener
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonObject
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityOtpVerifyBinding
import com.mamboflix.smsgateway.RetrofitClient
import com.mamboflix.ui.activity.signup.InterestActivity
import com.mamboflix.ui.broadcast.OTPReceiver
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.*

class OtpVerifyActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOtpVerifyBinding
    private var myOtp:String? = ""
    private var phoneOrEmail:String? = ""
    private var countryCode:String?=""
    private var screenType: String? = ""
    private var userName: String? = ""
    private var userEmail: String? = ""
    private var userPassword: String? = ""
    private var loginStatus: Boolean = false
    private var openType:Int?= null  //1=Signup 0 =Forgot password
    var tokenId:String?=null
    val PERMISSIONS_RECEIVE_SMS = 42
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verify)
        setToolBar()
        screenType = intent.extras!!.getString("screenType")
        if (screenType=="1"){
            userName = intent.extras!!.getString("name")
            userEmail = intent.extras!!.getString("email")
            userPassword = intent.extras!!.getString("password")
            }
            myOtp = intent.extras!!.getString("otp")
            phoneOrEmail = intent.extras!!.getString("phone")
            openType = intent.extras!!.getInt("otptype")
            countryCode = intent.extras!!.getString("country_code")
         if (openType==1){
              binding.tvSubTitle.text = "OTP has been sent to your phone number"
           }else if (openType==2){
            binding.tvSubTitle.text = "OTP has been sent to your email id"
           }
            window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        requestPermissionForSMS()
        binding!!.otpView.setOTP(myOtp.toString())
        binding.btnOtpVerify.setOnClickListener(this)
        binding.tvResendOtp.setOnClickListener(this)
        OTPReceiver().setEditText(binding!!.otpView)
        binding!!.otpView?.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }
            override fun onOTPComplete(otp: String) {

            }
        }

       /* binding!!.otpView?.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {
               *//* myOtp = otp
                if (userPref.getOTP().equals(myOtp.toString())) {

                    if (phoneOtp != "") {

                        tokenId?.let { callCheckAccountAPI(phoneOtp!!, "2", it) }
                    } else {
                        Toast.makeText(this@OtpActivity, "Incorrect Mobile Number", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("otpMatch", myOtp)
                    Toast.makeText(this@OtpActivity, "Incorrect Verification Code $myOtp", Toast.LENGTH_SHORT).show()
                }*//*
            }
        }*/
        TimeCountDown()
    }
    fun TimeCountDown(){
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if(millisUntilFinished / 1000<10){
                    binding.tvTimer.setText("00:0" + millisUntilFinished / 1000)
                }else{
                    binding.tvTimer.setText("00:" + millisUntilFinished / 1000)

                }
                binding.tvResendOtp.isEnabled = false
//                binding.tvResendOtp.setBackgroundDrawable(resources.getDrawable(R.drawable.buttondisable))

            }

            override fun onFinish() {
                binding.tvTimer.setText("00:00")
                binding.tvResendOtp.isEnabled = true
//                binding.tvResendOtp.setBackgroundDrawable(resources.getDrawable(R.drawable.button))

            }
        }.start()
    }
    private fun setToolBar(){
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Back"
        binding!!.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
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
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.tv_resend_otp -> {
                callForgotAPI(phoneOrEmail.toString())
               /* utils.hideKeyboard(binding.tvResendOtp)

                    var otp = String.format("%04d", Random().nextInt(9999 - 1000 + 1) + 1000)
                var msg = "Your Mamboflix OTP is $otp"

                //callloginSmsAPI(msg,countryCode+phoneOrEmail!!)

                    userPref.setOTP(otp)
                    binding!!.otpView.setOTP(otp)
                myOtp = otp*/
                    //calReSendOtpRequestAPI()

            }
            R.id.btn_otp_verify -> {
                utils.hideKeyboard(binding.btnOtpVerify)

                myOtp = binding!!.otpView.otp.toString()
                if (userPref.getOTP().equals(myOtp.toString())) {

                    if (screenType=="1"){
                        callSignUpAPI("1",userName.toString(),userEmail.toString()
                                ,userPassword.toString(),phoneOrEmail.toString())
                    }else{


                        val intent =Intent(this, UpdatePasswordActivity::class.java)
                            .putExtra("phone",phoneOrEmail)
                        startActivity(intent)
                    }

                } else {
                    Toast.makeText(this, "Incorrect Verification Code $myOtp", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
    private fun callForgotAPI(mobile:String) {
        apiService.callgetotp(openType!!,mobile,userPref.getPreferredLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)

                .subscribe({ commonResponse ->
                    if (commonResponse.status !=0 ) {
                        userPref.setOTP(commonResponse.mdata!!.otp)
                        //          Toast.makeText(this,commonResponse.message, Toast.LENGTH_SHORT).show()
                        binding!!.otpView.setOTP(commonResponse.mdata!!.otp.toString())
                        TimeCountDown()
                    } else {
                        utils.simpleAlert(
                                this,
                                getString(R.string.alert),
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


    private fun callloginSmsAPI(yourOtp:String, mobileNumber:String) {
       // var SmsID: String  = String.format("%04d", Random().nextInt(9999 - 1000 + 1) + 1000)

        RetrofitClient.instance.callSmsAPI("mambotvapi","mambotvapi",
                mobileNumber,yourOtp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)
                .subscribe({ commonResponse ->
                    if (commonResponse.results!=null && commonResponse.results.size>0) {
                        if (commonResponse.results[0].statustext=="OK"){
                            userPref.setOTP(yourOtp)
                            Toast.makeText(applicationContext, "OTP send successfully", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(applicationContext, commonResponse.results[0].statustext, Toast.LENGTH_LONG).show()
                        }

                    } else {
                        utils.simpleAlert(
                                this,
                                "Error",
                                "OTP fail"
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
                            getString(R.string.check_network_connection)
                        )
                    }
                })
    }

    private fun requestPermissionForSMS() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.RECEIVE_SMS
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECEIVE_SMS),
                    PERMISSIONS_RECEIVE_SMS
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_RECEIVE_SMS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //  Log.d(TAG, "PERMISSIONS_RECEIVE_SMS permission granted")


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Log.d(TAG, "PERMISSIONS_RECEIVE_SMS permission denied")
                }
                return
            }
        }
    }



    private fun callSignUpAPI(mType:String, name:String, email:String,
    password:String,mobile:String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", name)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("password", password)
        jsonObject.addProperty("country_code", countryCode)
        jsonObject.addProperty("mobile", mobile)
        jsonObject.addProperty("type",mType)
        jsonObject.addProperty("device_id", "131")
        jsonObject.addProperty("device_name", "Android")
        jsonObject.addProperty("device_type", "Android")
        jsonObject.addProperty("device_token", userPref.getFcmToken())


        apiService.callSignUpAPI(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)

                .subscribe({ commonResponse ->
                    if (commonResponse.status !=0 && commonResponse.mdata != null) {
                        userPref.isLogin= true
                        userPref.user = commonResponse.mdata.user
                        userPref.setToken(commonResponse.mdata.token)
                        userPref.setSubUserId(commonResponse.mdata.user.id)
                        userPref.setSubUserName(commonResponse.mdata.user.name)
                        startActivity(Intent(this, InterestActivity::class.java))
                        //startActivity(Intent(this, HomeActivity::class.java))
                        finish()
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
                                getString(R.string.check_network_connection)
                        )
                    }
                })
    }

}
