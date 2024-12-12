package com.mamboflix.ui.activity.settings


import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.mamboflix.BaseActivity
import com.mamboflix.HomeActivity3.HomeActivity3
import com.mamboflix.R
import com.mamboflix.databinding.ActivitySettingsBinding
import com.mamboflix.databinding.LayoutPreferredLanguageDialogBinding
import com.mamboflix.ui.activity.MyDownloadActivity
import com.mamboflix.ui.activity.recentView.RecentViewActivity
import com.mamboflix.utils.CommonUtils

import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

@Suppress("UNREACHABLE_CODE")
class SettingsActivity : BaseActivity(), View.OnClickListener {
    private var binding: ActivitySettingsBinding? = null
    //private var myListAdapter: PaymentBillingAdapter? = null
    private var title: String? = ""
    private var languagePopUpBinding: LayoutPreferredLanguageDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setToolBar()
        binding!!.switchNoti.setOnClickListener(this)
        binding!!.switchSubtitle.setOnClickListener(this)
        binding!!.switchWifi.setOnClickListener(this)
        binding!!.lytDevice4.setOnClickListener(this)
        binding!!.lytType.setOnClickListener(this)
        binding!!.lytRecentView.setOnClickListener(this)
        binding!!.lytDevice3.setOnClickListener(this)
        //setRecyclerview()
        if (userPref.user.id == userPref.getSubUserId().toString()){
            binding!!.lytChats.visibility = View.GONE
            binding!!.ViewNew.visibility = View.GONE
        }else{
            binding!!.lytChats.visibility = View.VISIBLE
            binding!!.ViewNew.visibility = View.VISIBLE
        }


        if (userPref.getAdult()) {
            binding!!.chkParentalControl.isChecked = true
        } else {
            binding!!.chkParentalControl.isChecked = false
        }
//        Toast.makeText(this, userPref.getAdult().toString(), Toast.LENGTH_SHORT).show()
        // binding!!.chkParentalControl.isChecked = !userPref.getAdult()
        Log.d("TAG", "onCreate: " + userPref.getNotification())
//        if (userPref.getNotification()==true) {
//            binding!!.switchNoti.isChecked = true
//
//        } else if (userPref.getNotification()==false){
//            binding!!.switchNoti.isChecked = false
//
//        }

//        if (userPref.getNotificationStatus() == "1") {
//
//            binding!!.switchNoti.isChecked = true
//
//        } else if (userPref.getNotificationStatus() == "0") {
//
//
//            binding!!.switchNoti.isChecked = false
//
//        }

        binding!!.switchautoplay.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (binding!!.switchautoplay.isChecked) {
                    userPref.setautoplay(true)
//                    callParentalControlAPI("1")
                } else {
                    userPref.setautoplay(false)
//                    callParentalControlAPI("2")
                }
            }
        })
        binding!!.switchautoplay.isChecked = userPref.getautoplay()


        binding!!.chkParentalControl.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (binding!!.chkParentalControl.isChecked) {
                    userPref.setAdult(true)
                    callParentalControlAPI("1")
                } else {
                    userPref.setAdult(false)
                    callParentalControlAPI("2")
                }
            }
        })
        binding!!.switchSubtitle.isChecked = userPref.getSubtitles()
    }

    /* private fun setRecyclerview() {
         myListAdapter = PaymentBillingAdapter(this)
         val linearLayoutManager =
             LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
         binding!!.rec.layoutManager = linearLayoutManager
         binding!!.rec.adapter = myListAdapter

         myListAdapter!!.setOnItemClickListener(object : PaymentBillingAdapter.OnItemClickListener {
             @SuppressLint("LogNotTimber")
             override fun onItemClick(position: Int, view: View) {

             }
         })
     }*/

    private fun setToolBar() {
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //title = "Settings"
        binding!!.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )

        setTitle("Settings")
        binding!!.switchSubtitle.isChecked = userPref.getSubtitles()
