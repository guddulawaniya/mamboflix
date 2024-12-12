package com.mamboflix.ui.activity.signup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityInterestBinding
import com.mamboflix.model.interest.InterestModel
import com.mamboflix.ui.adapter.InterestAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.*
import kotlin.collections.ArrayList

class InterestActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityInterestBinding
    private var adapter: InterestAdapter?=null
    private var listData: ArrayList<InterestModel>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= DataBindingUtil.setContentView(this, R.layout.activity_interest)
        initializeValidation()
    }
    private fun initializeValidation() {
        binding.btnNext.setOnClickListener(this)
        listData = ArrayList()
        callCategoriesAPI()
        setRecyclerview()
    }

    private fun setRecyclerview(){
        adapter = InterestAdapter(this, listData!!)
        val linearLayoutManager =
            GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
        binding.rvInterest.layoutManager = linearLayoutManager
        binding.rvInterest.adapter = adapter
        adapter!!.setOnItemClickListener(object : InterestAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(position: Int, view: View) {
                listData!![position].isSelected = !listData!![position].isSelected
                adapter!!.notifyDataSetChanged()
                //Toast.makeText(this@InterestActivity,"onClick>>"+listData!![position].isSelected,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validation():Boolean{
        var valid = true
        /*if (binding.tvDate.text.toString().isEmpty()){
            binding.tvDate.error = "Please enter Date"
            valid = false;
        }else{
            binding.tvDate.error = null
        }
        if (binding.tvTime.text.toString().isEmpty()){
            binding.tvTime.error = "Please enter Time"
            valid = false;
        }else{
            binding.tvTime.error = null
        }*/
        return valid
    }

    override fun onClick(view: View?) {
        //tvDate
        when(view!!.id) {
            R.id.btnNext ->{
                var isAll: Boolean = false
                for (data in listData!!) {
                    isAll = false
                    if (data.isSelected) {
                        isAll = true
                        break
                    }
                }
                if (isAll){
                    callSubmitCategoryAPI()
                }else{
                    utils.simpleAlert(this,"Error","Please select at list one")
                }
                }
        }
    }
   /* private fun setToolBar(){
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Activity Log "
        binding.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }*/

    //callCategoriesAPI
    private fun callCategoriesAPI() {
        listData!!.clear()
        apiService.callCategoriesAPI("Bearer "+userPref.getToken(),
            userPref.getPreferredLanguage(),
            userPref.getFcmToken().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status !=0 && commonResponse.mdata != null) {
                    listData!!.addAll(commonResponse.mdata)//                    for (i in 0 until listData!!.size){
//                        if (listData!![i].content_count.equals(0)){
//                            listData!!.removeAt(i)
//                        }
//                    }
                    adapter!!.notifyDataSetChanged()
                } else {
                    utils.simpleAlert(
                       this,
                        "",
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


    private fun addJsonArray(model: ArrayList<InterestModel>): JsonArray {

        val jArray: JsonArray = JsonArray()

        for (data in model) {
            if (data.isSelected) {
                var jGroup: JsonObject = JsonObject()
                jGroup.addProperty("id", data.id)
                jArray.add(jGroup)
            }
        }
        return jArray
    }

    private fun callSubmitCategoryAPI() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("device_token", userPref.getFcmToken().toString())
        jsonObject.addProperty("lang_type", userPref.getPreferredLanguage())
        jsonObject.add("categories",addJsonArray(listData!!))

        apiService.callSubmitCategoryAPI("Bearer "+userPref.getToken(),jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)

            .subscribe({ commonResponse ->

                if (commonResponse.status !=0 ) {
                    userPref.setGenere(true)
                    startActivity(Intent(this, WatchTypeActivity::class.java))
                    finish()
                } else {
                    utils.simpleAlert(
                        this,
                        "",
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
                        getString(R.string.check_network_connection))

                }

            })
    }

   /* private fun callloginAPI(mType:String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("username", binding?.edtEmail?.text.toString())
        jsonObject.add("categories", "[{"1"},{"2"}]")
        jsonObject.addProperty("device_token", "token123")


        apiService.callSubmitCategoryAPI("Bearer "+userPref.getToken(),jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)


            .subscribe({ commonResponse ->

                if (commonResponse.status !=0 && commonResponse.mdata != null) {

                    userPref.isLogin= true
                    userPref.user = commonResponse.mdata.user
                    userPref.setToken(commonResponse.mdata.token)
                    startActivity(Intent(this, HomeActivity::class.java))
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
                        getString(R.string.check_network_connection))
                }
            })
    }*/

    override fun onBackPressed() {
//        moveTaskToBack(false)
        finish()
    }
}
