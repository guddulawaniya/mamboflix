package com.mamboflix.ui.activity.forgotpassword

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityForgotPasswordBinding
import com.mamboflix.smsgateway.RetrofitClient
import com.mamboflix.utils.Constants
import com.mamboflix.utils.IpInfoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.*

class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityForgotPasswordBinding
    private var validation: AwesomeValidation? = null
    private var validation1: AwesomeValidation? = null
    private var otp: String? = null
    private var type = "email"
    private var type1 = 2
    var ccp = "+91"
    var value: Int = 0
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        setToolBar()
        binding.etEmail.editText!!.requestFocus()
        binding.etEmail.editText!!.isCursorVisible = true
        initializeValidation()
        clicListenerView()
        value = intent.getIntExtra("value", 0)
        if (value == 1) {
            binding.etEmail.editText!!.setText(userPref.user.email)
            binding.etPhone.editText!!.setText(userPref.user.mobile)
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        val tm: TelephonyManager =
            this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCodeValue: String = tm.getNetworkCountryIso()
        //  Toast.makeText(this,countryCodeValue.toUpperCase(),Toast.LENGTH_LONG).show()
        binding.ccp.setCountryForNameCode(countryCodeValue.toUpperCase())
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ipinfo.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(IpInfoService::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = service.getIpInfo()
                val country = response.country
                Log.d("TAG", "Country from IPinfo: $country")
               if (country.equals("TZ")){
                   binding.rbMobile.visibility=View.VISIBLE
               } else{
                binding.rbMobile.visibility=View.GONE
               }
                // Use the country information as needed
                } catch (e: Exception) {
                Log.e("TAG", "Error fetching IPinfo", e)
                // Handle network request or parsing errors
            }
        }
    }

    fun clicListenerView() {
        binding.btnProceed.setOnClickListener(this)
        //binding!!.tvSignup.setOnClickListener(this)
        //binding!!.tvForgot.setOnClickListener(this)
        binding.rbEmail.isChecked = true

        binding.rgAccount.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbEmail) {
                type = "email"
                binding.etEmail.visibility = View.VISIBLE
                binding.etPhone.visibility = View.GONE
                binding.mobilell.visibility = View.GONE
                type1 = 2
                binding.etPhone.editText!!.isCursorVisible = false
                binding.etEmail.editText!!.requestFocus()
                binding.etEmail.editText!!.isCursorVisible = true
            }
            if (checkedId == R.id.rbMobile) {
                type = "mobile"
                type1 = 1
                binding.etEmail.editText!!.isCursorVisible = false
                binding.etPhone.editText!!.requestFocus()
                binding.etPhone.editText!!.isCursorVisible = true
                binding.etEmail.visibility = View.GONE
                binding.mobilell.visibility = View.VISIBLE
                binding.etPhone.visibility = View.VISIBLE

            }
        }


    }

    private fun setToolBar() {
        setSupportActionBar(binding.toolbar)
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

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btnProceed -> {
                //0>> email...... 1>>Phone

                //    otp = String.format("%04d", Random().nextInt(9999 - 1000 + 1) + 1000)
                var code: String = binding?.ccp!!.selectedCountryCode.toString()
                if (type.equals("email", ignoreCase = true)) {

                    if (!binding.etEmail.editText!!.text!!.matches(emailPattern.toRegex())) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Please enter correct email",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        callForgotAPI(binding.etEmail.editText!!.text.toString().trim())

                    }
                    //  userPref.setOTP(otp)
                    /*   startActivity(
                           Intent(this, OtpVerifyActivity::class.java)
                               .putExtra("otp", otp)
                               .putExtra("phone", binding!!.etEmail.editText!!.text.toString())
                               .putExtra("otptype",0)
                               .putExtra("country_code", code)
                       )*/
                } else if (type.equals("mobile", ignoreCase = true)) {
                    // var msg = "Your Mamboflix OTP is $otp"

                    //
                    if (binding.etPhone.editText!!.text.length < 10) {
                        Toast.makeText(this, "Please at least 10 digit number", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        callForgotAPI(binding.etPhone.editText!!.text.toString())
                    }


                    //callloginSmsAPI(msg,code+binding!!.etPhone.editText!!.text.toString())
                }
            }

            R.id.tvForgot -> {

            }

            else -> {
            }

        }
    }

    private fun initializeValidation() {
        validation = AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT)
        validation!!.addValidation(
            binding.etEmail,
            Constants.EMPTY_REGEX,
            "Please enter the valid Email."
        )

        validation1 = AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT)
        validation1!!.addValidation(binding.etPhone, Patterns.PHONE, "Please enter mobile number")
    }

    private fun callForgotAPI(mobile: String) {
        apiService.callgetotp(type1, mobile, userPref.getPreferredLanguage())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)

            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
                    var code: String = binding?.ccp!!.selectedCountryCode.toString()
                    userPref.setOTP(commonResponse.mdata!!.otp)
                    //          Toast.makeText(this,commonResponse.message, Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(this, OtpVerifyActivity::class.java)
                            .putExtra("screenType", "2")
                            .putExtra("otp", commonResponse.mdata.otp)
                            .putExtra("phone", mobile)
                            .putExtra("otptype", type1)
                            .putExtra("country_code", code)
                    )
                    finish()
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


    /* private fun callForgotPasswordAPI(type : String, countryCode: String) {

        val jsonObject = JsonObject()

        if (type == "1"){
            jsonObject.addProperty("mobile_no", binding?.etPhone?.editText!!.text.toString())
            jsonObject.addProperty("country_code", countryCode)
            jsonObject.addProperty("type", type)

        }else{
            jsonObject.addProperty("email", binding?.etEmail?.editText!!.text.toString())
            jsonObject.addProperty("country_code", countryCode)
            jsonObject.addProperty("type", type)
        }


        apiService.callForgotPasswordAPI(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)


            .subscribe({ commonResponse ->

                if (commonResponse.status == "1" && commonResponse.data != null) {

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
                    *//*utils.simpleAlert(
                        this,
                        "",
                        ""
                    )*//*

                } else {
                    *//* utils.simpleAlert(
                         this,
                         "",
                         throwable.message.toString()
                     )*//*
                }
            })
    }*/
    private fun callloginSmsAPI(yourOtp: String, mobileNumber: String) {
        // var SmsID: String  = String.format("%04d", Random().nextInt(9999 - 1000 + 1) + 1000)

        RetrofitClient.instance.callSmsAPI(
            "mambotvapi", "mambotvapi",
            mobileNumber, yourOtp
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.results != null && commonResponse.results.size > 0) {
                    if (commonResponse.results[0].statustext == "OK") {
                        var code: String = binding?.ccp!!.selectedCountryCode.toString()
                        userPref.setOTP(otp)
                        startActivity(
                            Intent(this, OtpVerifyActivity::class.java)
                                .putExtra("screenType", "2")
                                .putExtra("otp", otp)
                                .putExtra("phone", binding!!.etPhone.editText!!.text.toString())
                                .putExtra("otptype", 1)
                                .putExtra("country_code", code)
                        )
                        Toast.makeText(
                            applicationContext,
                            "OTP send successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            commonResponse.results[0].statustext,
                            Toast.LENGTH_LONG
                        ).show()
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
                        throwable.message.toString()
                    )

                }

            })
    }

}
