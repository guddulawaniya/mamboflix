package com.mamboflix.ui.activity.payment


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityPurchasingPackageBinding
import com.mamboflix.databinding.DialogMsgBinding
import com.mamboflix.ui.activity.offersection.Offers
import com.mamboflix.ui.activity.payment.model.PackageListModel
import com.mamboflix.ui.activity.profile.EditProfileActivity
import com.mamboflix.ui.adapter.PaymentBillingAdapter
import kotlinx.coroutines.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

@Suppress("UNREACHABLE_CODE")
class PaymentBillingActivity : BaseActivity(), View.OnClickListener, CoroutineScope by MainScope() {
    private var binding: ActivityPurchasingPackageBinding? = null
    private var myListAdapter: PaymentBillingAdapter? = null
    private var packageListsingleuser: ArrayList<PackageListModel>? = null
    private var packageList: ArrayList<PackageListModel>? = null
    private var packageListfamily: ArrayList<PackageListModel>? = null
    var click: Int = 0
    var country_code: String = ""
    var country_name: String = ""
    var is_subscribed: String = ""
    lateinit var linearLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchasing_package)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setToolBar()

        packageListsingleuser = ArrayList()
        packageListfamily = ArrayList()
        packageList = ArrayList()

        callGetPackageAPI()
        //callPackAPI()

        setRecyclerview()

        setListner()

        binding!!.familypack.setBackgroundResource(R.drawable.round_corner_light)
        binding!!.singleuser.setBackgroundResource(R.drawable.round_corner_light1)
        binding!!.selectsinglepack.setBackgroundResource(R.drawable.circle_white_corner1)
        binding!!.selectfamilypack.setBackgroundResource(R.drawable.circle_white_corner)
    }

    private fun setListner() {
        binding!!.familypack.setOnClickListener(this)
        binding!!.singleuser.setOnClickListener(this)
        binding!!.applycoupon.setOnClickListener(this)
    }

    private fun setRecyclerview() {
        myListAdapter = PaymentBillingAdapter(this, packageListsingleuser!!, country_code,country_name,is_subscribed)
        linearLayoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
/*
        linearLayoutManager.setSpanSizeLookup(object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // grid items to take 1 column
                return if (packageListsingleuser!!.size % 2 == 0) {
                    1
                } else {
                    if (position == packageListsingleuser!!.size - 1) 2 else 1
                }
            }
        })*/
        binding!!.rec.layoutManager = linearLayoutManager
        binding!!.rec.adapter = myListAdapter

        myListAdapter!!.setOnItemClickListener(object : PaymentBillingAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(position: Int, view: View, type: String) {
                Log.v("type", type)
                if (type == "1") {
                    startActivity(
                        Intent(this@PaymentBillingActivity, PaymentBillingDetailsActivity::class.java)
                            .putExtra("model", packageListsingleuser!![position])
                    )
                } else {
                    startActivity(
                        Intent(
                            this@PaymentBillingActivity,
                            PaymentBillingDetailsActivity::class.java
                        )
                            .putExtra("model", packageListfamily!![position])
                    )

                }

                //offerCheck(position)
            }
        })
    }



    private fun offerCheck(pos: Int) {
        apiService.callGetOfferList(
            "Bearer " + userPref.getToken(), userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /* .doOnSubscribe(this::showProgressDialog)
             .doOnCompleted(this::hideProgressDialog)*/


            .subscribe({ commonResponse ->

                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    startActivity(
                        Intent(this@PaymentBillingActivity, Offers::class.java)
                            .putExtra("model", packageListsingleuser!![pos])
                            .putExtra("value", 1)
                    )
                } else {
                    startActivity(
                        Intent(
                            this@PaymentBillingActivity,
                            PaymentBillingDetailsActivity::class.java
                        ).putExtra("model", packageListsingleuser!![pos])
                    )
                }

            }, { throwable ->
                //hideProgressDialog()
                if (throwable is ConnectException) {
                    utils.simpleAlert(
                        this@PaymentBillingActivity,
                        "Error",
                        getString(R.string.check_network_connection)
                    )

                } else {

                    utils.simpleAlert(
                        this@PaymentBillingActivity,
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


    private fun callGetPackageListAfterCouponApply() {
        apiService.callGetPackageListAfterCouponApply(
            "Bearer " + userPref.getToken(),
            userPref.getFcmToken().toString(),
            binding!!.entercoupon.text.toString(),
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)


            .subscribe({ commonResponse ->
                if (commonResponse.status == 1 && commonResponse.mdata != null &&
                    commonResponse.mdata.size > 0
                ) {
                    packageListsingleuser!!.clear()
                    packageListfamily!!.clear()
                    packageList!!.clear()
                    packageList!!.addAll(commonResponse.mdata)
                    //setData(commonResponse.mdata as ArrayList<FaqMainModel>?)
                    for (i in 0 until commonResponse.mdata!!.size) {
                        if (commonResponse.mdata.get(i).package_type == "1") {
                            packageListsingleuser!!.add(commonResponse.mdata.get(i))
                        } else {
                            packageListfamily!!.add(commonResponse.mdata.get(i))
                        }
                    }

                    Log.v("size", packageListsingleuser!!.size.toString())
                    Log.v("size1", packageListfamily!!.size.toString())

                    myListAdapter!!.notifyDataSetChanged()

                } else {
                    utils.simpleAlert(
                        this@PaymentBillingActivity,
                        "Alert",
                        commonResponse.message!!.toString()
                    )
                }

            }, { throwable ->
                //hideProgressDialog()
                if (throwable is ConnectException) {
                    utils.simpleAlert(
                        this@PaymentBillingActivity,
                        "Error",
                        getString(R.string.check_network_connection)
                    )

                } else {

                    utils.simpleAlert(
                        this@PaymentBillingActivity,
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

    private fun setToolBar() {
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Payment and Billing"
        binding!!.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
    }


    override fun onResume() {
        super.onResume()
        callGetPackageAPI()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.familypack -> {
                click = 1
                binding!!.familypack.setBackgroundResource(R.drawable.round_corner_light1)
                binding!!.singleuser.setBackgroundResource(R.drawable.round_corner_light)
                binding!!.selectsinglepack.setBackgroundResource(R.drawable.circle_white_corner)
                binding!!.selectfamilypack.setBackgroundResource(R.drawable.circle_white_corner1)
                //  myListAdapter!!.notifyDataSetChanged()
                /* linearLayoutManager.setSpanSizeLookup(object : SpanSizeLookup() {
                     override fun getSpanSize(position: Int): Int {
                         // grid items to take 1 column
                         return if (packageListfamily!!.size % 2 == 0) {
                             1
                         } else {
                             if (position == packageListfamily!!.size - 1) 2 else 1
                         }
                     }
                 })
                 binding!!.rec.layoutManager = linearLayoutManager*/
                myListAdapter = PaymentBillingAdapter(this, packageListfamily!!, country_code,country_name,is_subscribed)
                binding!!.rec.adapter = myListAdapter
                myListAdapter!!.notifyDataSetChanged()
            }
            R.id.singleuser -> {
                click = 0
                binding!!.selectsinglepack.setBackgroundResource(R.drawable.circle_white_corner1)
                binding!!.selectfamilypack.setBackgroundResource(R.drawable.circle_white_corner)
                binding!!.familypack.setBackgroundResource(R.drawable.round_corner_light)
                binding!!.singleuser.setBackgroundResource(R.drawable.round_corner_light1)
                //  myListAdapter!!.notifyDataSetChanged()
                myListAdapter = PaymentBillingAdapter(this, packageListsingleuser!!, country_code,country_name,is_subscribed)
                binding!!.rec.adapter = myListAdapter
                myListAdapter!!.notifyDataSetChanged()
            }
            R.id.applycoupon -> {
                callGetPackageListAfterCouponApply()
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


    /* private fun callPackAPI(){
             try {
                launch(Dispatchers.Main) {
                     val response = apiService.callGetPackageAPI(
                         "Bearer " + userPref.getToken(), userPref.getFcmToken().toString(),
                         userPref.getPreferredLanguage())

                     if (response.status == 1 && response.mdata!=null && response.mdata.size>0) {
                         //setData(commonResponse.mdata as ArrayList<FaqMainModel>?)
                         packageList!!.addAll(response.mdata)
                         myListAdapter!!.notifyDataSetChanged()

                     }else{
                         Toast.makeText(
                             this@PaymentBillingActivity,
                             "Error Occurred: ${response.message}",
                             Toast.LENGTH_LONG).show()
                     }

                 }
             }catch (e: Exception) {
             // Show API error. This is the error raised by the client.
             Toast.makeText(this@PaymentBillingActivity,
                 "Error Occurred: ${e.message}",
                 Toast.LENGTH_LONG).show()
         }


     }*/

    private fun callGetPackageAPI() {
        apiService.callGetPackageAPI(
            "Bearer " + userPref.getToken(), userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { showProgressDialog() }
            .doOnCompleted { hideProgressDialog() }
            .subscribe(
                { commonResponse ->
                    packageListsingleuser!!.clear()
                    packageListfamily!!.clear()
                    country_code = commonResponse.country_code.toString()
                    country_name = commonResponse.country.toString()
                    is_subscribed = commonResponse.is_subscribed.toString()

                    // utils.simpleAlert(this, "", it.msg.toString())
                    if (commonResponse.status == 1 && commonResponse.mdata != null &&
                        commonResponse.mdata.size > 0
                    ) {
                        Log.d("TAG", "callGetPackageAPI:vcvc " + commonResponse.country_code)
                        packageList!!.clear()
                        packageList!!.addAll(commonResponse.mdata)
                        country_code = commonResponse.country_code.toString()
                        country_name = commonResponse.country.toString()
                        is_subscribed = commonResponse.is_subscribed.toString()

                        //setData(commonResponse.mdata as ArrayList<FaqMainModel>?)
                        for (i in 0 until commonResponse.mdata!!.size) {
                            if (commonResponse.mdata.get(i).package_type == "1") {
                                packageListsingleuser!!.add(commonResponse.mdata.get(i))
                            } else {
                                packageListfamily!!.add(commonResponse.mdata.get(i))
                            }

                        }
                        setRecyclerview()
                        Log.d("size", packageListsingleuser!!.size.toString())
                        Log.d("size1", packageListfamily!!.size.toString())
                        Log.d("size2 ", "   nnn  " + Gson().toJson(packageListfamily))

                        myListAdapter!!.notifyDataSetChanged()



                    } else {
                        val error = utils.fromHtml(commonResponse.message!!)
//                        utils.simpleAlert(
//                            this,
//                            "Note",
//                            getString(R.string.completeprofile))
                        var cDialog = Dialog(this@PaymentBillingActivity,R.style.Theme_Tasker_Dialog)
                        val bindingDialog: DialogMsgBinding = DataBindingUtil.inflate(
                            LayoutInflater.from(this@PaymentBillingActivity),
                            R.layout.dialog_msg,
                            null,
                            false
                        )
                        cDialog!!.setContentView(bindingDialog.root)
                        cDialog!!.setCancelable(true)
                        cDialog!!.window!!.setLayout(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT
                        )
                        //}

                        cDialog!!.show()
                        bindingDialog.tvTitle.text = "Note"
                        bindingDialog.tvMsg.text = "Please complete your profile first"

                        bindingDialog.btCancel.setOnClickListener {
                            cDialog.dismiss()
                            startActivity(Intent(this@PaymentBillingActivity,EditProfileActivity::class.java))
                        }
                        bindingDialog.btSubmit.setOnClickListener {
                            cDialog.dismiss()
                            startActivity(Intent(this@PaymentBillingActivity,EditProfileActivity::class.java))

                        }
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

}


