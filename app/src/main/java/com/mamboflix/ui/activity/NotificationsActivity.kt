package com.mamboflix.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mamboflix.BaseActivity
import com.mamboflix.HomeActivity3.HomeActivity3
import com.mamboflix.R
import com.mamboflix.databinding.*
import com.mamboflix.model.NotificationModel
import com.mamboflix.ui.adapter.NotificationsAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class NotificationsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityNotificationBinding
    private var myListAdapter: NotificationsAdapter? = null
    private var listData: ArrayList<NotificationModel>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_notification)
        listData = ArrayList()
        setToolBar()
        binding!!.nsvMain.visibility = View.GONE
//        binding!!.shimmerFrameLayout.visibility = View.VISIBLE
//        binding!!.shimmerFrameLayout.startShimmer()
        userPref.setNotificationdot(false)
        callNotificationAPI()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        }

    private fun setToolBar(){
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = " "
        binding.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        binding.toolbar.title = resources.getString(R.string.notifications)
        binding.tvTitle.text=resources.getString(R.string.notifications)
        binding!!.btnClearAll.setOnClickListener(this)
        setRecyclerview()
        //callGetContentApi()
     }

    private fun setRecyclerview() {
        myListAdapter = NotificationsAdapter(this,listData!!)
        val linearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding!!.rvMyList.layoutManager = linearLayoutManager
        binding!!.rvMyList.adapter = myListAdapter
        myListAdapter!!.setOnItemClickListener(object : NotificationsAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(position: Int, view: View) {
            }
        })
    }

    override fun onClick(view: View?) {
        //btn_clear_all

        when (view?.id) {
            R.id.btn_clear_all -> {
                callClearNotificationAPI()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
            startActivity(Intent(this, HomeActivity3::class.java))
               // finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun callNotificationAPI() {
        listData!!.clear()
        apiService.callNotificationAPI("Bearer "+userPref.getToken(),userPref.user.id,userPref.getFcmToken().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status !=0 && commonResponse.mdata != null) {
                    binding.btnClearAll.visibility = View.VISIBLE
                    binding!!.nsvMain.visibility = View.VISIBLE
//                    binding!!.shimmerFrameLayout.stopShimmer()
//                    binding!!.shimmerFrameLayout.visibility = View.GONE
                    binding.tvEmptyDownload.visibility=View.GONE
                    binding.ivNotification.visibility=View.GONE
                     listData!!.addAll(commonResponse.mdata)
                   myListAdapter!!.notifyDataSetChanged()
                }
                else
                {
                    confirmationAlert()
                    binding.btnClearAll.visibility = View.GONE
                    binding!!.nsvMain.visibility = View.VISIBLE
//                    binding!!.shimmerFrameLayout.stopShimmer()
//                    binding!!.shimmerFrameLayout.visibility = View.GONE
                    binding.ivNotification.visibility=View.VISIBLE
                    binding.tvEmptyDownload.visibility=View.VISIBLE
                    binding.tvEmptyDownload.text=getString(R.string.no_notification)
                   /* utils.simpleAlert(
                        this,
                        "","No Data"
                    )*/
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

                }else {
                    utils.simpleAlert(
                      this,
                        "Error",
                        throwable.message.toString())
                }
            })
    }

    private fun callClearNotificationAPI() {
        listData!!.clear()
        apiService.callClearNotificationAPI("Bearer "+userPref.getToken(),userPref.user.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status !=0 && commonResponse.mdata != null) {
                    myListAdapter!!.notifyDataSetChanged()
                    binding.btnClearAll.visibility = View.GONE
                    binding.tvEmptyDownload.visibility=View.VISIBLE
                    binding.ivNotification.visibility=View.VISIBLE
                    binding.tvEmptyDownload.text=getString(R.string.no_notification)
                } else {
                    myListAdapter!!.notifyDataSetChanged()
                    binding.btnClearAll.visibility = View.GONE
                    binding.tvEmptyDownload.visibility=View.VISIBLE
                    binding.ivNotification.visibility=View.VISIBLE
                    binding.tvEmptyDownload.text=getString(R.string.no_notification)

                    /* utils.simpleAlert(
                         this,
                         "","No Data"
                     )*/
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

    private fun confirmationAlert() {
        var cDialog = Dialog(this,R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogMsgBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
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

        cDialog!!.show()
         bindingDialog.tvTitle.text = getString(R.string.alert)
        //bindingDialog.tvMsg.visibility=GONE
         bindingDialog.tvMsg.text = getString(R.string.no_notification)


        bindingDialog.btCancel.setOnClickListener {
           startActivity(Intent(applicationContext,HomeActivity3::class.java))
            finishAffinity()
        }

    }
}
