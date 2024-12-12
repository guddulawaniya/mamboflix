package com.mamboflix.ui.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.google.gson.JsonObject
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityLoginBinding
import com.mamboflix.ui.activity.forgotpassword.ForgotPasswordActivity
import com.mamboflix.ui.activity.initial.VideoSplashActivity
import com.mamboflix.ui.activity.signup.InterestActivity
import com.mamboflix.ui.activity.signup.SignUpActivity
import com.mamboflix.utils.Constants
import org.json.JSONException
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException


class BottomSheetDialog : BaseActivity(), View.OnClickListener {
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
    private var socialAccountId: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initializeValidation()
        clicListenerView()
        initView()
        //myFirebaseToken = FirebaseInstanceId.getInstance().token
        //Log.e("Token fcm", "Token>>$myFirebaseToken")
        initGoogleSignIn()
        facebookLogin()
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
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnLogin -> {
                if (validation!!.validate()) {
                    utils.hideKeyboard(binding!!.edtEmail)
                    /* startActivity(Intent(this, HomeActivity::class.java))
                    finish()*/
                    callloginAPI("1")
                }


            }

            R.id.tvSignup -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }

            R.id.tvForgot -> {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))

            }

            R.id.llGoogleSignIn -> {
                val signInIntent = mGoogleSignInClient!!.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }

            R.id.llFacebookLogin -> {


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
            "Please enter the valid Email."
        )
        validation!!.addValidation(
            binding!!.edtPassword,
            Constants.PASSWORD_REGEX,
            "Please enter the valid Password."
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
        jsonObject.addProperty("device_id", "Android")

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
                    userPref.setSubUserId("7")
                    userPref.setLoginType("1")
                    userPref.setSubUserId(commonResponse.mdata.user.id)
                    userPref.setSubUserName(commonResponse.mdata.user.name)
                    startActivity(Intent(this, VideoSplashActivity::class.java))
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

//        binding!!.loginButton.registerCallback(
//            callbackManager,
//            object : FacebookCallback<LoginResult> {
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
//                    Log.d("API123", "Cancel")
//                }
//
//            })


        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // App code
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }
            })

    }


    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)

/*
        if(account!=null) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
        }
*/
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager!!.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                userName = account!!.displayName
                email = account.email
                socialAccountId = account.id
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
                    startActivity(Intent(this, VideoSplashActivity::class.java))
                    //startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else if (commonResponse.status == 0 && commonResponse.mdata != null) {
                    userPref.isLogin = true
                    userPref.user = commonResponse.mdata.user
                    userPref.setToken(commonResponse.mdata.token)
                    startActivity(Intent(this, InterestActivity::class.java))
                    //startActivity(Intent(this, HomeActivity::class.java))
                    finish()
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

    private fun googleSignOut() {
        mGoogleSignInClient!!.signOut().addOnCompleteListener(
            this
        ) { }

    }

    private fun facebookLogout() {
        LoginManager.getInstance().logOut()
    }

}
