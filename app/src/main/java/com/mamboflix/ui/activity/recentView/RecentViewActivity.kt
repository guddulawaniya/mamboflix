package com.mamboflix.ui.activity.recentView

import android.app.Dialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityRecentViewBinding
import com.mamboflix.databinding.DialogMsgBinding
import com.mamboflix.model.recentView.RecentViewModel
import com.mamboflix.ui.activity.contentdetails.ContentDetailsActivity
import com.mamboflix.ui.adapter.RecentViewAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class RecentViewActivity : BaseActivity() {
    private lateinit var binding:ActivityRecentViewBinding
    private var recentViewList:ArrayList<RecentViewModel>?=null
    private var adapter: RecentViewAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_view)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_recent_view)
        recentViewList= ArrayList()
        setToolBar()
        callRecentHistoryApi()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }


    private fun setToolBar(){
        setSupportActionBar(binding.toolbarRecent)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Recent View History"
        binding.toolbarRecent.navigationIcon!!.setColorFilter(
                this.resources.getColor(R.color.white),
                PorterDuff.Mode.SRC_ATOP
        )
        binding.toolbarRecent.setNavigationOnClickListener {
            finish()
        }
        recycler()
    }

    private fun recycler(){
        adapter= RecentViewAdapter(this,recentViewList!!)
        binding.rvRecent.adapter=adapter
        adapter!!.setOnItemClickListener(object:RecentViewAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, view: View) {
                startActivity(
                        Intent(this@RecentViewActivity, ContentDetailsActivity::class.java)
                                .putExtra("content_id",recentViewList!![position].id.toString())
                                 .putExtra("watch_time", "0")
                                .putExtra("episode_id","0"))
            }
        })
    }

    private fun callRecentHistoryApi() {
        recentViewList!!.clear()
        apiService.callGetWatchHistoryApi("Bearer "+userPref.getToken()!!,
                userPref.getSubUserId().toString(), userPref.getFcmToken()!!,
        userPref.getPreferredLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)
                .subscribe({ commonResponse ->
                    if (commonResponse.status !=0 && commonResponse.mdata != null) {
                        recentViewList!!.addAll(commonResponse.mdata)
                        adapter!!.notifyDataSetChanged()
                        } else {
                        adapter!!.notifyDataSetChanged()
                        confirmationAlert(commonResponse.message!!)
                        //binding!!.swipeContainer.isRefreshing = false
                    //    utils.toaster(commonResponse.message!!)

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

                        /* utils.simpleAlert(
                             requireContext(),
                             "Error",
                             getString(R.string.check_network_connection))*/

                    }

                })
    }

    private fun confirmationAlert(message: String) {

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
    //    bindingDialog.tvTitle.text =message
        bindingDialog.tvTitle.text = getString(R.string.alert)
        //bindingDialog.tvMsg.visibility=GONE
        bindingDialog.tvMsg.text =message



        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
        }

    }

}