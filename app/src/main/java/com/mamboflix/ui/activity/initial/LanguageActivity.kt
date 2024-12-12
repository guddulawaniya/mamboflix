package com.mamboflix.ui.activity.initial

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityLanguageBinding
import com.mamboflix.ui.activity.login.LoginActivity


class LanguageActivity : BaseActivity() , View.OnClickListener{
    private var binding: ActivityLanguageBinding?=null
    private var validation: AwesomeValidation? = null
    private var myFirebaseToken: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language)
        initializeValidation()
        clicListenerView()
        initView()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        //myFirebaseToken = FirebaseInstanceId.getInstance().token
        //Log.e("Token fcm", "Token>>$myFirebaseToken")
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

        if(userPref.getPreferredLanguage()=="Swahili"){
            binding!!.ivEnglish.setImageResource(R.drawable.uncheck)
            binding!!.ivKiswahili.setImageResource(R.drawable.check)
        }
        else{
            binding!!.ivEnglish.setImageResource(R.drawable.check)
            binding!!.ivKiswahili.setImageResource(R.drawable.uncheck)
        }
    }


    fun clicListenerView(){
        binding!!.btnNext.setOnClickListener(this)
        binding!!.lytEnglish.setOnClickListener(this)
        binding!!.lytKiswahili.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnNext -> {
                /*if (validation!!.validate()) {
                    utils.hideKeyboard(binding!!.edtEmail)
                    //callloginAPI()
                }*/
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)

            }

            R.id.lytEnglish -> {
               binding!!.ivEnglish.setImageResource(R.drawable.check)
                userPref.setPreferredLanguage("English")
                binding!!.ivKiswahili.setImageResource(R.drawable.uncheck)
                //restartActivity()
            }

            R.id.lytKiswahili -> {
                binding!!.ivEnglish.setImageResource(R.drawable.uncheck)
                binding!!.ivKiswahili.setImageResource(R.drawable.check)
                userPref.setPreferredLanguage("Swahili")
               // restartActivity()
            }

            else -> {
            }

        }
    }

    private fun initializeValidation() {
       /* validation = AwesomeValidation(ValidationStyle.BASIC)
        validation!!.addValidation(
            binding!!.edtEmail,
            Constants.EMPTY_REGEX,
            "Please enter the valid Email."
        )
        validation!!.addValidation(
            binding!!.edtPassword,
            Constants.PASSWORD_REGEX,
            "Please enter the valid Password."
        )*/
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



    private fun restartActivity() {
       /* val intent = intent
        finish()
        startActivity(intent)*/
        this.recreate()
    }


}
