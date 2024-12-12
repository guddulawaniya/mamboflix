package com.mamboflix.ui.activity.initial

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityVideoSplaceBinding
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException


class VideoSplashActivity : BaseActivity() , View.OnClickListener{
    private var binding: ActivityVideoSplaceBinding?=null
    private var token: String? = null
    var START_MILLI_SECONDS = 60000L
    lateinit var countdown_timer: CountDownTimer
    var isRunning: Boolean = false
    var time_in_milli_seconds = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_splace)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        time_in_milli_seconds = 3006L
        if (userPref.getBannerImg()!=null && userPref.getBannerImg()!=""){
            binding!!.ivSplash.visibility=GONE
            binding!!.ivSplash1.visibility= VISIBLE
            startTimer(time_in_milli_seconds)
            Glide.with(this)
                .load(userPref.getBannerImg())
                .skipMemoryCache(true)
                .apply(RequestOptions.placeholderOf(R.mipmap.splash))
                .apply(RequestOptions.errorOf(R.mipmap.splash))
                .into(binding!!.ivSplash1)
        }else{
            callBannerImageAPI()
        }
        clicListenerView()
        //startTimer(time_in_milli_seconds)
        //getDeviceToken()

    }

    fun clicListenerView() {
        binding!!.tvSkip.setOnClickListener(this)
    }

    fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result

            // Log
            Log.e("okok App class", "getInstanceId token$token")

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvSkip -> {

                /*if (userPref.isLogin) {
          startActivity(Intent(this, HomeActivity::class.java))
      } else {
          startActivity(Intent(this, LanguageActivity::class.java))
      }
                finish()*/
            }

            else -> {
            }
        }
    }


    private fun updateTextUI() {
        /*val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60*/

        val seconds = time_in_milli_seconds / 1000
        binding!!.tvSkip.text = "$seconds sec"
    }


    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
                //loadConfeti()
                if (isRunning) {
                    pauseTimer()
                }
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
               // Log.e("time","<<in long>> $time_in_milli_seconds  <<Sec value >> ${time_in_milli_seconds / 1000}")
                updateTextUI()
            }
        }
        countdown_timer.start()

        isRunning = true
        //button.text = "Pause"
        //reset.visibility = View.INVISIBLE

    }

    private fun pauseTimer() {
        //button.text = "Start"
        countdown_timer.cancel()
        isRunning = false
        //binding!!.tvSkip.text = "Skip"
        if (userPref.isLogin ) {
            startActivity(Intent(this, SubUserActivity::class.java))
        } else {
            startActivity(Intent(this, LanguageActivity::class.java))
        }
        finish()
    }

   /* Glide.with(context)
    .load("file:///"+model.absolutePath)
    .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
    .apply(RequestOptions.errorOf(R.drawable.image_loading))
    .into(holder.binding.ivUser)*/

   private fun callBannerImageAPI() {
       apiService.callBannerImageAPI(userPref.getFcmToken().toString(),
           userPref.getPreferredLanguage())
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .doOnSubscribe(this::showProgressDialog)
           .doOnCompleted(this::hideProgressDialog)
           .subscribe({ commonResponse ->
               if (commonResponse.status != 0 && commonResponse.mdata != null) {
                   startTimer(time_in_milli_seconds)
                   binding!!.ivSplash.visibility=GONE
                   binding!!.ivSplash1.visibility= VISIBLE
                   userPref.setBannerImg(commonResponse.mdata.image_name)

                   Glide.with(this)
                       .load(commonResponse.mdata.image_name)
                       .skipMemoryCache(true)
                       .apply(RequestOptions.placeholderOf(R.mipmap.splash))
                       .apply(RequestOptions.errorOf(R.mipmap.splash))
                       .into(binding!!.ivSplash1)
               } else {
                   startTimer(time_in_milli_seconds)
                   utils.simpleAlert(
                       this,
                       "Error",
                       commonResponse.message.toString()
                   )
                   hideProgressDialog()
               }

           }, { throwable ->
               startTimer(time_in_milli_seconds)
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
   }
}
