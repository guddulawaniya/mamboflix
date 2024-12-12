package com.mamboflix.ui.activity.signup

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.mamboflix.BaseActivity
import com.mamboflix.OtpEditText
import com.mamboflix.R
import com.mamboflix.databinding.ActivitySignUpBinding
import com.mamboflix.ui.activity.initial.VideoSplashActivity
import com.mamboflix.ui.activity.login.LoginActivity
import com.mamboflix.utils.Constants
import kotlinx.coroutines.CoroutineScope
import org.json.JSONException
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.coroutines.CoroutineContext

class SignUpActivity() : BaseActivity(), View.OnClickListener, CoroutineScope {
    private lateinit var binding: ActivitySignUpBinding
    private var isVisible2 = false
    private var isVisible1 = false
    private var validation: AwesomeValidation? = null
    private val EMAIL = "email"
    var verification_value: String = "1"
    lateinit var bottomSheet: BottomSheetDialog
    lateinit var bottomSheet_already_exit: BottomSheetDialog
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var callbackManager: CallbackManager? = null
    private var loggedOut: AccessToken? = null
    private val RC_SIGN_IN = 1
    private var userName: String? = ""
    private var userName_send_otp_parameter: String? = ""
    private var email: String? = ""
    private var Get_api_OTP: String? = ""
    private var socialAccountId: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        bottomSheet = BottomSheetDialog(this@SignUpActivity)
        bottomSheet_already_exit = BottomSheetDialog(this@SignUpActivity)

        validation()
        setListener()
        getDeviceToken()
        initGoogleSignIn()
        facebookLogin()

