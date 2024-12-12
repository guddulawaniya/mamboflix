package com.mamboflix.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityContacusBinding
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException


class ContacUsActivity : BaseActivity(), View.OnClickListener {
    lateinit var bottam_sheet_send_message_query: BottomSheetDialog
    private lateinit var binding: ActivityContacusBinding
    private var datePickerDialog: DatePickerDialog? = null
    private var validation: AwesomeValidation? = null
    var year1 :String?=null
    var month1 :String?=null
    var day1:String?=null
    var number:String?=null
    private var isVisible2 = false
    private var isVisible1 = false
    val FACEBOOK_PAGE_ID = "approids"
    val FACEBOOK_URL = "https://www.facebook.com/approids"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= DataBindingUtil.setContentView(this, R.layout.activity_contacus)
        setToolBar()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    private fun setToolBar(){
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Contact Us"
        binding.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        binding!!.whatsaap.setOnClickListener(this)
        binding!!.facebook.setOnClickListener(this)
        callGetContentApi()
    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.whatsaap -> {
              openWhatsApp()
            }
            R.id.facebook -> {
                val facebookIntent = Intent(Intent.ACTION_VIEW)
                val facebookUrl: String? = getFacebookPageURL(this@ContacUsActivity)
                facebookIntent.data = Uri.parse(facebookUrl)
                startActivity(facebookIntent)
                getOpenFacebookIntent()
            }
        }
    }
    fun getFacebookPageURL(context: Context): String? {
        val packageManager: PackageManager = context.getPackageManager()
        return try {
            val versionCode =
                packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            val activated =
                packageManager.getApplicationInfo("com.facebook.katana", 0).enabled
            if (activated) {
                if (versionCode >= 3002850) {
                    Log.d("main", "fb first url")
                    "fb://facewebmodal/f?href=$FACEBOOK_URL"
                } else {
                    "fb://page/$FACEBOOK_PAGE_ID"
                }
            } else {
                FACEBOOK_URL
            }
        } catch (e: PackageManager.NameNotFoundException) {
            FACEBOOK_URL
        }
    }
    @SuppressLint("InflateParams")
    private fun openWhatsApp(){
        val dialogBinding = LayoutInflater
            .from(this@ContacUsActivity).inflate(R.layout.send_popup, null)
        bottam_sheet_send_message_query = BottomSheetDialog(this@ContacUsActivity)
        bottam_sheet_send_message_query.setContentView(dialogBinding)
        bottam_sheet_send_message_query.setOnShowListener { dia ->
            val bottomSheetDialog = dia as BottomSheetDialog
            val bottomSheetInternal: FrameLayout =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            bottomSheetInternal.setBackgroundResource(R.drawable.rounded_top_corners)
        }
        bottam_sheet_send_message_query.setCancelable(true)

        val et_send_message =
            bottam_sheet_send_message_query.findViewById<EditText>(R.id.et_send_message)
        val btn_send =
            bottam_sheet_send_message_query.findViewById<AppCompatButton>(R.id.btn_send)

        btn_send!!.setOnClickListener() {

            if (et_send_message!!.text.toString().isEmpty()) {
                Toast.makeText(this@ContacUsActivity, "Please enter message", Toast.LENGTH_SHORT)
                    .show()

            } else {

//                val packageManager: PackageManager = getPackageManager()
//                val i = Intent(Intent.ACTION_VIEW)
//                val uri =
//                    "https://api.whatsapp.com/send?phone=" + "+918349000183" + "&text=" + URLEncoder.encode(
//                        et_send_message.text.toString()
//                    )
//                i.setPackage("com.whatsapp")
//                i.data = Uri.parse(uri)
//
//                if (i.resolveActivity(packageManager) != null) {
//                    startActivity(i)
//                }

                val phoneNumberWithCountryCode = binding.tvCon.text.toString()
                val message = et_send_message.text.toString()

                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            String.format(
                                "https://api.whatsapp.com/send?phone=%s&text=%s",
                                phoneNumberWithCountryCode,
                                message
                            )
                        )
                    )
                )
                bottam_sheet_send_message_query.dismiss()
            }
        }
        bottam_sheet_send_message_query.show()

//        val phoneNumber = binding!!.tvContactNumber.text.toString()
//        val url = "https://api.whatsapp.com/send?phone=$number"
//        try {
//            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
//            val i = Intent(Intent.ACTION_VIEW)
//            i.data = Uri.parse(url)
//            startActivity(i)
//        } catch (e: PackageManager.NameNotFoundException) {
//            Toast.makeText(this, "Whatsapp is not installed in your phone.", Toast.LENGTH_SHORT).show()
//            e.printStackTrace()
//        }
    }

    fun getOpenFacebookIntent() {

      /*  val facebookId = "fb://page/<Facebook Page ID>"
        val urlPage = "http://www.facebook.com/mypage"

        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)))
        } catch (e: Exception) {
         //   Log.e(FragimentActivty.TAG, "Application not intalled.")
            //Open url web page.
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)))
        }
*/

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun callGetContentApi() {
        apiService.callGetContentApi("Bearer "+userPref.getToken(),"1",
            userPref.getFcmToken().toString(),
        userPref.getPreferredLanguage())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->

                if (commonResponse.status !=0 && commonResponse.mdata != null) {
                    binding.tvContactNumber.text = Html.fromHtml(commonResponse.mdata.content)
                    binding.tvMailId.text = Html.fromHtml(commonResponse.mdata.email)
                    binding.tvAddress.text = Html.fromHtml(commonResponse.mdata.address)
                    binding.tvContactNumber.setOnClickListener {
                        val dialIntent = Intent(Intent.ACTION_DIAL)
                        dialIntent.data = Uri.parse("tel:${commonResponse.mdata.content.trim()}")
                        startActivity(dialIntent)

                    }
                    binding.tvMailId.setOnClickListener {
                        val emailIntent = Intent(
                            Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", commonResponse.mdata.email.trim(), null
                            )
                        )
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body")
                        startActivity(Intent.createChooser(emailIntent, "Send email..."))
                    }
//                    val arr: List<String> =  binding.tvCon.text.toString().split(",")
//                    binding.tvCon1.text=  Html.fromHtml(arr[0])
//                    val arr1: List<String> =  binding.tvCon1.text.toString().split(":")
//                    number=arr1[1]
//                    binding.tvCon2.text=  Html.fromHtml(arr[1])
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
    }

}
