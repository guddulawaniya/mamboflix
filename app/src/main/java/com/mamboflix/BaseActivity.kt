package com.mamboflix

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mamboflix.api.ApiService
import com.mamboflix.prefs.UserPref
import com.mamboflix.utils.LocaleHelper
import com.mamboflix.utils.Utils
import dagger.android.AndroidInjection
import javax.inject.Inject


open class  BaseActivity : AppCompatActivity() {

    @Inject lateinit var  utils: Utils
    @Inject lateinit var  userPref: UserPref
    @Inject
    open lateinit var apiService: ApiService

    var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        getDeviceToken()

    }



    protected fun replaceFragment(fragment: Fragment) {
        //val fragmentTransaction = supportFragmentManager.beginTransaction()
        /*fragmentTransaction.replace(R.id.frameContainer, fragment, fragment.javaClass.name).commit()*/
    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        /*val manager = supportFragmentManager
        val ft = manager.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        ft.replace(R.id.frameContainer, fragment, tag)
        ft.commitAllowingStateLoss()*/
    }




    /*protected void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }*/



    protected fun showProgressDialog() {
        if (progressDialog == null)
            progressDialog = Dialog(this)
        progressDialog!!.setContentView(R.layout.progress_dialog)
        progressDialog!!.setCancelable(false)
        progressDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        if (progressDialog != null && !progressDialog!!.isShowing)
            progressDialog!!.show()
    }

    protected fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null && progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }


    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            if (userPref.getFcmToken()==null){
                var token = task.result
                userPref.setFcmToken(token)
            }
            Log.e("okok base class", "getInstanceId token${userPref.getFcmToken()}")
            // Log
            //
        })
    }

    override fun attachBaseContext(newBase: Context) {
        var localeToSwitchTo = "en"
        localeToSwitchTo = if( UserPref(newBase).getPreferredLanguage()=="Swahili"){
            "sw"
        } else{
            "en"
        }
        /*  val localeToSwitchTo = UserPref(newBase).getPreferredLanguage()*/
        val localeUpdatedContext = LocaleHelper.setLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}
