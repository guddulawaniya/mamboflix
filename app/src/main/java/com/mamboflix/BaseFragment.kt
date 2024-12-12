package com.mamboflix

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mamboflix.api.ApiService
import com.mamboflix.prefs.UserPref
import com.mamboflix.utils.Utils
import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection
 open class BaseFragment : Fragment() {
     @Inject lateinit var  utils: Utils
     @Inject lateinit var  userPref: UserPref
     @Inject lateinit var apiService: ApiService
    var progressDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    protected fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        //fragmentTransaction.setCustomAnimations( R.anim.pop_enter, R.anim.pop_exit,R.anim.enter, R.anim.exit)
       // fragmentTransaction.replace(R.id.frameContainer, fragment, fragment.javaClass.name).commit()
    }

    fun showProgressDialog() {
        if (progressDialog == null)
            progressDialog = Dialog(requireContext())
        progressDialog!!.setContentView(R.layout.progress_dialog)
        progressDialog!!.setCancelable(false)
        progressDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        if (progressDialog != null && !progressDialog!!.isShowing)
            progressDialog!!.show()
    }

    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null && progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }

    override fun onPause() {
        super.onPause()
    }
}