//        binding!!.switchNoti.isChecked = (!userPref.getNotification())
        binding!!.switchWifi.isChecked = userPref.getDownloadWifi()
        if (userPref.getPreferredLanguage() == "Swahili") {
            binding!!.tvLanguage.text = resources.getString(R.string.kiswahili)
        } else {
            binding!!.tvLanguage.text = resources.getString(R.string.english)
        }
    }


    override fun onResume() {
        super.onResume()

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.switchNoti -> {
                if (binding!!.switchNoti.isChecked == true) {
                    userPref.setNotification(false)
                } else if (binding!!.switchNoti.isChecked == false) {
                    userPref.setNotification(true)
                }
            }
            R.id.switchSubtitle -> {
                if (binding!!.switchSubtitle.isChecked) {
                    userPref.setSubtitles(true)
                } else {
                    userPref.setSubtitles(false)
                }
            }  R.id.switchautoplay -> {
                if (binding!!.switchautoplay.isChecked) {
                    userPref.setautoplay(true)
                } else {
                    userPref.setautoplay(false)
                }
            }

            R.id.switchWifi -> {
                if (binding!!.switchWifi.isChecked) {
                    userPref.setDownloadWifi(true)
                } else {
                    userPref.setDownloadWifi(false)
                }
            }

            R.id.lytDevice4 -> {
                setUpLanguageDialog()
            }


            R.id.lytType -> {
                /* if(userPref.getAdult()){
                     userPref.setAdult(false)
                     binding!!.chkParentalControl.isChecked = false
                     callParentalControlAPI("2")
                 }
                 else{
                     userPref.setAdult(true)
                     binding!!.chkParentalControl.isChecked = true
                     callParentalControlAPI("1")
                 }
 */
            }

            R.id.lytDevice3 -> {
                startActivity(Intent(this, MyDownloadActivity::class.java))
            }

            R.id.lytRecentView -> {
                startActivity(Intent(this, RecentViewActivity::class.java))
            }

        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this@SettingsActivity, HomeActivity3::class.java)
                //   intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun callParentalControlAPI(mType: String) {
        apiService.callParentalControlAPI(
            "Bearer " + userPref.getToken()!!,
//            userPref.user.id,
            mType,
            userPref.getSubUserId()!!,
            userPref.getFcmToken()!!,
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)

            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
                    //   utils.simpleAlert(this@SettingsActivity, "Success", commonResponse.message!!)
                    //   Toast.makeText(this,""+commonResponse.message,Toast.LENGTH_LONG).show()
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


    private fun setUpLanguageDialog() {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.layout_preferred_language_dialog, null, false)
        languagePopUpBinding = DataBindingUtil.bind(dialogView)!!
        val dialog = CommonUtils.createCustomDialog(this, languagePopUpBinding!!.root)
        dialog.setCanceledOnTouchOutside(true)
        if (userPref.getPreferredLanguage() == "Swahili") {
            languagePopUpBinding!!.rbLanguage2.isChecked = true
        } else {
            languagePopUpBinding!!.rbLanguage1.isChecked = true
        }
        languagePopUpBinding!!.rbLanguage1.setOnClickListener {
            if (languagePopUpBinding!!.rbLanguage1.isChecked) {
                languagePopUpBinding!!.rbLanguage2.isChecked = false
                userPref.setPreferredLanguage("English")
                binding!!.tvLanguage.text = resources.getString(R.string.english)
                dialog.dismiss()
                // startActivity(intent)
                // attachBaseContext(BaseActivity())
                restartActivity()
            }
        }

        languagePopUpBinding!!.rbLanguage2.setOnClickListener {
            if (languagePopUpBinding!!.rbLanguage2.isChecked) {
                languagePopUpBinding!!.rbLanguage1.isChecked = false
                userPref.setPreferredLanguage("Swahili")
                binding!!.tvLanguage.text = resources.getString(R.string.kiswahili)
                dialog.dismiss()
                //startActivity(intent)
                restartActivity()
                //attachBaseContext(BaseActivity())
            }
        }
        dialog.show()
    }

    private fun restartActivity() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private fun restartLanguageActivity() {
        val intent = Intent(this@SettingsActivity, HomeActivity3::class.java)
        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        restartLanguageActivity()
    }
}

