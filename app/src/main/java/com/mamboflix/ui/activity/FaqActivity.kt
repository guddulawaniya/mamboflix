package com.mamboflix.ui.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityFaqBinding
import com.mamboflix.model.FaqMainModel
import com.mamboflix.model.FaqModel
import com.mamboflix.ui.adapter.FaqExpandableAdapter
import com.mamboflix.utils.ToastObj
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class FaqActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityFaqBinding
    private var expandableAdapter: FaqExpandableAdapter? = null
    private var headerList: ArrayList<String>? = null
    private var childList: ArrayList<FaqModel>? = null
    private var lastExpandedPosition: Int = -1
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_faq)

        setToolBar()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    private fun setToolBar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "FAQ"
        binding.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )

        callFaqAPI()
    }

    override fun onClick(view: View?) {

        //tvDate

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


    private fun callFaqAPI() {
        apiService.callGetFaqAPI("Bearer "+userPref.getToken(),
            userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { showProgressDialog() }
            .doOnCompleted { hideProgressDialog() }
            .subscribe(
                { commonResponse ->
                    // utils.simpleAlert(this, "", it.msg.toString())
                    if (commonResponse.status == 1) {
                        setData(commonResponse.mdata as ArrayList<FaqMainModel>?)

                    } else {
                        val error = utils.fromHtml(commonResponse.message!!)
                        ToastObj.message(this, error.toString())
                    }

                }, {

                    hideProgressDialog()
                    if (it is ConnectException) {
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
                }
            )
    }

    private fun setData(mData: ArrayList<FaqMainModel>?) {

        expandableAdapter = FaqExpandableAdapter(this, mData)
        binding.faqRecyclerview.adapter =  expandableAdapter
        binding.faqRecyclerview.layoutManager = LinearLayoutManager(this)

    }

}
