package com.mamboflix.ui.activity.filter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityFilterBinding
import com.mamboflix.model.searchContent.SearchContentModel
import com.mamboflix.ui.activity.contentdetails.ContentDetailsActivity
import com.mamboflix.ui.adapter.SearchContentAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class FilterActivity : BaseActivity() {
    private lateinit var binding: ActivityFilterBinding
    private var searchContentList: ArrayList<SearchContentModel>? = null
    private var adapter: SearchContentAdapter? = null
    private var cat_id: String? = ""
    private var categoryName: String? = ""
    private var type = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)
        searchContentList = ArrayList()
        cat_id = intent.getStringExtra("cat_id")
        categoryName = intent.getStringExtra("categoryName")
        type = intent.getStringExtra("type")!!
        setToolBar()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        binding.nsvMain.visibility = View.GONE
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.shimmerFrameLayout.startShimmer()

        if (type == "1") {
            callMoviesByCategoryApi()
        } else {
            callShowsByCategoryApi()
        }
    }
    private fun setToolBar() {
        setSupportActionBar(binding.toolbarFilter)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = categoryName
        binding.toolbarFilter.navigationIcon!!.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        binding.toolbarFilter.setNavigationOnClickListener {
            finish()
        }
        recycler()

    }

    private fun recycler() {
        adapter = SearchContentAdapter(this, searchContentList!!)
        binding.rvFilter.adapter = adapter
        adapter!!.setOnItemClickListener(object : SearchContentAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                startActivity(
                    Intent(this@FilterActivity, ContentDetailsActivity::class.java)
                        .putExtra("content_id", searchContentList!![position].id)
                        .putExtra("watch_time", "0")
                        .putExtra("episode_id", "0")
                )
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun callMoviesByCategoryApi() {
        searchContentList!!.clear()
        apiService.callGetMovieByCategoryApi(
            "Bearer " + userPref.getToken()!!,
            userPref.getSubUserId().toString(), cat_id!!, userPref.getFcmToken()!!,
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /* .doOnSubscribe(this::showProgressDialog)
             .doOnCompleted(this::hideProgressDialog)*/
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    binding.nsvMain.visibility = View.VISIBLE
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = View.GONE
                    //commonResponse.mdata.content?.all_content?.let { searchContentList!!.addAll(it) }
                    if (commonResponse.mdata.content != null && commonResponse.mdata.content.all_content.size > 0) {
                        searchContentList!!.addAll(commonResponse.mdata.content.all_content)
                        binding.tvNoData.visibility = View.GONE
                    } else {
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                    adapter?.notifyDataSetChanged()
                    binding.tvNoData.visibility = View.GONE
                } else {
                    binding.nsvMain.visibility = View.VISIBLE
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = View.GONE
                    adapter?.notifyDataSetChanged()
                    binding.tvNoData.visibility = View.VISIBLE
                    //binding!!.swipeContainer.isRefreshing = false
                    //utils.toaster(commonResponse.message!!)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun callShowsByCategoryApi() {
        searchContentList!!.clear()
        apiService.callGetShowByCategoryApi(
            "Bearer " + userPref.getToken()!!,
            userPref.getSubUserId().toString(), cat_id!!, userPref.getFcmToken()!!,
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*.doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)*/
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    binding.nsvMain.visibility = View.VISIBLE
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = View.GONE
                    if (commonResponse.mdata.content != null && commonResponse.mdata.content.all_content.size > 0) {
                        searchContentList!!.addAll(commonResponse.mdata.content.all_content)
                        binding.tvNoData.visibility = View.GONE
                    } else {
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                    adapter?.notifyDataSetChanged()
                 } else {
                    binding.nsvMain.visibility = View.VISIBLE
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = View.GONE
                    adapter?.notifyDataSetChanged()
                    binding.tvNoData.visibility = View.VISIBLE
                    //binding!!.swipeContainer.isRefreshing = false
                    //utils.toaster(commonResponse.message!!)
                    //hideProgressDialog()
                }
             }, { throwable ->
                // hideProgressDialog()
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