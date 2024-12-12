package com.mamboflix.ui.activity.purchsedhistory

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityPurchasinglistBinding
import com.mamboflix.ui.activity.purchsedhistory.paymenthistory.history
import com.vaidic.guru.ui.activity.register.adapters.purchasedAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException


class PurchasedHistory : BaseActivity(), View.OnClickListener,purchasedAdapter.unsubscribed {

    private lateinit var binding: ActivityPurchasinglistBinding
    var list: ArrayList<history>? = null
    lateinit var FromHome:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchasinglist)
        list = ArrayList()
        binding.ivBack.setOnClickListener { finish() }

        binding.rv.layoutManager = LinearLayoutManager(this)

        if (intent != null){
            FromHome = intent.getStringExtra("FromHome").toString()
        }
        if (FromHome == "FromHome"){
            binding.tvHead.text = "My Plan"
        }else{
            binding.tvHead.text = "Payment History"
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        callGetHistoryApi()

    }




    private fun unsubscribed_plan(id:String) {
        apiService.unsubscribed_plan(
            "Bearer " + userPref.getToken(),
            id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.data != null) {

                    Toast.makeText(this@PurchasedHistory, commonResponse.message, Toast.LENGTH_SHORT).show()


                } else {
//                    utils.simpleAlert1(
//                        this,
//                        resources.getString(R.string.alert),
//                        commonResponse.message.toString(), 1
//                    )

                }

            }, { throwable ->
                //hideProgressDialog()
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
                        throwable.message.toString()
                    )

                    /* utils.simpleAlert(
                         requireContext(),
                         "Error",
                         getString(R.string.check_network_connection))*/

                }

            })
    }





    private fun callGetHistoryApi() {
        list!!.clear()
        apiService.callGetpaymenthistory(
            "Bearer " + userPref.getToken(), userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    list!!.clear()
                    list!!.addAll(commonResponse.mdata)
                    if (list!!.isNotEmpty() && list != null && list!!.size > 0) {
                        binding.rv.adapter = purchasedAdapter(this, list!!,this,FromHome)

                        binding.tvPurchaseHistory.visibility = View.GONE
                    } else {
                        binding.rv.visibility = View.INVISIBLE
                        binding.tvPurchaseHistory.visibility = View.VISIBLE
                    }


                } else {
//                    utils.simpleAlert1(
//                        this,
//                        resources.getString(R.string.alert),
//                        commonResponse.message.toString(), 1
//                    )

                }

            }, { throwable ->
                //hideProgressDialog()
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
                        throwable.message.toString()
                    )

                    /* utils.simpleAlert(
                         requireContext(),
                         "Error",
                         getString(R.string.check_network_connection))*/

                }

            })
    }


    override fun onClick(v: View?) {
        when (v?.id) {


        }
    }

    override fun dataforunsubscribed(id: String, layout: TextView) {
        layout.setOnClickListener {
            layout.visibility = View.GONE
            unsubscribed_plan(id)
        }

    }


}