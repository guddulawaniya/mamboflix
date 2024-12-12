package com.mamboflix.ui.activity.HomeActivity2

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.mamboflix.R
import com.mamboflix.databinding.ActivityHelpBinding
import com.mamboflix.databinding.ActivityHome2Binding
import com.mamboflix.databinding.DialogCustomBinding
import com.mamboflix.model.DashboardHeaderMenuModel
import com.mamboflix.prefs.UserPref
import com.mamboflix.ui.activity.*
import com.mamboflix.ui.activity.login.LoginActivity
import com.mamboflix.ui.activity.managedevices.ManageDevicesActivity
import com.mamboflix.ui.activity.payment.PaymentBillingActivity
import com.mamboflix.ui.activity.profile.ProfileActivity
import com.mamboflix.ui.activity.settings.SettingsActivity
import com.mamboflix.ui.adapter.NavExpandableListAdapter
import com.mamboflix.ui.fragment.DashboardFragment
import com.mamboflix.ui.fragment.MoviesTabFragment
import com.mamboflix.ui.fragment.MyListFragment
import com.mamboflix.ui.fragment.TvShowsFragment
import com.mamboflix.utils.CommonUtils


class HomeActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private var headerList: MutableList<DashboardHeaderMenuModel>? = null
    private var listDataChild: HashMap<String, List<String>>? = null
    lateinit var userPref1: UserPref
    lateinit var tv_more_text: TextView
    lateinit var constraint_click: ConstraintLayout
    lateinit var drawer_layout: DrawerLayout
    private lateinit var adapter: NavExpandableListAdapter
    private var exit: Boolean = false
    lateinit var mfragment: Fragment
    private var mGoogleSignInClient: GoogleSignInClient? = null
    val tabi = intArrayOf(
        R.drawable.ic_home_dark,
        R.drawable.ic_movies_dark,
        R.drawable.ic_tv_dark,
        R.drawable.ic_list_dark
    )

    private lateinit var binding: ActivityHome2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        userPref1 = UserPref(applicationContext)

        pager = findViewById(R.id.viewPager)
        tab = findViewById(R.id.tabs)
        tv_more_text = findViewById(R.id.tv_more_text)
        constraint_click = findViewById(R.id.constraint_click)
        drawer_layout = findViewById(R.id.drawer_layout)

        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(DashboardFragment(), "Home")
        adapter.addFragment(MoviesTabFragment(), "Movie")
        adapter.addFragment(TvShowsFragment(), "Tv Shows")
        adapter.addFragment(MyListFragment(), "My List")

        tab.tabGravity = TabLayout.GRAVITY_FILL
        pager.adapter = adapter

        tab.setupWithViewPager(pager)
        tab.getTabAt(0)!!.setIcon(tabi[0])
        tab.getTabAt(1)!!.setIcon(tabi[1])
        tab.getTabAt(2)!!.setIcon(tabi[2])
        tab.getTabAt(3)!!.setIcon(tabi[3])


        constraint_click.setOnClickListener {
            openDrawer()
//            view_under_text_more.visibility = View.VISIBLE
//            Toast.makeText(applicationContext, "click ", Toast.LENGTH_SHORT).show()
        }
        mfragment = DashboardFragment()

        setNavigationData()
    }

    override fun onBackPressed() {
        closeDrawer()
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
//            binding!!.llAppBarDashboard.bottomNav.menu.getItem(0).isChecked = true
            mfragment = DashboardFragment()
            CommonUtils.setFragment(mfragment, false, this, R.id.frameContainer)
        } else {
            if (mfragment is DashboardFragment) {
                if (exit) {
                    super.onBackPressed()
                    finishAffinity()
                    //finish() // finish activity
                } else {
                    Handler().postDelayed({
                        Toast.makeText(
                            applicationContext,
                            "Press Back again to Exit.",
                            Toast.LENGTH_SHORT
                        ).show()
//                        utils.toaster(getString(R.string.pressbackagain))
                        exit = true

                    }, 3 * 100)
                }
            } else {
//                binding!!.llAppBarDashboard.bottomNav.menu.getItem(0).isChecked = true
                mfragment = DashboardFragment()
                CommonUtils.setFragment(mfragment, false, this, R.id.frameContainer)
            }
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /* if (binding!!.llAppBarDashboard.bottomNav.menu.getItem(AppConstant.tabIndex).itemId!=Constants.DriverDashboardBottomNavTab.HOME) {
             binding!!.llAppBarDashboard.bottomNav.menu.getItem(0).isChecked = true
             mfragment = DashboardFragment()
             CommonUtils.setFragment(mfragment, false, this, R.id.frameContainer)
         } else {
             if (mfragment is DashboardFragment) {
                 *//*(this as AppCompatActivity).supportActionBar?.show()
                CommonUtils.exitDialog(this)*//*
                if (exit){
                    super.onBackPressed()
                    finish() // finish activity
                }else{
                    Handler().postDelayed({
                        utils.toaster(getString(R.string.pressbackagain))
                        exit = true

                    }, 3 * 100)
                }
            }

        }*/



        fun onBack() {
            supportFragmentManager.popBackStack()
//        mfragment = null
        }
    }

    private fun openDrawer() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return true
    }

    private fun setNavigationData() {
        binding.header.tvName0.text = "Welcome " + userPref1.getSubUserName()
        prepareNavMenuList()
        navMenuClickListener()
    }

    private fun prepareNavMenuList() {
        headerList = ArrayList()
        headerList!!.add(
            DashboardHeaderMenuModel(
                getString(R.string.notifications),
                false
            )
        )
        /* headerList!!.add(
                 DashboardHeaderMenuModel(
                         getString(R.string.my_list),
                         false
                 )
         )*/

        headerList!!.add(
            DashboardHeaderMenuModel(
                getString(R.string.myDownloads),
                false
            )
        )

        headerList!!.add(
            DashboardHeaderMenuModel(
                getString(R.string.paymentandbilling),
                false
            )
        )

        headerList!!.add(
            DashboardHeaderMenuModel(
                getString(R.string.manageDevices),
                false
            )
        )
/*

        headerList!!.add(
            DashboardHeaderMenuModel(
                getString(R.string.offers),
                false
            )
        )
*/

        headerList!!.add(
            DashboardHeaderMenuModel(
                getString(R.string.settings),
                false
            )
        )

        headerList!!.add(
            DashboardHeaderMenuModel(
                "FAQ",
                false
            )
        )

        headerList!!.add(
            DashboardHeaderMenuModel(
                getString(R.string.help),
                false
            )
        )

        headerList!!.add(
            DashboardHeaderMenuModel(
                getString(R.string.contactUs),
                false
            )
        )

        headerList!!.add(
            DashboardHeaderMenuModel(
                getString(R.string.logout),
                false
            )
        )


        /*Navigation header E-Wallet child list item*/
        val eWallet: MutableList<String> =
            ArrayList()
        eWallet.add(getString(R.string.myWallet))
        eWallet.add(getString(R.string.transactionHistory))
        eWallet.add(getString(R.string.send))
        eWallet.add(getString(R.string.receive))
        eWallet.add(getString(R.string.stackingWallet))


        /*Navigation header teamReport child list item*/
        val teamReport: MutableList<String> =
            ArrayList()
        teamReport.add("Tier1")
        teamReport.add("Tier2")

        listDataChild = HashMap()
        listDataChild!![headerList!![0].title] = ArrayList()
        listDataChild!![headerList!![1].title] = ArrayList()
        listDataChild!![headerList!![2].title] = ArrayList()
        listDataChild!![headerList!![3].title] = ArrayList()
        listDataChild!![headerList!![4].title] = ArrayList()
        listDataChild!![headerList!![5].title] = ArrayList()
        listDataChild!![headerList!![6].title] = ArrayList()
        listDataChild!![headerList!![7].title] = ArrayList()
        listDataChild!![headerList!![8].title] = ArrayList()
        //     listDataChild!![headerList!![9].title] = ArrayList()
        //listDataChild!![headerList!![10].title] = ArrayList()

        adapter = NavExpandableListAdapter(
            this,
            headerList as ArrayList<DashboardHeaderMenuModel>,
            listDataChild!!, userPref1.getNotificationdot()
        )
        binding.elvMenu.setAdapter(adapter)
    }

    private fun navMenuClickListener() {
        binding.header.ivUser.setOnClickListener { view ->
            startActivity(Intent(this, ProfileActivity::class.java))
            closeDrawer()
        }

        binding.elvMenu.setOnGroupClickListener { _, _, clickOnPosition, l ->
            if (listDataChild!![headerList!![clickOnPosition.toInt()].title]!!.isEmpty()) {
                when (clickOnPosition) {
                    0 -> {
                        /* val bundle = Bundle()
                        mFragment = CovidHomeFragment()
                        bundle.putInt("place", 0)
                        mFragment!!.arguments = bundle
                        CommonUtils.setFragment(mFragment, false,this, R.id.frameContainer)*/
                        startActivity(Intent(this, NotificationsActivity::class.java))

                    }
                    /* 1 -> {
                         startActivity(Intent(this, MyListActivity::class.java))
                     }*/


                    1 -> {
                        startActivity(Intent(this, MyDownloadActivity::class.java))
                    }

                    //Activity log
                    2 -> {
                        //PaymentBillingActivity
                        startActivity(Intent(this, PaymentBillingActivity::class.java))
                    }


                    3 -> {
                        startActivity(Intent(this, ManageDevicesActivity::class.java))
                    }

                    /*    4 -> {

                            startActivity(Intent(this, Offers::class.java) .putExtra("value",0))


                        }*/
                    4 -> {

                        startActivity(Intent(this, SettingsActivity::class.java))

                    }

                    5 -> {
                        /* val bundle = Bundle()
                        mFragment = CovidHomeFragment()
                        bundle.putInt("place", 4)
                        mFragment!!.arguments = bundle
                        CommonUtils.setFragment(mFragment, false,this, R.id.frameContainer)*/
                        startActivity(Intent(this, FaqActivity::class.java))
                        //FaqActivity
                    }

                    6 -> {
                        startActivity(Intent(this, HelpActivity::class.java))

                    }

                    7 -> {
                        startActivity(Intent(this, ContacUsActivity::class.java))
                    }

                    8 -> {
                        closeDrawer()

                        logoutAlert()
                    }

                }
                closeDrawer()
            }
            false
        }


        binding.elvMenu.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->


            when (groupPosition) {

            }
            false
        }
    }

    private fun logoutAlert() {
        var cDialog = Dialog(this, R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogCustomBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_custom,
            null,
            false
        )
        cDialog.setContentView(bindingDialog.root)
        cDialog.setCancelable(false)
        cDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //}

        cDialog.show()
        bindingDialog.tvTitle.text = getString(R.string.logout)
        bindingDialog.tvMsg.text = getString(R.string.logoutConfirmation)

        bindingDialog.btOk.setOnClickListener {
            cDialog.dismiss()
            if (userPref1.getLoginType() == "2") {
                googleSignOut()

            } else if (userPref1.getLoginType() == "3") {
                facebookLogout()
//                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
//                val googleSignInClient = GoogleSignIn.getClient(this@HomeActivity, gso)
//                googleSignInClient.signOut()
            }
            startActivity(Intent(this, LoginActivity::class.java))
            userPref1.clearPref()
//            callLogoutApi()

        }

        bindingDialog.btCancel.setOnClickListener {
            cDialog.dismiss()
        }


    }

    private fun googleSignOut() {
        mGoogleSignInClient!!.signOut().addOnCompleteListener(
            this,
            object : OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    val gso =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    val googleSignInClient = GoogleSignIn.getClient(this@HomeActivity2, gso)
                    googleSignInClient.signOut()
                }
            })
    }

    private fun facebookLogout() {
        LoginManager.getInstance().logOut()
    }

    private fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

//    private fun callLogoutApi() {
//        apiService.callLogoutApi("Bearer " + userPref1.getToken(), userPref1.getFcmToken()!!)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe(this::showProgressDialog)
//            .doOnCompleted(this::hideProgressDialog)
//            .subscribe({ commonResponse ->
//                if (commonResponse.status != 0) {
//                    userPref.clearPref()
//                    startActivity(Intent(this, LoginActivity::class.java))
//                    val gso =
//                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
//                    val googleSignInClient = GoogleSignIn.getClient(this@HomeActivity2, gso)
//                    googleSignInClient.signOut()
//                    finishAffinity()
//                } else {
//                    utils.simpleAlert(
//                        this,
//                        "Error",
//                        commonResponse.message.toString()
//                    )
//                    hideProgressDialog()
//                }
//
//            }, { throwable ->
//                hideProgressDialog()
//                if (throwable is ConnectException) {
//                    utils.simpleAlert(
//                        this,
//                        "Error",
//                        getString(R.string.check_network_connection)
//                    )
//
//                } else {
//
//                    utils.simpleAlert(
//                        this,
//                        "Error",
//                        throwable.message.toString()
//                    )
//
//
//                }
//            })
//    }

}
