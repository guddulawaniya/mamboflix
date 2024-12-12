package com.mamboflix.ui.activity


import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.*


@Suppress("UNREACHABLE_CODE")
class HelpActivity : BaseActivity(), View.OnClickListener {


    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


    lateinit var binding: ActivityHelpBinding

    //private var myListAdapter: PaymentBillingAdapter? = null
    private var title: String? = ""
    private var languagePopUpBinding: LayoutPreferredLanguageDialogBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setToolBar()
        binding.btSubmit.setOnClickListener(this)
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
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //title = "Settings"
        binding.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        setTitle("Help")

    }


    override fun onResume() {
        super.onResume()

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btSubmit -> {

                if (binding.etEmail.text.toString().isEmpty()) {
                    Toast.makeText(applicationContext, "Please enter Subject", Toast.LENGTH_SHORT)
                        .show()
                } else if (binding.etMsg.text.toString().isEmpty()){
                    Toast.makeText(applicationContext, "Please enter Message", Toast.LENGTH_SHORT)
                        .show()
                }else {
                    callHeldAPI(
                        binding.etEmail.text.trim().toString(),
                        binding.etEmail.text.trim().toString(),
                        binding.etMsg.text.toString()
                    )
                }

            }


        }
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


    private fun callHeldAPI(email_Id: String, subject: String, msg: String) {
        apiService.callHeldAPI(
            "Bearer " + userPref.getToken()!!,
            userPref.getFcmToken()!!,
            email_Id,
            msg,
            msg
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
                    binding.etEmail.setText("")
                    binding.etMsg.setText("")
                    //   utils.simpleAlert(this@SettingsActivity, "Success", commonResponse.message!!)
                    Toast.makeText(this, "" + commonResponse.message, Toast.LENGTH_LONG).show()
                    onBackPressed()
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

}

