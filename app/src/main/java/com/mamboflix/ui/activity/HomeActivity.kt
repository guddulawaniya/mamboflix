package com.mamboflix.ui.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityDashboardBinding
import com.mamboflix.databinding.DialogCustomBinding
import com.mamboflix.model.DashboardHeaderMenuModel
import com.mamboflix.ui.Constants
import com.mamboflix.ui.activity.login.LoginActivity
import com.mamboflix.ui.activity.managedevices.ManageDevicesActivity
import com.mamboflix.ui.activity.payment.PaymentBillingActivity
import com.mamboflix.ui.activity.profile.ProfileActivity
import com.mamboflix.ui.activity.settings.SettingsActivity
import com.mamboflix.ui.adapter.NavExpandableListAdapter
import com.mamboflix.ui.fragment.*
import com.mamboflix.utils.AppConstant
import com.mamboflix.utils.CommonUtils
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.*


@Suppress("UNREACHABLE_CODE")
open class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {
    override fun onClick(v: View?) {

    }
    private var mContext: Context? = null
    lateinit var binding: ActivityDashboardBinding
    lateinit var mfragment: Fragment
    private lateinit var adapter: NavExpandableListAdapter
    private var headerList: MutableList<DashboardHeaderMenuModel>? = null
    private var listDataChild: HashMap<String, List<String>>? = null
    private var exit: Boolean = false
    private var mGoogleSignInClient: GoogleSignInClient? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor = Color.BLACK
        }*/

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        mContext = this


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }*/

        /*StatusBarUtil.setTransparent(this)*/

        initGoogleSignIn()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onStart() {
        super.onStart()
        binding.llAppBarDashboard.bottomNav.menu.clear()
        setNavigationData()
        setNavigationBar()
        initializeDriversBnv()
        getPlanDetail()

    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun setNavigationBar() {
        /*setSupportActionBar(binding.llAppBarDashboard?.dashboardToolbar)*/
        mfragment = DashboardFragment()
        CommonUtils.setFragment(mfragment, false, this, R.id.frameContainer)
        /* title = "Home"*/
        setBottomNavigation()
    }


    override fun onResume() {
        super.onResume()

        setNavigationData()
    }

    override fun onRestart() {
        super.onRestart()

        setNavigationData()

    }

    override fun onResumeFragments() {
        super.onResumeFragments()

        setNavigationData()

        getUserProfileDetail()

    }

    fun setBottomNavigation() {
        binding.llAppBarDashboard.bottomNav!!.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

    }

    /*logoutAlert() is used to show alert for logout*/
    private fun logoutAlert() {
        var cDialog = Dialog(this, R.style.Theme_Tasker_Dialog)
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
        bindingDialog.tvTitle.text = getString(R.string.logout)
        bindingDialog.tvMsg.text = getString(R.string.logoutConfirmation)

        bindingDialog.btOk.setOnClickListener {
            cDialog!!.dismiss()
            if (userPref.getLoginType() == "2") {
                googleSignOut()

            } else if (userPref.getLoginType() == "3") {
                facebookLogout()
//                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
//                val googleSignInClient = GoogleSignIn.getClient(this@HomeActivity, gso)
//                googleSignInClient.signOut()
            }
            callLogoutApi()

        }

        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
        }


        /*  val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
          builder.setTitle(getString(R.string.logout))
          builder.setMessage(getString(R.string.logoutConfirmation))
          builder.setPositiveButton("Yes") { dialogInterface, i ->
              *//*startActivity(Intent(this, LoginActivity::class.java))
            finish()*//*
            if(userPref.getLoginType()=="2"){
                googleSignOut()
            }
            else if(userPref.getLoginType()=="3"){
                facebookLogout()
            }
            callLogoutApi()
        }
        builder.setNegativeButton("No", null)
        builder.create()
        builder.show()*/
    }

    private fun setNavigationData() {
        binding.header.tvName0.text = "Welcome  " + userPref.getSubUserName()

//        if (!userPref.getsubuserImg() .isNullOrBlank()) {
//            Glide.with(this).load(Uri.parse(userPref.getsubuserImg()))
//                .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
//                .apply(RequestOptions.errorOf(R.drawable.user_profile))
//                .into(binding.header.ivUser)
//            Log.d("TAG", "setNavigationData: "+"Not null")
//        }
        if (!userPref.user.profile_image.isNullOrBlank()) {
            Glide.with(this).load(Uri.parse(userPref.user.profile_image))
                .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
                .apply(RequestOptions.errorOf(R.drawable.user_profile))
                .into(binding.header.ivUser)
            Log.d("TAG", "setNavigationData: " + "Not null")
        }
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
            listDataChild!!, userPref.getNotificationdot()
        )
        binding.elvMenu.setAdapter(adapter)
    }


    private fun navMenuClickListener() {
        binding.header.llHeader.setOnClickListener { view ->
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

    /*closeDrawer() is used to close notification drawer*/
    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun openDrawer() {
//        if (!userPref.getsubuserImg().isNullOrBlank()) {
//            Glide.with(this).load(Uri.parse(userPref.getsubuserImg()))
//                .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
//                .apply(RequestOptions.errorOf(R.drawable.user_profile))
//                .into(binding.header.ivUser)
//        }
//        if (!userPref.user.profile_image.isNullOrBlank()) {
//            Glide.with(this).load(Uri.parse(userPref.user.profile_image))
//                .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
//                .apply(RequestOptions.errorOf(R.drawable.user_profile))
//                .into(binding.header.ivUser)
//        }
//
//        binding.header.tvName0.text = "Welcome  " + userPref.getSubUserName()
        binding.drawerLayout.openDrawer(GravityCompat.START)


    }

    private fun getPlanDetail() {
        apiService.callplandetails("Bearer " + userPref.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    binding.packagename.visibility = VISIBLE
                    binding.aa.visibility = VISIBLE
                    //   binding.valid.visibility=VISIBLE
                    binding.expiredon.visibility = VISIBLE
                    binding.v2.visibility = VISIBLE
                    /*   val params = FrameLayout.LayoutParams(
                           FrameLayout.LayoutParams.MATCH_PARENT,
                           FrameLayout.LayoutParams.MATCH_PARENT
                       )
                       params.setMargins(0, 160, 0, 0)
                       binding.llexpand.setLayoutParams(params)
   */
                    binding.packagename.text =
                        getString(R.string.SubscriptionPlan) + "- " + commonResponse.mdata.package_name
                    binding.valid.text =
                        getString(R.string.validupto) + "- " + commonResponse.mdata.counter + " " + getString(
                            R.string.days
                        )
                    binding.expiredon.text =
                        getString(R.string.expired_on) + "- " + commonResponse.mdata.expity_date
                } else {
                    /*  val params = FrameLayout.LayoutParams(
                          FrameLayout.LayoutParams.MATCH_PARENT,
                          FrameLayout.LayoutParams.MATCH_PARENT
                      )
                      params.setMargins(0, 160, 0, 0)
                      binding.llexpand.setLayoutParams(params)*/
                    binding.packagename.visibility = GONE
                    binding.valid.visibility = GONE
                    binding.expiredon.visibility = GONE

                    binding.v2.visibility = GONE
                }

            }, { throwable ->
                //hideProgressDialog()
                if (throwable is ConnectException) {

                } else {
                }

            })
    }

    private fun getUserProfileDetail() {
        apiService.userData("Bearer " + userPref.getToken(), userPref.getFcmToken().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ commonResponse ->

                if (commonResponse.status != 0 && commonResponse.user_data != null) {

                    binding.header.tvName0.text = "Welcome  " + commonResponse.user_data.name

                    Glide.with(this).load(Uri.parse(commonResponse.user_data.profile_image))
                        .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
                        .apply(RequestOptions.errorOf(R.drawable.user_profile))
                        .into(binding.header.ivUser)

//                    Toast.makeText(this, " success ", Toast.LENGTH_SHORT).show()
                }

            }, { throwable ->
                //hideProgressDialog()
                if (throwable is ConnectException) {

                } else {
                }

            })
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("KeyEvent", "Back key detected")
            showExitDialog()
            return true // Event handled
        }
        return super.onKeyDown(keyCode, event)
    }



    override fun onBackPressed() {
        // calling the function
        showExitDialog()
    }

    private fun showExitDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Do you really want to exit?")
        builder.setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
            dialog.dismiss() // Dismiss the dialog
            finishAffinity() // Close all activities and exit
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.dismiss() // Just dismiss the dialog
        }

        val dialog = builder.create()
        dialog.show()
    }

    /* override fun onBackPressed() {
         if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
             closeDrawer()
             binding.llAppBarDashboard.bottomNav.menu.getItem(0).isChecked = true
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
                         utils.toaster(getString(R.string.pressbackagain))
                         exit = true

                     }, 3 * 100)
                 }
             } else {
                 binding.llAppBarDashboard.bottomNav.menu.getItem(0).isChecked = true
                 mfragment = DashboardFragment()
                 CommonUtils.setFragment(mfragment, false, this, R.id.frameContainer)
             }
         }

         //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         *//* if (binding.llAppBarDashboard.bottomNav.menu.getItem(AppConstant.tabIndex).itemId!=Constants.DriverDashboardBottomNavTab.HOME) {
             binding.llAppBarDashboard.bottomNav.menu.getItem(0).isChecked = true
             mfragment = DashboardFragment()
             CommonUtils.setFragment(mfragment, false, this, R.id.frameContainer)
         } else {
             if (mfragment is DashboardFragment) {
                 *//**//*(this as AppCompatActivity).supportActionBar?.show()
                CommonUtils.exitDialog(this)*//**//*
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

        }*//*



        fun onBack() {
            supportFragmentManager.popBackStack()
//        mfragment = null
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        /* menuInflater.inflate(R.menu.notification, menu)
         val alertMenuItem = menu!!.findItem(R.id.activity_main_alerts_menu_item)
         val rootView = alertMenuItem!!.actionView as FrameLayout
         countTextView = rootView.findViewById(R.id.view_alert_count_textview)
         rootView.setOnClickListener {
              val fragment = NotificationFragment()
              CommonUtils.setFragment(fragment, false, this@HomeActivity, R.id.frameContainer)
         }*/
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun changeIcon(b: Boolean) {
        /*if (b) {
            Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
            //toggle!!.setHomeAsUpIndicator(R.drawable.toggle_bar)
            toggle!!.isDrawerIndicatorEnabled = true
            toggle!!.syncState()
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            toggle!!.syncState()

        } else {
            Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
            toggle!!.isDrawerIndicatorEnabled = false
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            toggle!!.setHomeAsUpIndicator(R.drawable.back_icon)
            //binding.llAppBarDashboard.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        }
*/

    }

    fun fragmenttitle(name: String, searchbar: Boolean) {
        /*if (searchbar) {
            binding.llAppBarDashboard.toolbar.search_layout.visibility = View.VISIBLE
        } else {
            binding.llAppBarDashboard.toolbar.search_layout.visibility = View.GONE
        }*/

        //binding.llAppBarDashboard.dashboardToolbar.title = name

    }


    open fun initializeDriversBnv() {
        AddMenusInBnv(
            Menu.NONE, Constants.DriverDashboardBottomNavTab.HOME,
            1, resources.getString(R.string.dashboardHomeTab),
            R.drawable.ic_home_dark
        )
        AddMenusInBnv(
            Menu.NONE, Constants.DriverDashboardBottomNavTab.MOVIES,
            2, resources.getString(R.string.dashboardMoviesTab),
            R.drawable.ic_movies_dark
        )
        AddMenusInBnv(
            Menu.NONE, Constants.DriverDashboardBottomNavTab.TV,
            3, resources.getString(R.string.dashboardTvTab),
            R.drawable.ic_tv_dark
        )
        AddMenusInBnv(
            Menu.NONE, Constants.DriverDashboardBottomNavTab.LIST,
            4, resources.getString(R.string.dashboardListTab),
            R.drawable.ic_list_dark
        )
        AddMenusInBnv(
            Menu.NONE,
            Constants.DriverDashboardBottomNavTab.MORE,
            5,
            resources.getString(R.string.dashboardMoreTab),
            R.drawable.ic_more_dark
        )
        //SwitchFragment(Constants.DriverDashboardBottomNavTab.HOME)
    }


    private fun SwitchFragment(tab: Int) {
        when (tab) {
            Constants.DriverDashboardBottomNavTab.HOME -> {
                AppConstant.tabIndex = 0
                userPref.set_check_api("1")
                mfragment = DashboardFragment()
                CommonUtils.setFragment(mfragment, false, this, R.id.frameContainer)
            }
            Constants.DriverDashboardBottomNavTab.MOVIES -> {
                AppConstant.tabIndex = 1
                userPref.set_check_api("2")
                mfragment = MoviesTabFragment()
                CommonUtils.setFragment(mfragment, false, this, R.id.frameContainer)
            }
            Constants.DriverDashboardBottomNavTab.TV -> {
                AppConstant.tabIndex = 2
                userPref.set_check_api("3")
                mfragment = TvShowsFragment()
                CommonUtils.setFragment(mfragment, false, this, R.id.frameContainer)
            }
            Constants.DriverDashboardBottomNavTab.LIST -> {
                AppConstant.tabIndex = 3
                userPref.set_check_api("4")
                mfragment = MyListFragment()
                CommonUtils.setFragment(mfragment, false, this, R.id.frameContainer)
            }
            Constants.DriverDashboardBottomNavTab.MORE -> {
                AppConstant.tabIndex = 4
                mfragment = DashboardFragment()
                CommonUtils.setFragment(mfragment, false, this, R.id.frameContainer)

            }

        }
    }


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                Constants.DriverDashboardBottomNavTab.HOME -> {
                    redirectToTodayTab(0)
                    if (mfragment is DashboardFragment) {

                    } else {
                        SwitchFragment(Constants.DriverDashboardBottomNavTab.HOME)
                        return@OnNavigationItemSelectedListener true
                    }

                }
                Constants.DriverDashboardBottomNavTab.MOVIES -> {
                    redirectToTodayTab(1)
                    if (mfragment is MoviesTabFragment) {

                    } else {
                        SwitchFragment(Constants.DriverDashboardBottomNavTab.MOVIES)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                Constants.DriverDashboardBottomNavTab.TV -> {
                    redirectToTodayTab(2)
                    if (mfragment is TvShowsFragment) {

                    } else {
                        SwitchFragment(Constants.DriverDashboardBottomNavTab.TV)
                        return@OnNavigationItemSelectedListener true
                    }
                }

                Constants.DriverDashboardBottomNavTab.LIST -> {
                    redirectToTodayTab(3)
                    if (mfragment is MyListFragment) {

                    } else {
                        SwitchFragment(Constants.DriverDashboardBottomNavTab.LIST)
                        return@OnNavigationItemSelectedListener true
                    }
                }

                Constants.DriverDashboardBottomNavTab.MORE -> {
                    redirectToTodayTab(4)
                    setNavigationData()
                    openDrawer()
                    getUserProfileDetail()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    fun AddMenusInBnv(
        groupId: Int,
        menuId: Int,
        order: Int,
        menuTitle: String,
        icon: Int
    ) {
        var menu: Menu = binding.llAppBarDashboard.bottomNav!!.menu
        menu.add(groupId, menuId, order, menuTitle)
        menu.findItem(menuId).setIcon(icon)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return true
    }


    private fun callLogoutApi() {
        apiService.callLogoutApi("Bearer " + userPref.getToken(), userPref.getFcmToken()!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
                    userPref.clearPref()
                    startActivity(Intent(this, LoginActivity::class.java))
                    val gso =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    val googleSignInClient = GoogleSignIn.getClient(this@HomeActivity, gso)
                    googleSignInClient.signOut()
                    finishAffinity()
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
                        throwable.message.toString()
                    )


                }
            })
    }


    private fun initGoogleSignIn() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.server_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun googleSignOut() {
        mGoogleSignInClient!!.signOut().addOnCompleteListener(
            this,
            object : OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    val gso =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    val googleSignInClient = GoogleSignIn.getClient(this@HomeActivity, gso)
                    googleSignInClient.signOut()
                }
            })

    }

    private fun facebookLogout() {
        LoginManager.getInstance().logOut()
    }


    fun redirectToTodayTab(tab: Int) {
//        SwitchFragment(tab)
//        val tabId = Constants.AdminDashboardBottomNavTab.TODAY
        //binding.llAppBarDashboard.bottomNav.setSelectedItemId(tab)
        binding.llAppBarDashboard.bottomNav.menu.getItem(tab).isChecked = true
    }

}



