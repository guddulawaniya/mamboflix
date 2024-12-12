package com.mamboflix.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.*
import com.mamboflix.model.mylist.MyListModel
import com.mamboflix.ui.activity.contentdetails.ContentDetailsActivity
import com.mamboflix.ui.adapter.mylist.MyListAdapter
import com.mamboflix.ui.navigater.DashboardTabNavigator
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.Locale

class MyListActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mNavigator: DashboardTabNavigator
    private lateinit var binding: ActivityMylistBinding
    private var myListAdapter: MyListAdapter? = null
    var myList : ArrayList<MyListModel>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       binding= DataBindingUtil.setContentView(this, R.layout.activity_mylist)
        myList = ArrayList()
        binding!!.nsvMain.visibility = View.GONE
        binding!!.shimmerFrameLayout.visibility = View.VISIBLE
        binding!!.shimmerFrameLayout.startShimmer()
        getCountryName(this)
        setToolBar()
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
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        callMyListAPI()
        setRecyclerview()
        binding.btAddList.setOnClickListener(this)
        //callGetContentApi()
      }

    private fun setRecyclerview() {
        myListAdapter = MyListAdapter(this,myList!!)
        val linearLayoutManager =
            GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        binding!!.rvMyList.layoutManager = linearLayoutManager
        binding!!.rvMyList.adapter = myListAdapter

        myListAdapter!!.setOnItemClickListener(object : MyListAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(position: Int, view: View, filterList: ArrayList<MyListModel>) {
                startActivity(Intent(this@MyListActivity, ContentDetailsActivity::class.java)
                    .putExtra("content_id",filterList!![position].content_id)
                    .putExtra("episode_id",filterList!![position].episode_id)
                    .putExtra("watch_time","0"))
            }
            override fun onLongPress(position: Int, view: View, filterList: ArrayList<MyListModel>) {
                confirmationAlert(filterList!![position])
            }
        })
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.btAddList->{
                //Constants.AdminDashboardBottomNavTab.TODAY
                //HomeActivity().redirectToTodayTab(com.mamboflix.ui.Constants.DriverDashboardBottomNavTab.HOME)
                finish()
            }

        }
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

    private fun callMyListAPI() {
        myList!!.clear()
        apiService.callMyListAPI("Bearer "+userPref.getToken(),userPref.getSubUserId().toString(),
            userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage(),getCountryCode(getCountryName(this))!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
          /*  .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)*/
            .subscribe({ commonResponse ->
                if (commonResponse.status !=0 && commonResponse.mdata != null) {
                    binding!!.nsvMain.visibility = View.VISIBLE
                    binding!!.shimmerFrameLayout.stopShimmer()
                    binding!!.shimmerFrameLayout.visibility = View.GONE
                    myList!!.addAll(commonResponse.mdata)
                    binding.tvNoData.visibility = View.GONE
                    myListAdapter!!.notifyDataSetChanged()
                } else {
                    binding!!.nsvMain.visibility = View.VISIBLE
                    binding!!.shimmerFrameLayout.stopShimmer()
                    binding!!.shimmerFrameLayout.visibility = View.GONE
                    binding.llEmptyMyList.visibility=View.VISIBLE
                   // hideProgressDialog()
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
                        throwable.message.toString())

                }

            })
    }


    private fun callMakeListApi(isStatus:String , mList: MyListModel) {
        apiService.callMakeListApi(
            "Bearer "+userPref.getToken(),
            userPref.getSubUserId().toString(),
            mList.content_id!!,
            mList!!.cat_id,
            isStatus,
            userPref.getFcmToken().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                /*.doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)*/
                .subscribe({ commonResponse ->
                    if (commonResponse.status !=0) {

                        binding!!.nsvMain.visibility = View.VISIBLE
                        binding!!.shimmerFrameLayout.stopShimmer()
                        binding!!.shimmerFrameLayout.visibility = View.GONE
                        binding!!.tvNoData.visibility = View.GONE
                        myList!!.remove(mList)
                        myListAdapter!!.mFilterList!!.remove(mList)
                        myListAdapter!!.notifyDataSetChanged()
                        //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(this, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
                        Toast.makeText(this,""+commonResponse.message, Toast.LENGTH_SHORT).show()

                        if (myList!!.isEmpty()){
                            binding!!.llEmptyMyList.visibility=View.VISIBLE
                        }
                    } else {
                        binding!!.nsvMain.visibility = View.VISIBLE
                        binding!!.shimmerFrameLayout.stopShimmer()
                        binding!!.shimmerFrameLayout.visibility = View.GONE
                        binding.llEmptyMyList.visibility=View.VISIBLE
                       /* utils.simpleAlert(
                                this,
                                "Error",
                                commonResponse.message.toString()
                        )*/
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
                                throwable.message.toString())
                    }
                })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val alertMenuItem = menu!!.findItem(R.id.action_search)
        val searchView = alertMenuItem!!.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                myListAdapter!!.filter.filter((newText.toString()))
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun confirmationAlert(file: MyListModel) {

        var cDialog = Dialog(this,R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogCustomBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.dialog_custom,
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
        bindingDialog.tvTitle.text = getString(R.string.confirmation)
        bindingDialog.tvMsg.text = getString(R.string.do_you_want)

        bindingDialog.btOk.setOnClickListener {
            cDialog!!.dismiss()
            binding!!.nsvMain.visibility = View.GONE
            binding!!.shimmerFrameLayout.visibility = View.VISIBLE
            binding!!.shimmerFrameLayout.startShimmer()
            callMakeListApi("0",file)
        }

        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
        }

    }
    fun getCountryCode(countryName: String): String? {
        val isoCountryCodes = Locale.getISOCountries()
        for (code in isoCountryCodes) {
            val locale = Locale("", code)
            if (countryName.equals(locale.displayCountry, ignoreCase = true)) {
                return code
            }
        }
        return ""
    }
    fun getCountryName(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = telephonyManager.networkCountryIso.toUpperCase(Locale.getDefault())
        getCountryCode(Locale("", countryCode).displayCountry)
        return Locale("", countryCode).displayCountry
    }
}
