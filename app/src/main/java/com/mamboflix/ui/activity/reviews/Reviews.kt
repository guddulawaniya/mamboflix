package com.mamboflix.ui.activity.reviews

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityRatingBinding
import com.mamboflix.databinding.DialogMsgBinding
import com.mamboflix.ui.activity.reviews.adapters.ReviewAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException


class Reviews : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRatingBinding
    var checkexist : String =""
    var list: ArrayList<com.mamboflix.ui.activity.reviews.reviewsmodel.rating_list>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rating)
        list = ArrayList()
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.rv.layoutManager= LinearLayoutManager(this)


        callGetRatingList()

        /*binding.post.setOnClickListener(View.OnClickListener {
            submitrating()
        })*/

        binding.post.setOnClickListener {
            // Check if the list is empty
            if (list.isNullOrEmpty()) {
                if (binding.myRatingBar1.rating == 0f) {
                    utils.simpleAlert(this, getString(R.string.alert), getString(R.string.rating))
                } else {
                    submitrating()
                }
            } else {
                // Check if the review has already been submitted by the user
                val isReviewExist = list!!.any { it.from_id.toString() == userPref.user.id.toString() }

                if (isReviewExist) {
                    utils.simpleAlert(
                        this,
                        getString(R.string.alert),
                        getString(R.string.Reviewalreadysubmitted)
                    )
                } else {
                    if (binding.myRatingBar1.rating == 0f) {
                        utils.simpleAlert(this, getString(R.string.alert), getString(R.string.rating))
                    } else {
                        submitrating()
                    }
                }
            }
        }

    }

    private fun submitrating(){
        if(binding.comment.text.toString().equals("")){
            utils.simpleAlert(
                this,
                resources.getString(R.string.alert),
                resources.getString(R.string.pleaseenterreview)
            )
        }else {
            apiService.callsubmitRatingList(
                "Bearer " + userPref.getToken()
                , userPref.getFcmToken().toString(),
                userPref.getPreferredLanguage(), intent.getStringExtra("contentId").toString(),
                intent.getStringExtra("contenttype").toString(),
                binding.myRatingBar1.rating.toString(),
                binding.comment.text.toString()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)

                .subscribe({ commonResponse ->

                    if (commonResponse.status != 0) {
                        simpleAlert(
                            this,
                            getString(R.string.succes),
                            getString(R.string.Thankyouforyourrating)
                        )

                    } else {
                        // binding!!.swipeContainer.isRefreshing = false
                        utils.simpleAlert(
                            this,
                            "Error",
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
    }

    fun simpleAlert(context: Context, title: String, message: String) {

        var cDialog = Dialog(context,R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogMsgBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
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
        //}

        cDialog!!.show()
        bindingDialog.tvTitle.text = title
        bindingDialog.tvMsg.text = message
        bindingDialog.btCancel.text =resources.getString(R.string.okay)

        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
            binding.comment.setText("")
            binding.myRatingBar1.rating=0f
            callGetRatingList()

        }

    }


    private fun callGetRatingList() {
        list!!.clear()
        apiService.
        callGetRatingList("Bearer "+userPref.getToken()
            ,userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage(),intent.getStringExtra("contentId").toString(),intent.getStringExtra("contenttype").toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)


            .subscribe({ commonResponse ->

                if (commonResponse.status !=0 && commonResponse.mdata != null) {
                    binding.myRatingBar.visibility= VISIBLE
                    binding.rateno.text=commonResponse.mdata.average_rating
                    binding.myRatingBar.rating= commonResponse.mdata.average_rating.toFloat()
                    binding.description.text= "Based on "+commonResponse.mdata.total_review
                    list!!.clear()

                    if (commonResponse.mdata.rating_list!=null && commonResponse.mdata.rating_list.size>0){
                        list!!.addAll(commonResponse.mdata.rating_list)
                        binding.rv.adapter= ReviewAdapter(this,list!!)
                        binding.tvNoData.visibility = GONE
                    }else{
                        binding.tvNoData.visibility = VISIBLE
                    }


                } else {
                    binding.tvNoData.visibility = VISIBLE
                    binding.myRatingBar.visibility=GONE
                    // binding!!.swipeContainer.isRefreshing = false
                    /*  utils.simpleAlert(
                         this,
                          "",
                          commonResponse.message.toString()
                      )*/
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
                        throwable.message.toString())

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