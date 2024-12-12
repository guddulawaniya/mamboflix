package com.mamboflix.ui.activity.offersection

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityOfferlistBinding
import com.mamboflix.ui.activity.offersection.offers.OffersList
import com.mamboflix.ui.activity.payment.PaymentBillingDetailsActivity
import com.mamboflix.ui.activity.payment.model.PackageListModel
import com.vaidic.guru.ui.activity.register.adapters.OffersAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException


class Offers : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOfferlistBinding
    var list: ArrayList<OffersList>? = null
    var value: Int = 0
    var packageid: String = ""
    private var pMpdel: PackageListModel? = null
    private var myListAdapter: OffersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offerlist)
        list = ArrayList()
        binding.ivBack.setOnClickListener { finish() }

        binding.rv.layoutManager = LinearLayoutManager(this)

        value = intent.getIntExtra("value", 0)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        if (value == 1) {
            var bundle: Bundle? = intent.extras
            if (bundle != null) {
                pMpdel = bundle.get("model") as PackageListModel?
            }
            packageid = pMpdel?.id.toString()
        } else {
            packageid = ""
        }

        myListAdapter = OffersAdapter(this, list!!)
        binding!!.rv.adapter = myListAdapter

        myListAdapter!!.setOnItemClickListener(object : OffersAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(position: Int, view: View) {
                if (value == 1) {

                    apiService.callapplyoffer(
                        "Bearer " + userPref.getToken(), userPref.getFcmToken().toString(),
                        userPref.getPreferredLanguage(), list!!.get(position).id, packageid
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(this@Offers::showProgressDialog)
                        .doOnCompleted(this@Offers::hideProgressDialog)


                        .subscribe({ commonResponse ->

                            if (commonResponse.status == 1) {
                                startActivity(
                                    Intent(this@Offers, PaymentBillingDetailsActivity::class.java)
                                        .putExtra("model", pMpdel)
                                        .putExtra("value", 1)
                                        .putExtra(
                                            "percentage",
                                            commonResponse.mdata!!.offer_percentage
                                        )
                                        .putExtra(
                                            "discount",
                                            commonResponse.mdata!!.discount_amount
                                        )
                                        .putExtra(
                                            "amount",
                                            commonResponse.mdata!!.after_offer_amount
                                        )
                                        .putExtra("country_code", commonResponse.country_code)
                                )
                            } else {
                                // binding!!.swipeContainer.isRefreshing = false
                                utils.simpleAlert(
                                    this@Offers,
                                    "Error",
                                    commonResponse.message.toString()
                                )
                                //hideProgressDialog()
                            }

                        }, { throwable ->
                            //hideProgressDialog()
                            if (throwable is ConnectException) {
                                utils.simpleAlert(
                                    this@Offers,
                                    "Error",
                                    getString(R.string.check_network_connection)
                                )

                            } else {

                                utils.simpleAlert(
                                    this@Offers,
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


            }
        })
        callGetOffersApi()

    }


    private fun callGetOffersApi() {
        Log.v("token", userPref.getFcmToken().toString())
        list!!.clear()
        apiService.callGetOfferList(
            "Bearer " + userPref.getToken(), userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)


            .subscribe({ commonResponse ->

                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    list!!.addAll(commonResponse.mdata)
                    myListAdapter!!.notifyDataSetChanged()

                } else {
                    // binding!!.swipeContainer.isRefreshing = false
                    utils.simpleAlert(
                        this,
                        "",
                        commonResponse.message.toString()
                    )
                    //hideProgressDialog()
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


}