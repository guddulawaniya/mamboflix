package com.mamboflix.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.firebase.iid.FirebaseInstanceId
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivitySplaceBinding
import com.mamboflix.ui.activity.initial.LanguageActivity
import com.mamboflix.ui.activity.initial.VideoSplashActivity
import com.mamboflix.ui.activity.login.LoginActivity
import com.mamboflix.utils.PreferenceManager
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SplaceScreen : BaseActivity() {
//https://mega.nz/folder/duR1zIrL#-2cu-0Gsxs82xPakCu1LOg

    private val SPLASH_TIMEOUT: Long = 3000 //3sec
    lateinit var binding: ActivitySplaceBinding
    private var myFirebaseToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splace)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splace)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        myFirebaseToken = FirebaseInstanceId.getInstance().token
        Log.e("Token fcm", "Token>>$myFirebaseToken")
        userPref.setFcmToken(myFirebaseToken)

        try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        }
        catch (e: PackageManager.NameNotFoundException) {
            Log.e("name not found", e.toString())
        }
        catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        }

        callBannerImageAPI()

        /* Handler().postDelayed({
             startActivity(Intent(this, VideoSplashActivity::class.java))
             *//*if (userPref.isLogin) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }*//*

            finish()

        },SPLASH_TIMEOUT)*/
    }
    private fun mySplash() {
        Handler().postDelayed({

            val preferenceManager = PreferenceManager(this)

            if (preferenceManager.isFirstLaunch()) {
                // This is the first launch, launch the desired activity
                startActivity(Intent(this, LanguageActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }else {
                if (userPref.isLogin) {
                    startActivity(Intent(this, VideoSplashActivity::class.java))
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }

            finish()

        }, SPLASH_TIMEOUT)
    }


    private fun callBannerImageAPI() {
        apiService.callBannerImageAPI(
            userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*.doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)*/
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    userPref.setBannerImg(commonResponse.mdata.image_name)
                    mySplash()
                } else {
                    mySplash()
                    /* utils.simpleAlert(
                         this,
                         "Error",
                         commonResponse.message.toString()
                     )
                     hideProgressDialog()*/
                }

            }, { throwable ->

                if (throwable is ConnectException) {
                    /* utils.simpleAlert(
                         this,
                         "Error",
                         getString(R.string.check_network_connection)
                     )*/
                    mySplash()

                } else {
                    mySplash()

                    /* utils.simpleAlert(
                         this,
                         "Error",
                         getString(R.string.check_network_connection)
                     )*/

                }

            })
    }


}