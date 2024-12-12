package com.mamboflix.ui.activity.search

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivitySearchBinding
import com.mamboflix.model.searchContent.SearchContentModel
import com.mamboflix.ui.activity.contentdetails.ContentDetailsActivity
import com.mamboflix.ui.adapter.SearchContentAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.*


class SearchActivity : BaseActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var searchContentList: ArrayList<SearchContentModel>? = null
    private var adapter: SearchContentAdapter? = null
    private var contentType: String? = ""
    private var timer: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        searchContentList = ArrayList()
        contentType = intent.getStringExtra("contentType")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setToolBar()
    }

    private fun setToolBar() {
        setSupportActionBar(binding.toolbarSearch)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = " "
        binding.toolbarSearch.navigationIcon!!.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        binding.toolbarSearch.setNavigationOnClickListener {
            finish()
        }

        recycler()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val alertMenuItem = menu!!.findItem(R.id.action_search)
        val searchView = alertMenuItem!!.actionView as SearchView
        searchView.isIconified = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (timer != null) {
                    timer!!.cancel()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                timer = Timer()
                timer!!.schedule(object : TimerTask() {
                    override fun run() {
                        this@SearchActivity.runOnUiThread(Runnable {
                            //showProgressbar(true)
                            //resultText.setVisibility(View.INVISIBLE)
                            callDashboardApi(newText!!)
                        })
                        try {
                            Thread.sleep(2000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        // do your actual work here
                    }
                }, 600)

                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }


    private fun recycler() {

        adapter = SearchContentAdapter(this, searchContentList!!)
        binding.rvSearch.adapter = adapter
        adapter!!.setOnItemClickListener(object : SearchContentAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                startActivity(
                    Intent(this@SearchActivity, ContentDetailsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("content_id", searchContentList!![position].id)
                        .putExtra("watch_time", "0")
                        .putExtra("episode_id", "0")
                )
            }


        })
    }

    private fun callDashboardApi(keyword: String) {
        apiService.callSearchContentAPI(
            "Bearer " + userPref.getToken()!!, contentType!!, keyword,
            userPref.getSubUserId().toString(),
            userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*  .doOnSubscribe(this::showProgressDialog)
              .doOnCompleted(this::hideProgressDialog)*/
            .subscribe({ commonResponse ->
                try {
                    if (commonResponse.status != 0 && commonResponse.mdata != null) {
                        searchContentList!!.clear()
                        searchContentList!!.addAll(commonResponse.mdata)
                        adapter!!.notifyDataSetChanged()
                        binding.rvSearch.visibility=View.VISIBLE

                    } else {
                        searchContentList!!.clear()
                        binding.rvSearch.visibility=View.GONE
                        adapter!!.notifyDataSetChanged()
                        //binding!!.swipeContainer.isRefreshing = false
                        // utils.toaster(commonResponse.message!!)
                        //hideProgressDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
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