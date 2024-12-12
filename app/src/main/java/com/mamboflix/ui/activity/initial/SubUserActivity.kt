package com.mamboflix.ui.activity.initial


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mamboflix.BaseActivity
import com.mamboflix.HomeActivity3.HomeActivity3
import com.mamboflix.MainActivity
import com.mamboflix.R
import com.mamboflix.databinding.*
import com.mamboflix.model.CreateWatchUserModel
import com.mamboflix.ui.activity.MyDownloadActivity
import com.mamboflix.ui.activity.signup.AddProfileActivity
import com.mamboflix.ui.adapter.subuser.SubUserAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

@Suppress("UNREACHABLE_CODE")
class SubUserActivity : BaseActivity(), View.OnClickListener {
    private var binding: ActivitySubuserBinding? = null
    private var title: String? = ""
    private var manageUserAdapter: SubUserAdapter? = null
    var userList: ArrayList<CreateWatchUserModel>? = null
    lateinit var linearLayoutManager1: GridLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subuser)
        // setToolBar()
        userList = ArrayList()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        binding!!.lytRec.visibility = View.GONE
        binding!!.shimmerFrameLayout.visibility = View.VISIBLE
        binding!!.shimmerFrameLayout.startShimmer()
        callGetWatchingUserAPI()
        setRecyclerview()

        binding!!.lytProfile.setOnClickListener{
            startActivity(Intent(this, AddProfileActivity::class.java)
                .putExtra("enterType",1))
        }
    }

    private fun setRecyclerview() {
        manageUserAdapter = SubUserAdapter(this, userList!!)
        manageUserAdapter!!.setOnItemClickListener(object : SubUserAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(position: Int, view: View, id: String) {
                callswitchProfile(id)
                sendScreen(position)
            }
        })
    }

    private fun callswitchProfile(id: String) {
        apiService.switch_profile(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
             /*.doOnSubscribe(this::showProgressDialog)
             .doOnCompleted(this::hideProgressDialog)*/
            .subscribe({ commonResponse ->
                try {
                    if (commonResponse.status != 0 && commonResponse.mdata != null) {

                    } else {

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, { throwable ->

            })
    }

    private fun sendScreen(pos: Int) {
        userPref.setSubUserId(userList!![pos].id)
        userPref.setSubUserName(userList!![pos].name)
        userPref.setsubuserImg(userList!![pos].image)
        startActivity(Intent(this, HomeActivity3::class.java))
        finish()
    }

    /*   private fun setToolBar(){
           setSupportActionBar(binding!!.toolbar)
           supportActionBar!!.setDisplayHomeAsUpEnabled(true)
           title = getString(R.string.who_s_watching)
           binding!!.toolbar.navigationIcon?.setColorFilter(
               this.resources.getColor(R.color.white),
               PorterDuff.Mode.SRC_ATOP
           )
       }*/

    override fun onClick(v: View?) {

    }



    private fun callGetWatchingUserAPI() {
        userList!!.clear()
        apiService.callGetWatchingUserAPI(
            "Bearer " + userPref.getToken(), userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
             .doOnSubscribe(this::showProgressDialog)
             .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {

                    if (commonResponse.mdata.sub_users != null && commonResponse.mdata.sub_users.size > 0) {
                        userList!!.addAll(commonResponse.mdata.sub_users)
                        if (userList!!.size == 1 || userList!!.size == 0) {
                            linearLayoutManager1 =
                                GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false)
                        } else {
                            linearLayoutManager1 =
                                GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
                        }
                        binding!!.rvMyList.layoutManager = linearLayoutManager1
                        binding!!.rvMyList.adapter = manageUserAdapter
                        binding!!.lytRec.visibility = View.VISIBLE
                        binding!!.shimmerFrameLayout.stopShimmer()
                        binding!!.shimmerFrameLayout.visibility = View.GONE
                    }
                    manageUserAdapter!!.notifyDataSetChanged()
                    } else {
                    binding!!.lytRec.visibility = View.VISIBLE
                    binding!!.shimmerFrameLayout.stopShimmer()
                    binding!!.shimmerFrameLayout.visibility = View.GONE
                    utils.simpleAlert(
                        this,
                        "",
                        commonResponse.message.toString()
                    )
                    //hideProgressDialog()
                }
            }, { throwable ->
                // hideProgressDialog()
                if (throwable is ConnectException) {
//                    utils.simpleAlert(
//                        this,
//                        "Error",
//                        getString(R.string.check_network_connection)
//                    )
                    val intent=Intent(this, MyDownloadActivity::class.java)
                    startActivity(intent)
                } else {
                    utils.simpleAlert(
                        this,
                        "Error",
                        throwable.message.toString()
                    )
                }
            })
    }
}