        val tm: TelephonyManager =
            this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCodeValue: String = tm.getNetworkCountryIso()
        binding.ccp.setCountryForNameCode(countryCodeValue.toUpperCase())


    }
    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN: String =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{6,}"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)

        return matcher.matches()
    }

    public override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        intLocation()
    }

    private fun validation() {
        validation = AwesomeValidation(ValidationStyle.BASIC)
        validation!!.addValidation(
            this,
            R.id.edt_name,
            "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$",
            R.string.nameerror
        )
        validation!!.addValidation(
            this,
            R.id.edt_email,
            Patterns.EMAIL_ADDRESS,
            R.string.emailerror
        )
        validation!!.addValidation(this, R.id.edt_mobile, Patterns.PHONE, R.string.mobile)
        validation!!.addValidation(this, R.id.edtPassword, Constants.EMPTY_REGEX, R.string.password)
        validation!!.addValidation(
            this,
            R.id.edt_confirm_password,
            Constants.EMPTY_REGEX,
            R.string.cpassword
        )
        /*awesomeValidation!!.addValidation(this, R.id.edt_company, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.company)*/

    }

    private fun getDeviceToken() {
        /*FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                token = task.result!!.token
                Log.d("token", token!!)
            })*/
    }

    private fun setListener() {
        binding.radioEmail.setOnClickListener(this)
        binding.radioPhoneNumber.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
        binding.tvSignup.setOnClickListener(this)
        binding.llGoogleSignIn.setOnClickListener(this)
        binding.llFacebookLogin.setOnClickListener(this)
        binding.lytVisiblePass.setOnClickListener {
            if (isVisible1) {
                binding.edtPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisiblePass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.not_visible
                        )
                    )
                    binding.edtPassword.setSelection(binding.edtPassword.text.length)
                }
                isVisible1 = false
            } else { //Toast.makeText(this,"show",Toast.LENGTH_SHORT).show();
                binding.edtPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisiblePass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.not_visible_hover
                        )
                    )
                    binding.edtPassword.setSelection(binding.edtPassword.text.length)
                }
                isVisible1 = true
            }
        }

        binding.lytVisibleConfirmPass.setOnClickListener {
            if (isVisible2) {
                binding.edtConfirmPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisibleConfirmPass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.not_visible
                        )
                    )
                    binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.text.length)
                }
                isVisible2 = false
            } else { //Toast.makeText(this,"show",Toast.LENGTH_SHORT).show();
                binding.edtConfirmPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisibleConfirmPass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.not_visible_hover
                        )
                    )
                    binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.text.length)
                }
                isVisible2 = true
            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnSignUp -> {
                Log.d("TAG", "onClick: " + binding.ccp.selectedCountryName)
                /*startActivity(Intent(this, InterestActivity::class.java))
                finish()*/

//                if(validation!!.validate()) {
//                    if (binding.edtPassword.text.toString() == binding.edtConfirmPassword.text.toString()){
//
//                        callSignUpAPI("1")
////                        callValidationApi()
//                    }else{
//                        utils.simpleAlert(this,"Error", "Password do not match" )
//                    }
//                }

                if (binding.edtName.text.toString().isEmpty()) {
                    Toast.makeText(this@SignUpActivity, "Please enter name", Toast.LENGTH_SHORT)
                        .show()
                } else if (binding.edtEmail.text.toString().isEmpty()) {
                    Toast.makeText(this@SignUpActivity, "Please enter email", Toast.LENGTH_SHORT)
                        .show()

                } else if (!binding.edtEmail.text!!.matches(emailPattern.toRegex())) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Please enter correct email",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                } else if (binding.edtMobile.text.toString().isEmpty()) {
                    Toast.makeText(this@SignUpActivity, "Please enter mobile", Toast.LENGTH_SHORT)
                        .show()
                } else if (binding.edtMobile.text.toString().length != 10) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Please at least 10 digit mobile number ",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (binding.edtPassword.text.toString().isEmpty()) {
                    Toast.makeText(this@SignUpActivity, "Please enter password", Toast.LENGTH_SHORT)
                        .show()
                } else if (!isValidPassword(binding.edtPassword.text.toString())) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Password should be strong password",
                        Toast.LENGTH_SHORT
                    ).show()
                }  else if (binding.edtPassword.text.toString() != binding.edtConfirmPassword.text.toString()) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Password and confirm password should be same.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    send_otp_api_response()


                }


            }
            R.id.tvSignup -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.radio_email -> {
                verification_value = "2"
                binding.radioEmail.isChecked = true
                binding.radioPhoneNumber.isChecked = false
            }

            R.id.radio_phone_number -> {
                verification_value = "1"
                binding.radioPhoneNumber.isChecked = true
                binding.radioEmail.isChecked = false
            }

            R.id.llGoogleSignIn -> {
                val signInIntent = mGoogleSignInClient!!.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }

            R.id.llFacebookLogin -> {

            }


        }
    }

    private fun open_opt_pop(type:String) {
        val dialogBinding = LayoutInflater
            .from(this@SignUpActivity).inflate(R.layout.otp_verify_screen_popup, null)

        bottomSheet.setContentView(dialogBinding)
        bottomSheet.setOnShowListener { dia ->
            val bottomSheetDialog = dia as BottomSheetDialog
            val bottomSheetInternal: FrameLayout =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            bottomSheetInternal.setBackgroundResource(R.drawable.rounded_top_corners)
        }
        bottomSheet.setCancelable(true)

        val textViewNumber: TextView = bottomSheet.findViewById(R.id.textViewNumber)!!
        val tv_resend_otp: TextView = bottomSheet.findViewById(R.id.tv_resend_otp)!!

        val otpTv = bottomSheet.findViewById<TextView>(R.id.otpTv)
//        otpTv?.text = resources.getString(R.string.auto_verifying_your_otp_in)
        val veriefyOtp = bottomSheet.findViewById<AppCompatButton>(R.id.otpVerification)
        val et_otp = bottomSheet.findViewById<OtpEditText>(R.id.et_otp)

        if (type=="1"){
            textViewNumber.text =
                "Please Wait....\nwe will auto fill the OTP send to (+${binding.ccp.selectedCountryCode}) ${binding.edtMobile.text.toString()}."
        }else{
            textViewNumber.text =
                "Please Wait....\nwe will auto fill the OTP send to ${binding.edtEmail.text.toString()}."
        }
//        et_otp!!.setText(otp.toString())
//        if (otp!!.length == 4) {
//            veriefyOtp!!.isEnabled = true
//            veriefyOtp.isClickable = true
//            veriefyOtp.backgroundTintList =
//                ColorStateList.valueOf(resources.getColor(R.color.black))
//        }
        val countTv = bottomSheet.findViewById<TextView>(R.id.countTv)
//        countdown(countTv)


        object : CountDownTimer(10000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                /*CodeGreen.visibility = View.GONE
                Code.visibility = View.VISIBLE
                ResendCode.isEnabled= false*/
                tv_resend_otp.visibility = View.GONE
                if (millisUntilFinished / 1000 < 10) {
                    countTv!!.text =
                        "(" + "00:0" + millisUntilFinished / 1000 + ")"/*"00:0" + millisUntilFinished / 1000*/

                } else {
                    countTv!!.text = "(" + "00:" + millisUntilFinished / 1000 + ")"

                }
            }

            @SuppressLint("ResourceAsColor")
            override fun onFinish() {
                countTv!!.text = "(00:00)"
                tv_resend_otp.visibility = View.VISIBLE
                /* CodeGreen.visibility = View.VISIBLE
                 Code.visibility = View.GONE
                 ResendCode.isEnabled= true*/
            }
        }.start()


        tv_resend_otp.setOnClickListener() {
            send_otp_api_response()
        }

        et_otp!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val getOtp = p0.toString()
                if (getOtp.length == 4) {
                    val imm: InputMethodManager? =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
                    imm?.hideSoftInputFromWindow(et_otp.getWindowToken(), 0);

                } else {

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        veriefyOtp!!.setOnClickListener {
            if (Get_api_OTP == et_otp.text.toString()) {
//                progressbar.showProgress(this)
                verify_otp()


            } else {
                Toast.makeText(this, "Invalid Otp", Toast.LENGTH_SHORT).show()
            }

        }
        bottomSheet.show()
    }

    fun send_otp_api_response() {
        if (verification_value == "1") {
            userName_send_otp_parameter = binding.edtMobile.text.toString()
        } else if (verification_value == "2") {
            userName_send_otp_parameter = binding.edtEmail.text.toString()
        }

        apiService.send_otp(verification_value,binding.edtMobile.text.toString(),binding.ccp.selectedCountryCode.toString(),binding.edtEmail.text.toString(), "English")

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status == 1) {
                    open_opt_pop(verification_value)
                    Get_api_OTP = commonResponse.mdata!!.otp.toString()
                    Toast.makeText(
                        this@SignUpActivity,
                        "" + commonResponse.mdata!!.otp,
                        Toast.LENGTH_SHORT
                    ).show()
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


    fun verify_otp() {
        apiService.verify_otp(userName_send_otp_parameter.toString(), Get_api_OTP.toString())

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status == 1) {
                    callSignUpAPI("1")
//                    Toast.makeText(this@SignUpActivity, "" + commonResponse.otp, Toast.LENGTH_SHORT)
//                        .show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                userName = account!!.displayName
                email = account.email
                socialAccountId = account.id
                callSignUpSocialAPI("2")
                //  utils.toaster("Signed In Successful" + "Welcome" + account.displayName)
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.e("TAG", "signInResult:failed code=" + e.statusCode)
            }
        } else {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun callSignUpAPI(mType: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", binding?.edtName?.text.toString())
        jsonObject.addProperty("email", binding?.edtEmail?.text.toString())
        jsonObject.addProperty("password", binding?.edtPassword?.text.toString())
        jsonObject.addProperty("mobile", binding?.edtMobile?.text.toString())
        jsonObject.addProperty("type", mType)
        jsonObject.addProperty("device_id", "Android")
        jsonObject.addProperty("device_name", "Android")
        jsonObject.addProperty("device_type", "Android")
        jsonObject.addProperty("device_token", userPref.getFcmToken())
        jsonObject.addProperty("lang_type", "English")
        jsonObject.addProperty("country_code", binding.ccp.selectedCountryCode)
        jsonObject.addProperty("country", binding.ccp.selectedCountryName)
        jsonObject.addProperty("device_token", userPref.getFcmToken().toString())


        apiService.callSignUpAPI(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)

            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    userPref.isLogin = true
                    userPref.user = commonResponse.mdata.user
                    userPref.setToken(commonResponse.mdata.token)
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



    private fun intLocation() {
        /*if (isPermissionGiven()){

            getCurrentLocation()
        } else {
            givePermission()
        }*/
    }



    private fun initGoogleSignIn() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.server_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }


    private fun facebookLogin() {
        loggedOut = AccessToken.getCurrentAccessToken()
        callbackManager = CallbackManager.Factory.create()
        binding!!.loginButton.setPermissions(listOf(EMAIL))

        binding!!.loginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val loggedIn = AccessToken.getCurrentAccessToken()
                    Log.d("API123", "$loggedIn ??")
                    getUserProfile(loggedIn)
                }

                override fun onCancel() {
                    Log.d("API123", "Cancel")
                }

                override fun onError(error: FacebookException?) {
                    Log.d("API123", "Cancel")
                }

            })

    }

    private fun getUserProfile(currentAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
            currentAccessToken
        ) { `object`, response ->
            Log.d("TAG", `object`.toString())
            try {
                userName = `object`.getString("first_name")
                val last_name = `object`.getString("last_name")
                email = `object`.getString("email")
                socialAccountId = `object`.getString("id")
                callSignUpSocialAPI("3")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        request.parameters = parameters
        request.executeAsync()
    }


    private fun callSignUpSocialAPI(mType: String) {
        val jsonObject = JsonObject()
        if (mType == "2") {
            jsonObject.addProperty("google_id", socialAccountId)
        } else if (mType == "3") {
            jsonObject.addProperty("facebook_id", socialAccountId)
        }
        jsonObject.addProperty("name", userName)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("password", "")
        jsonObject.addProperty("mobile", "")
        jsonObject.addProperty("type", mType)
        jsonObject.addProperty("device_id", "1")
        jsonObject.addProperty("device_name", "Android")
        jsonObject.addProperty("device_type", "Android")
        jsonObject.addProperty("device_token", userPref.getFcmToken())
        jsonObject.addProperty("country_code", binding.ccp.selectedCountryCode)
        jsonObject.addProperty("country", binding.ccp.selectedCountryName)


        apiService.callSignUpAPI(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)

            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {

                    if (mType == "2") {
                        userPref.setLoginType("2")
                    } else if (mType == "3") {
                        userPref.setLoginType("3")
                    }
                    userPref.isLogin = true

                    userPref.user = commonResponse.mdata.user
                    userPref.setToken(commonResponse.mdata.token)
                    userPref.setSubUserId(commonResponse.mdata.user.id)
                    userPref.setSubUserName(commonResponse.mdata.user.name)
                    startActivity(Intent(this, VideoSplashActivity::class.java))
                    //startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else if (commonResponse.status == 0) {
                    userPref.isLogin = true
                    userPref.user = commonResponse.mdata!!.user
                    userPref.setToken(commonResponse.mdata!!.token)


                    if (commonResponse.message == "already exist!!") {
                        open_already_exit()
                    } else {
                        startActivity(Intent(this, InterestActivity::class.java))
                    }


//                    finish()
                } else {
                    if (mType == "2") {
                        googleSignOut()
                    } else if (mType == "3") {
                        facebookLogout()
                    }
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



    private fun open_already_exit() {
        // Inflate the dialog's layout
        val dialogBinding1 = LayoutInflater.from(this).inflate(R.layout.already_exit_popup, null)

        // Set the content view for the BottomSheetDialog
        bottomSheet_already_exit.setContentView(dialogBinding1)

        // Customize the BottomSheetDialog appearance
        bottomSheet_already_exit.setOnShowListener { dia ->
            val bottomSheetDialog = dia as BottomSheetDialog
            val bottomSheetInternal: FrameLayout =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            bottomSheetInternal.setBackgroundResource(R.drawable.rounded_top_corners)
        }

        // Make the dialog not cancelable
        bottomSheet_already_exit.setCancelable(false)

        // Initialize the buttons
        val tvOk: TextView = dialogBinding1.findViewById(R.id.tv_ok)
        val tvCancel: TextView = dialogBinding1.findViewById(R.id.tv_cancel)

        // Set click listener for the OK button
        tvOk.setOnClickListener {
            startActivity(Intent(this, InterestActivity::class.java))
            bottomSheet_already_exit.dismiss()
        }

        // Set click listener for the Cancel button
        tvCancel.setOnClickListener {
            bottomSheet_already_exit.dismiss()
        }

        // Show the BottomSheetDialog
        bottomSheet_already_exit.show()
    }



    private fun googleSignOut() {
        mGoogleSignInClient!!.signOut().addOnCompleteListener(
            this
        ) { }

    }

    private fun facebookLogout() {
        LoginManager.getInstance().logOut()
    }

    override val coroutineContext: CoroutineContext
        get() = coroutineContext

}