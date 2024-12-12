package com.mamboflix.utils


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.os.Handler
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.mamboflix.R
import com.mamboflix.databinding.DialogMsgBinding
import com.mamboflix.ui.activity.login.LoginActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton


@Singleton
class Utils(private val context: Context) {
    private  val SPLASH_TIMEOUT :Long=30000 //30sec
    var progressDialog: Dialog? = null
    fun toaster(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun logger(message: String) {
        Log.e("Win-Millionaire-Log", message)
    }

    fun simpleAlert(context: Context, title: String, message: String) {

       var cDialog = Dialog(context,R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogMsgBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_msg,
                null,
                false
        )
        cDialog!!.setContentView(bindingDialog.root)
        cDialog!!.setCancelable(false)
        cDialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        //}

        cDialog!!.show()
        bindingDialog.tvTitle.text = title
        bindingDialog.tvMsg.text = message

        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
        }
       /* val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_show, null)

        val builder = AlertDialog.Builder(context)
        builder.setView(mDialogView)
        //builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Close", null)
        builder.create()
        builder.show()*/
    }
 fun simpleAlert1(context: Context, title: String, message: String, i: Int) {

       var cDialog = Dialog(context,R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogMsgBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_msg,
                null,
                false
        )
        cDialog!!.setContentView(bindingDialog.root)
        cDialog!!.setCancelable(false)
        cDialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        //}

        cDialog!!.show()
        bindingDialog.tvTitle.text = title
        bindingDialog.tvMsg.text = message
        bindingDialog.btCancel.text = context.resources.getString(R.string.okay)

        bindingDialog.btCancel.setOnClickListener {

            if(i==0){
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
            if(i ==2){
                showProgressDialog()
                Handler().postDelayed({
                    hideProgressDialog()
                },SPLASH_TIMEOUT)
            }
            cDialog!!.dismiss()
        }
       /* val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_show, null)

        val builder = AlertDialog.Builder(context)
        builder.setView(mDialogView)
        //builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Close", null)
        builder.create()
        builder.show()*/
    }


    protected fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }

    fun showPopup(v: View?, layoutResource: View):RelativePopupWindow {
       // val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
     //   val popupView: View = layoutInflater.inflate(layout, null)
        val popupWindow = RelativePopupWindow(
                layoutResource,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow.setBackgroundDrawable(BitmapDrawable())
        popupWindow.isOutsideTouchable = true
        popupWindow.setOnDismissListener(PopupWindow.OnDismissListener {
            //TODO do sth here on dismiss
        })
        popupWindow.showOnAnchor(v!!, RelativePopupWindow.VerticalPosition.ABOVE, RelativePopupWindow.HorizontalPosition.LEFT, false);
        return popupWindow
    }

    fun hideKeyboard(view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun savedDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(date)
    }

    fun showDate(date: Date): String {
        val sdf = SimpleDateFormat("dd-MMM-yyyy")
        return sdf.format(date)
    }

    fun savedDate1(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(date)
    }

    fun showDate1(date: Date): String {
        val sdf = SimpleDateFormat("dd-MMM-yyyy")
        return sdf.format(date)
    }

    fun fromHtml(html: String): Spanned {
        val result: Spanned
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            result = Html.fromHtml(html)
        }
        return result
    }


    /**
     * checking internet connection
     */
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }


    fun getFormattedDate(incomingDate: String): String {
        val fmt = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = null
        var formatedDate = ""
        try {
            date = fmt.parse(incomingDate)
            val fmtOut = SimpleDateFormat("dd-MMM-yyyy")
            formatedDate = fmtOut.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatedDate
    }

    companion object {

        var userType: String? = null
        var serviceID: String? = null
    }

    fun haveWifiNetworkConnection(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.isActiveNetworkMetered
    }
    protected fun showProgressDialog() {
        if (progressDialog == null)
            progressDialog = Dialog(context)
        progressDialog!!.setContentView(R.layout.progress_dialog)
        progressDialog!!.setCancelable(false)
        progressDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        if (progressDialog != null && !progressDialog!!.isShowing)
            progressDialog!!.show()
    }
}