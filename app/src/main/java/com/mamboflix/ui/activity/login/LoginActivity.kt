package com.mamboflix.ui.activity.login

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityLoginBinding
import com.mamboflix.ui.activity.forgotpassword.ForgotPasswordActivity
import com.mamboflix.ui.activity.initial.LanguageActivity
import com.mamboflix.ui.activity.initial.VideoSplashActivity
import com.mamboflix.ui.activity.signup.InterestActivity
import com.mamboflix.ui.activity.signup.SignUpActivity
import com.mamboflix.ui.activity.signup.WatchTypeActivity
import com.mamboflix.utils.Constants
import org.json.JSONException
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException
import java.net.ConnectException
import java.util.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    private var binding: ActivityLoginBinding? = null
    private var validation: AwesomeValidation? = null
    private var myFirebaseToken: String? = null
    private val RC_SIGN_IN = 1
    private val EMAIL = "email"
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var callbackManager: CallbackManager? = null
    private var loggedOut: AccessToken? = null
    private var userName: String? = ""
    private var email: String? = ""
    private var countryCode: String? = ""
    private var countryName: String? = ""
    private var socialAccountId: String? = ""
    lateinit var bottomSheet_already_exit_login: BottomSheetDialog
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var loginButton: LoginButton
    private var isVisible1 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        bottomSheet_already_exit_login = BottomSheetDialog(this@LoginActivity)
        FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext())
        callbackManager = CallbackManager.Factory.create();
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
//        AppEventsLogger.activateApp(this@LoginActivity);
        initializeValidation()
        clicListenerView()
        initView()
        myFirebaseToken = FirebaseInstanceId.getInstance().token

        userPref.setFcmToken(myFirebaseToken)
        loginButton = findViewById(R.id.login_button)
        binding!!.lytVisiblePass.setOnClickListener {
            if (isVisible1) {
                binding!!.edtPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding!!.ivVisiblePass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.not_visible
                        )
                    )
                    binding!!.edtPassword.setSelection(binding!!.edtPassword.text.length)
                }
                isVisible1 = false
            } else { //Toast.makeText(this,"show",Toast.LENGTH_SHORT).show();
                binding!!.edtPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding!!.ivVisiblePass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.not_visible_hover
                        )
                    )
                    binding!!.edtPassword.setSelection(binding!!.edtPassword.text.length)
                }
                isVisible1 = true
            }
        }


        //Log.e("Token fcm", "Token>>$myFirebaseToken")
        initGoogleSignIn()
        facebookLogin()
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
    }

    private fun initView() {
        //This method will use for fetching Token
        /* Thread(Runnable {
             try {
                 myFirebaseToken = FirebaseInstanceId.getInstance().token
                 Log.e("Token fcm", "Token>>$myFirebaseToken")
             } catch (e: IOException) {
                 e.printStackTrace()
             }
         }).start()*/
    }
    fun clicListenerView() {
        binding!!.btnLogin.setOnClickListener(this)
        binding!!.tvSignup.setOnClickListener(this)
        binding!!.tvForgot.setOnClickListener(this)
        binding!!.llGoogleSignIn.setOnClickListener(this)
        binding!!.llFacebookLogin.setOnClickListener(this)
        binding!!.back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
//                if (validation!!.validate()) {
//                    utils.hideKeyboard(binding!!.edtEmail)
                    /* startActivity(Intent(this, HomeActivity::class.java))
                    finish()*/
//                    callloginAPI("1")
//                }

                if (binding?.edtEmail?.text.toString().isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Please enter email/mobile number", Toast.LENGTH_SHORT)
                        .show()
                }else if (binding?.edtPassword?.text.toString().isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Please enter password", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    callloginAPI("1")
                }
            }
            R.id.tvSignup -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            R.id.back -> {
                startActivity(Intent(this, LanguageActivity::class.java))
            }
            R.id.tvForgot -> {
                startActivity(Intent(this, ForgotPasswordActivity::class.java).putExtra("value", 0))
            }
            R.id.llGoogleSignIn -> {
                val signInIntent = mGoogleSignInClient!!.signInIntent
//                val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
            R.id.llFacebookLogin -> {
                    LoginManager.getInstance().logInWithReadPermissions(
                        this,
                        Arrays.asList("public_profile", "email")
                    )
            }
            else -> {
            }
        }
    }

    private fun initializeValidation() {
        validation = AwesomeValidation(ValidationStyle.BASIC)
        validation!!.addValidation(
            binding!!.edtEmail,
            Constants.EMPTY_REGEX,
            getString(R.string.please_enter_your_email_and_mobile)
        )
        validation!!.addValidation(
            binding!!.edtPassword,
            Constants.PASSWORD_REGEX,
            getString(R.string.please_enter_password)
        )
    }

    private fun callloginAPI(mType: String) {
        val jsonObject = JsonObject()

        jsonObject.addProperty("username", binding?.edtEmail?.text.toString())
        jsonObject.addProperty("password", binding?.edtPassword?.text.toString())
        jsonObject.addProperty("device_token", userPref.getFcmToken().toString())
        jsonObject.addProperty("type", mType)
        jsonObject.addProperty("device_name", "Android")
        jsonObject.addProperty("device_type", "Android")
        jsonObject.addProperty("country_code", countryCode)
        jsonObject.addProperty("lang_type", "English")
        jsonObject.addProperty("country", countryName)
        jsonObject.addProperty("device_id", "1")

        apiService.callloginAPI(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)

            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    userPref.isLogin = true
                    userPref.user = commonResponse.mdata.user
                    userPref.setToken(commonResponse.mdata.token)
                    userPref.setLoginType("1")
                    userPref.setSubUserId(commonResponse.mdata.user.id)
                    userPref.setSubUserName(commonResponse.mdata.user.name)
                    if (commonResponse.mdata.select_cat == "0") {
                        startActivity(Intent(this, InterestActivity::class.java))
                        finish()
                    } else {
                        userPref.setGenere(true)
                        startActivity(Intent(this, VideoSplashActivity::class.java))
                        finish()
                    }
                } else {
                    if (commonResponse.is_subscribed==1) {
                        utils.simpleAlert(
                            this,
                            resources.getString(R.string.alert),
                            resources.getString(R.string.limiteexcedd)
                        )
                    } else if (commonResponse.is_subscribed==2) {
                        utils.simpleAlert(
                            this,
                            resources.getString(R.string.alert),
                            resources.getString(R.string.limiteexcedd1)
                        )
                    } else {
                        utils.simpleAlert(
                            this,
                            resources.getString(R.string.alert),
                            commonResponse.message.toString()
                        )
                    }
                    hideProgressDialog()
                }
            }, { throwable ->
                hideProgressDialog()
                if (throwable is ConnectException) {
                    utils.simpleAlert(
                        this,
                        resources.getString(R.string.alert),
                        getString(R.string.check_network_connection)
                    )
                } else {

                    utils.simpleAlert(
                        this,
                        resources.getString(R.string.alert),
                        throwable.message.toString()
                    )

                }

            })
            }

    private fun initGoogleSignIn() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.server_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, { connectionResult ->
                Log.d("GoogleSignIn", "onConnectionFailed: $connectionResult")
                Toast.makeText(this, "Google Play Services error", Toast.LENGTH_SHORT).show()
            })
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun facebookLogin() {
        loggedOut = AccessToken.getCurrentAccessToken()
        callbackManager = CallbackManager.Factory.create()
        binding!!.loginButton.setPermissions(listOf(EMAIL).toString())

        binding!!.loginButton.registerCallback(
            callbackManager!!,
            object : FacebookCallback<LoginResult> {
                //                override fun onSuccess(result: LoginResult?) {
//                    val loggedIn = AccessToken.getCurrentAccessToken()
//                    Log.d("API123", "$loggedIn ??")
//                    getUserProfile(loggedIn)
//                }
//
//                override fun onCancel() {
//                    Log.d("API123", "Cancel")
//                }
//
//                override fun onError(error: FacebookException?) {
//                    Log.d("API123", "Cancel--"+error)
//                }
                override fun onCancel() {
                    Log.d("API123", "Cancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("API123", "Cancel--"+error)
                }

                override fun onSuccess(result: LoginResult) {
                    val loggedIn = AccessToken.getCurrentAccessToken()
                    Log.d("API123", "$loggedIn ??")
                    if (loggedIn != null) {
                        getUserProfile(loggedIn)
                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
//        if (userPref.user.id != null){
//            if(account!=null) {
//                val intent = Intent(this@LoginActivity, HomeActivity3::class.java)
//                startActivity(intent)
//            }
//        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
//            val task =
//                GoogleSignIn.getSignedInAccountFromIntent(data)

            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                userName = account!!.displayName
                email = account.email
                socialAccountId = account.id
//                val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
// Using Locale to get the country name
//                val locale = Locale.getDefault()
                userPref.setuserImg(account.photoUrl.toString())
                userPref.setsubuserImg(account.photoUrl.toString())
                callSignUpAPI("2")
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
    fun getCountryCode(context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(0.0, 0.0, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    return addresses[0].countryCode ?: ""
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getCountryName(context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(0.0, 0.0, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    return addresses[0].countryName ?: ""
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
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
                callSignUpAPI("3")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        request.parameters = parameters
        request.executeAsync()
    }


    private fun callSignUpAPI(mType: String) {
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
        jsonObject.addProperty("device_id", "Android")
        jsonObject.addProperty("device_name", "Android")
        jsonObject.addProperty("device_type", "Android")
        jsonObject.addProperty("device_token", userPref.getFcmToken())
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
                    userPref.setsubuserImg(commonResponse.mdata.user.profile_image)
                    userPref.setNotification(true)
                    if (commonResponse.mdata.select_cat == "0") {
                        userPref.setGenere(false)
                    } else {
                        userPref.setGenere(true)
                    }
                    startActivity(Intent(this, VideoSplashActivity::class.java))
                    //startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else if (commonResponse.status == 0 && commonResponse.mdata != null) {
                    userPref.isLogin = true
                    userPref.user = commonResponse.mdata.user
                    userPref.setToken(commonResponse.mdata.token)
                    if (commonResponse.message == "already exist!!") {
//                        open_already_exit()
                        startActivity(Intent(this, WatchTypeActivity::class.java))
                    } else {
                        startActivity(Intent(this, InterestActivity::class.java))
                    }
//                    finish()
                }
//                else if (commonResponse.status == 0 && commonResponse.mdata != null) {
//                    userPref.isLogin = true
//                    userPref.user = commonResponse.mdata.user
//                    userPref.setToken(commonResponse.mdata.token)
//                    startActivity(Intent(this, InterestActivity::class.java))
//                    finish()
//                }
                else {
                    if (mType == "2") {
                        googleSignOut()
                    } else if (mType == "3") {
                        facebookLogout()
                    }
                    utils.simpleAlert(
                        this,
                        resources.getString(R.string.alert),
                        commonResponse.message.toString()
                    )
                    hideProgressDialog()
                }

            }, { throwable ->
                hideProgressDialog()
                if (throwable is ConnectException) {
                    utils.simpleAlert(
                        this,
                        resources.getString(R.string.alert),
                        getString(R.string.check_network_connection)
                    )

                } else {
                    utils.simpleAlert(
                        this,
                        resources.getString(R.string.alert),
                        throwable.message.toString()
                    )
                }
            })

    }

    private fun googleSignOut() {
        mGoogleSignInClient!!.signOut().addOnCompleteListener(
            this
        ) { }

    }

    private fun facebookLogout() {
        LoginManager.getInstance().logOut()
    }

    private fun open_already_exit() {
        // Inflate the dialog's layout
        val dialogBindingLogin = LayoutInflater.from(this).inflate(R.layout.already_exit_popup, null)

        // Set the content view for the BottomSheetDialog
        bottomSheet_already_exit_login.setContentView(dialogBindingLogin)

        // Customize the BottomSheetDialog appearance
        bottomSheet_already_exit_login.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheetInternal: FrameLayout =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            bottomSheetInternal.setBackgroundResource(R.drawable.rounded_top_corners)
        }

        // Make the dialog not cancelable
        bottomSheet_already_exit_login.setCancelable(false)

        // Initialize the buttons
        val tvOk: TextView = dialogBindingLogin.findViewById(R.id.tv_ok)
        val tvCancel: TextView = dialogBindingLogin.findViewById(R.id.tv_cancel)

        // Set click listener for the OK button
        tvOk.setOnClickListener {
            startActivity(Intent(this, WatchTypeActivity::class.java))
            bottomSheet_already_exit_login.dismiss()
        }

        // Set click listener for the Cancel button
        tvCancel.setOnClickListener {
            bottomSheet_already_exit_login.dismiss()
        }

        // Show the BottomSheetDialog
        bottomSheet_already_exit_login.show()
    }



}
