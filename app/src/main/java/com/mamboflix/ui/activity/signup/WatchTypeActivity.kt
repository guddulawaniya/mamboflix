package com.mamboflix.ui.activity.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.mamboflix.BaseActivity
import com.mamboflix.HomeActivity3.HomeActivity3
import com.mamboflix.R
import com.mamboflix.databinding.ActivityWatchTypeBinding


class WatchTypeActivity : BaseActivity() , View.OnClickListener{
    private var binding: ActivityWatchTypeBinding?=null
    private var validation: AwesomeValidation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_watch_type)

        clicListenerView()
        initView()
        //myFirebaseToken = FirebaseInstanceId.getInstance().token
        //Log.e("Token fcm", "Token>>$myFirebaseToken")
        binding!!.tvName.text=userPref.user.name

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


    fun clicListenerView(){
        binding!!.lytProfile.setOnClickListener(this)
        binding!!.lytUser.setOnClickListener(this)


    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.lytProfile -> {

                startActivity(Intent(this, AddProfileActivity::class.java)
                        .putExtra("enterType",1))
                finish()

            }

            R.id.lytUser -> {
                userPref.setSubUserId(userPref.user.id)
                userPref.setSubUserName(userPref.user.name)
                startActivity(Intent(this, HomeActivity3::class.java))
                finish()
            }



            else -> {
            }

        }
    }




   /* private fun callloginAPI() {

        val jsonObject = JsonObject()
        jsonObject.addProperty("email", binding?.edtEmail?.text.toString())
        jsonObject.addProperty("password", binding?.edtPassword?.text.toString())
        jsonObject.addProperty("device_id", myFirebaseToken)
        jsonObject.addProperty("device_type", "Android")

        apiService.callloginAPI(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)


            .subscribe({ commonResponse ->

                if (commonResponse.status !=0 && commonResponse.mdata != null) {

                    userPref.isLogin= true
                    userPref.user = commonResponse.mdata
                    startActivity(Intent(this, HomeActivity::class.java))
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
                        throwable.message.toString())

                }

            })
    }*/






}
