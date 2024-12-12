package com.mamboflix.HomeActivity3

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.login.LoginManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityHome3Binding
import com.mamboflix.databinding.DialogCustomBinding
import com.mamboflix.databinding.DialogSeassionOutBinding
import com.mamboflix.databinding.InfoBottomSheetBinding
import com.mamboflix.databinding.LayoutPopUpCategoryBinding
import com.mamboflix.model.DashboardHeaderMenuModel
import com.mamboflix.model.MoviesAndShowsCategory
import com.mamboflix.model.PreviewMovieTabModelClass
import com.mamboflix.model.SubtitleModel
import com.mamboflix.model.hometab.CategoryContentModel
import com.mamboflix.model.hometab.MamboOriginalmODEL
import com.mamboflix.model.hometab.becauseyou.BecauseYouContentModel
import com.mamboflix.model.hometab.continuewatch.ContinueWatchModel
import com.mamboflix.model.hometab.mostwatched.MostWatchedModel
import com.mamboflix.model.hometab.mylist.MyListModel
import com.mamboflix.model.hometab.preview.PreviewModel
import com.mamboflix.model.hometab.topbanner.TopBannerContentModel
import com.mamboflix.model.hometab.topbanner.TopBannerModel
import com.mamboflix.model.hometab.trending.TrendingModel
import com.mamboflix.model.movieTabModel.CategoryContentMovieTabModel
import com.mamboflix.prefs.UserPref
import com.mamboflix.ui.activity.ContacUsActivity
import com.mamboflix.ui.activity.FaqActivity
import com.mamboflix.ui.activity.HelpActivity
import com.mamboflix.ui.activity.MyDownloadActivity
import com.mamboflix.ui.activity.NotificationsActivity
import com.mamboflix.ui.activity.contentdetails.ContentDetailsActivity
import com.mamboflix.ui.activity.filter.FilterActivity
import com.mamboflix.ui.activity.login.LoginActivity
import com.mamboflix.ui.activity.managedevices.ManageDevicesActivity
import com.mamboflix.ui.activity.payment.PaymentBillingActivity
import com.mamboflix.ui.activity.player.PlayerActivity
import com.mamboflix.ui.activity.profile.ProfileActivity
import com.mamboflix.ui.activity.purchsedhistory.PurchasedHistory
import com.mamboflix.ui.activity.search.SearchActivity
import com.mamboflix.ui.activity.settings.SettingsActivity
import com.mamboflix.ui.adapter.NavExpandableListAdapter
import com.mamboflix.ui.adapter.categories.CategoriesSpinnerAdapter
import com.mamboflix.ui.adapter.dashboard.BecauseYouHomeAdapter
import com.mamboflix.ui.adapter.dashboard.ContinueWarchingAdapter
import com.mamboflix.ui.adapter.dashboard.DashboardMovieTab.MovieAllAdapterMovieTab
import com.mamboflix.ui.adapter.dashboard.DashboardMovieTab.PreviewsMoviewTabAdapter
import com.mamboflix.ui.adapter.dashboard.MostWatchedHomeAdapter
import com.mamboflix.ui.adapter.dashboard.MovieAllAdapter
import com.mamboflix.ui.adapter.dashboard.MyListHomeAdapter
import com.mamboflix.ui.adapter.dashboard.PreviewsAdapter
import com.mamboflix.ui.adapter.dashboard.TrendingNowHomeAdapter
import com.mamboflix.ui.adapter.mylist.MyListAdapter
import com.mamboflix.utils.CommonUtils
import com.mamboflix.utils.isNetworkAvailable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.Locale

@Suppress("UNREACHABLE_CODE")
class HomeActivity3 : BaseActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private var headerList: MutableList<DashboardHeaderMenuModel>? = null
    private var listDataChild: HashMap<String, List<String>>? = null
    private lateinit var adapter: NavExpandableListAdapter

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var layoutManager1: RecyclerView.LayoutManager? = null
    private var layoutManager0: RecyclerView.LayoutManager? = null
    private var layoutManager2: RecyclerView.LayoutManager? = null
    private var layoutManager3: RecyclerView.LayoutManager? = null
    private var layoutManager4: RecyclerView.LayoutManager? = null
    private var layoutManager5: RecyclerView.LayoutManager? = null
    private var layoutManager6: RecyclerView.LayoutManager? = null
    private var previewsAdapter: PreviewsAdapter? = null
    private var continueWatchingAdapter: ContinueWarchingAdapter? = null
    private var MomboOriginalAdapter: MomboOriginalAdapterpackage? = null
    private var movieAllAdapter: MovieAllAdapter? = null
    private var myListHomeAdapter: MyListHomeAdapter? = null
    private var becauseYouHomeAdapter: BecauseYouHomeAdapter? = null
    private var trendingNowHomeAdapter: TrendingNowHomeAdapter? = null
    private var mostWatchedHomeAdapter: MostWatchedHomeAdapter? = null
    private var catgorie_content: ArrayList<CategoryContentModel>? = null
    private var continue_watch: ArrayList<ContinueWatchModel>? = null
    private var last_based_content: ArrayList<BecauseYouContentModel>? = null
    var most_watched: ArrayList<MostWatchedModel>? = null
    var mylist: ArrayList<MyListModel>? = null
    var preview: ArrayList<PreviewModel>? = null
    var trending: ArrayList<TrendingModel>? = null
    var mambooriginal: ArrayList<MamboOriginalmODEL>? = null
    var topbanner = ""
    private var bannerUrl: String = ""
    private var click_value: String = ""
    private var topBannerModel: TopBannerContentModel? = null
    private var bannerMain: TopBannerModel? = null
    var isAddList: Boolean = false
    lateinit var userPref1: UserPref
    lateinit var exoPlayer: ExoPlayer
    //Moview Tab
    var previewsAdapter_movie: PreviewsMoviewTabAdapter? = null
    var movieAllAdapter_movie: MovieAllAdapterMovieTab? = null
    var layoutManager_movie: RecyclerView.LayoutManager? = null
    var catgorie_content_movie: ArrayList<CategoryContentMovieTabModel>? = null
    var preview_movie_movie: ArrayList<PreviewMovieTabModelClass>? = null
    var isAddList_movie: Boolean = false
    //    Tv Show
    var previewsAdapter_tv_show: PreviewsAdapter? = null
    var movieAllAdapter_tv_show: MovieAllAdapterTvShow? = null
    var categoryList_movie: ArrayList<MoviesAndShowsCategory>? = null
    var catgorie_content_tv_show: ArrayList<CategoryContentModel>? = null
    var preview_tv_show: ArrayList<PreviewModel>? = null

    var isAddList_tv_show: Boolean = false
    var categoryAdapter: CategoriesSpinnerAdapter? = null
    var categoryList_tv_show: ArrayList<MoviesAndShowsCategory>? = null
    var dialogCategory: Dialog? = null
    var dialogCategory1: Dialog? = null

    //    My List
    var myListAdapter: MyListAdapter? = null
    var myList_list_tab: ArrayList<com.mamboflix.model.mylist.MyListModel>? = null
    var homebannerid: String = ""
    var homebannerCatid: String = ""
    var homemovieid: String = ""
    var homemovieCatid: String = ""
    var homemovietvid: String = ""
    var homemovietvcatid: String = ""
    var newMember: String = ""

    private var tabPosition = 0
    //    lateinit var apiService: ApiService
    var videoURL =
        "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"

    private var subTitleList: ArrayList<SubtitleModel>? = null

    private lateinit var binding: ActivityHome3Binding

    private var tabPositionArray = ArrayList<Int>()

    var bannerUrl_movie: String = ""
    var topBannerModel_movie: TopBannerContentModel? = null
    var bannerMain_movie: TopBannerModel? = null
    var categoryAdapter_movie: CategoriesSpinnerAdapter? = null
    var dialogCategory_movie: Dialog? = null
    var layoutManager_tv_show: RecyclerView.LayoutManager? = null
    var bannerUrl_tv_show: String = ""
    var topBannerModel_tv_show: TopBannerContentModel? = null
    var bannerMain_tv_show: TopBannerModel? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ActivityLifecycle", "onCreate called")
        binding = ActivityHome3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref1 = UserPref(this)
        userPref1.setset_value("2")
        tabPositionArray.add(0)

        Log.d("usertoken",userPref.getToken().toString())

        Log.d("tab_postion", tabPositionArray.toString())
        Log.d("tab_postion_last_index", tabPositionArray.last().toString())

        if (intent != null) {
            newMember = intent.getStringExtra("NEW_MAMBER").toString()
        }
        getCountryName(this)
        if (isNetworkAvailable(this)) {
        } else {
            val intent = Intent(this, MyDownloadActivity::class.java)
            startActivity(intent)
        }
        allclick()
        setNavigationData()
        home_api()
        //Movie Tab
        catgorie_content_movie = ArrayList()
        preview_movie_movie = ArrayList()
        categoryList_movie = ArrayList()
        callGetMovieApi()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

//        TV Show
        catgorie_content_tv_show = ArrayList()
        preview_tv_show = ArrayList()
        categoryList_tv_show = ArrayList()
        callGetTvShowsApi()
        setRecyclerview_tv_show()
        setUpSpinner_tv_show()
        setUpSpinner()

//        My List Tab
        myList_list_tab = ArrayList()
        callMyListAPI()
        setRecyclerview_my_list()


        //get Read Notification
        getReadNotification("1")
        requestStoragePermission()

    }



    private fun requestStoragePermission() {
        val permissions = mutableListOf<String>()
        permissions.add(Manifest.permission.CAMERA)  // Always add CAMERA permission
        permissions.add(Manifest.permission.POST_NOTIFICATIONS)

        // Add media permissions for Android 14 and below
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)  // Read images permission
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)   // Read videos permission
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }


        // Request permissions with Dexter
        Dexter.withActivity(this)
            .withPermissions(*permissions.toTypedArray())
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        // If permissions are granted, proceed with camera or gallery

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest() // Allow user to continue permission request
                }
            })
            .withErrorListener { error ->
                // Handle error if permission request fails
                Toast.makeText(this, "Error occurred while requesting permissions!", Toast.LENGTH_SHORT).show()
            }
            .onSameThread()  // Ensure it runs on the same thread to avoid async issues
            .check()
    }



    override fun onBackPressed() {
        Log.d("onbackpress","back button press ")
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            if (userPref1.getset_value() == "1") {
                super.onBackPressed()
                showExitDialog()
                //finish() // finish activity
            } else {
                Handler().postDelayed({
                    userPref1.setset_value("0")
                    utils.toaster(getString(R.string.pressbackagain))
                }, 3 * 100)
            }
        }
    }



    override fun onResume() {
        Log.d("Lifecycle", "onResume called")
        super.onResume()
        binding.llLayout.visibility = View.GONE
        home_api()
        callGetTvShowsApi()
        callMyListAPI()
    }

    private fun setRecyclerview_tv_show() {
        //Previews
        layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        previewsAdapter = PreviewsAdapter(applicationContext, preview_tv_show!!)

        binding.rvPreviewsTvShow.layoutManager = layoutManager
        binding.rvPreviewsTvShow.adapter = previewsAdapter

        previewsAdapter!!.setOnItemClickListener(object : PreviewsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {

//                startActivity(
//                    Intent(applicationContext, ContentDetailsActivity::class.java)
//                        .putExtra("content_id", continue_watch!![position].content_id)
//                        .putExtra("watch_time", continue_watch!![position].watch_duration)
//                        .putExtra("episode_id", continue_watch!![position].episode_id)
//                        .putExtra("fragment_type", click_value)
//                )

                Log.d("dataofpreview","content_id"+preview_tv_show!![position].content_id)
                Log.d("dataofpreview","content_id"+preview_tv_show!![position].content_id)
                Log.d("dataofpreview","content_id"+preview_tv_show!![position].content_id)
                Log.d("dataofpreview","content_id"+preview_tv_show!![position].content_id)
                startActivity(
                    Intent(applicationContext, PlayerActivity::class.java)
                        .putExtra("content_id", preview_tv_show!![position].content_id)
                        .putExtra("watch_time", "0")
                        .putExtra("name", preview_tv_show!![position].preview_content!!.title)
                        .putExtra(
                            "playUrl",
                            preview_tv_show!![position].preview_content!!.trailer_path
                        )
                )
            }
        })

        //MovieAllAdapter
        layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        movieAllAdapter_tv_show =
            MovieAllAdapterTvShow(applicationContext, catgorie_content_tv_show!!, "tv_show")
        binding.rvAllTvShow.layoutManager = layoutManager
        binding.rvAllTvShow.adapter = movieAllAdapter_tv_show
        movieAllAdapter_tv_show!!.notifyDataSetChanged()
        movieAllAdapter!!.setOnItemClickListener(object : MovieAllAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
            }
        })
    }

    private fun setUpSpinner_tv_show() {
        Log.d("categorydata",categoryList_tv_show.toString())
        binding.tvEthnicityTypeTvShow.text = getString(R.string.category_select)
        val dialogView = LayoutInflater.from(this@HomeActivity3)
            .inflate(R.layout.layout_pop_up_category, null, false)
        val dialogBinding: LayoutPopUpCategoryBinding = DataBindingUtil.bind(dialogView)!!
        dialogCategory = CommonUtils.createCustomDialog(this@HomeActivity3, dialogBinding.root)
//        categoryList_tv_show?.clear()
        categoryAdapter = CategoriesSpinnerAdapter(applicationContext, categoryList_tv_show!!)
        dialogBinding.rvCategory.adapter = categoryAdapter
        categoryAdapter!!.setOnItemClickListener(object :
            CategoriesSpinnerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                if (position > 0) {
                    val model = categoryList_tv_show!![position]
                    val intent = Intent(applicationContext, FilterActivity::class.java)
                    intent.putExtra("categoryName", model.title)
                    intent.putExtra("cat_id", model.id.toString())
                    intent.putExtra("type", "2")
                    startActivity(intent)
                }
                dialogCategory!!.dismiss()
            }
        })
    }

    private fun callGetTvShowsApi() {
        apiService.callGetShowApi(
            "Bearer " + userPref.getToken(),
            userPref.getSubUserId().toString(),
            userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage(),
            getCountryCode(getCountryName(this))!!
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    hideProgressDialog()
//                    commonResponse.mdata.main_data.top_banner?.let { setBannerData_tv_show(it) }
                    binding.listLytTvShow.visibility = View.VISIBLE

                    Log.d("categorydata","api : "+commonResponse.mdata)
                    if (commonResponse.mdata.main_data.top_banner != null) {
                        val bannerimage =
                            commonResponse.mdata.main_data.top_banner?.bnner_content!!.poster
                        Glide.with(this)
                            .load(Uri.parse(bannerimage))
                            /*.apply(RequestOptions.fitCenterTransform())*/
                            .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
                            .apply(RequestOptions.errorOf(R.drawable.image_loading))
                            .into(binding.ivBannerTvShow)
                        topBannerModel = commonResponse.mdata.main_data.top_banner?.bnner_content
                        bannerUrl = commonResponse.mdata.main_data.top_banner?.bnner_content.path!!
                        binding.tvMovieTvShow.text =
                            commonResponse.mdata.main_data.top_banner?.bnner_content.title
                        binding.tvYearTvShow.text =
                            commonResponse.mdata.main_data.top_banner?.bnner_content.release_year
                        binding.tvTimeTvShow.text =
                            commonResponse.mdata.main_data.top_banner?.bnner_content.content_duration
                        binding.tvCategoryTvShow.text = commonResponse.mdata.main_data.top_banner?.title
                        binding.tvCertificatTvShow.text = "U/A"
                        homemovietvid = topBannerModel!!.id.toString()
                        homemovietvcatid = topBannerModel!!.cat_id.toString()
                        if (commonResponse.mdata.main_data.top_banner.bnner_content.my_list_status == "1") {
                            isAddList_tv_show = true
                            binding.ivMyListTvShow.setImageResource(R.drawable.ic_check_mark_blue)
                            //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
                        } else {
                            binding.ivMyListTvShow.setImageResource(R.drawable.add_list)
                            //binding!!.ivMyList.colorFilter = null
                        }
                    }

                    /* val view: View =
                         layoutInflater.inflate(R.layout.info_bottom_sheet, null)*/

                    binding.lytInfoTvShow.setOnClickListener {
                        setInfo(commonResponse.mdata.main_data.top_banner!!)
                    }

                    if (commonResponse.mdata.main_data.preview != null && commonResponse.mdata.main_data.preview.size > 0) {
                        binding.lytPreviewTvShow.visibility = View.VISIBLE
                        preview_tv_show!!.clear()
                        preview_tv_show!!.addAll(commonResponse.mdata.main_data.preview)
                    } else {
                        binding.lytPreviewTvShow.visibility = View.GONE
                    }
                    if (commonResponse.mdata.main_data.catgorie_content != null && commonResponse.mdata.main_data.catgorie_content.size > 0) {
                        catgorie_content_tv_show!!.clear()
                        catgorie_content_tv_show!!.addAll(commonResponse.mdata.main_data.catgorie_content)
                    } else {
                        binding.rvAllTvShow.visibility = View.GONE
                    }
                    Log.d("categorydata","api : "+commonResponse.mdata.shows_category.toString())

                    if (commonResponse.mdata.shows_category != null) {
                        categoryList_tv_show!!.clear()
                        categoryList_tv_show!!.add(MoviesAndShowsCategory(0, "All", "", 0, "", ""))
                        categoryList_tv_show!!.addAll(commonResponse.mdata.shows_category)
                    }
                    previewsAdapter_tv_show!!.notifyDataSetChanged()
                    movieAllAdapter_tv_show!!.notifyDataSetChanged()
                } else {
                    Log.d("urlstringing", "callGetMovieApi: " + "else part movie")
                    binding.lytTopTvShow.visibility = View.GONE
                    utils.simpleAlert(
                        this,
                        "Error",
                        commonResponse.message.toString()
                    )

                }
            }, { throwable ->

                if (throwable is ConnectException) {
                    val intent=Intent(this,MyDownloadActivity::class.java)
                    startActivity(intent)
                }
            })
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun callGetMovieApi() {
        categoryList_movie!!.clear()
        catgorie_content_movie!!.clear()
        preview_movie_movie!!.clear()
        apiService.callGetMovieApi(
            "Bearer " + userPref1.getToken(),
            userPref1.getSubUserId().toString(), userPref1.getFcmToken().toString(),
            userPref1.getPreferredLanguage(),getCountryCode(getCountryName(this))!!
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                Log.d("getmoview",commonResponse.mdata.toString())
                Log.d("getmoview",commonResponse.message.toString())
                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    if (commonResponse.mdata.main_data.top_banner != null) {
//                        setBannerData_movie(commonResponse.mdata.main_data.top_banner)
                        if (commonResponse.mdata.main_data.top_banner != null) {
                            val bannerimage =
                                commonResponse.mdata.main_data.top_banner.bnner_content!!.poster
                            Glide.with(this)
                                .load(Uri.parse(bannerimage))
                                /*.apply(RequestOptions.fitCenterTransform())*/
                                .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
                                .apply(RequestOptions.errorOf(R.drawable.image_loading))
                                .into(binding.ivBannerMovie)

                            topBannerModel = commonResponse.mdata.main_data.top_banner.bnner_content
                            bannerUrl =
                                commonResponse.mdata.main_data.top_banner.bnner_content?.path!!
                            binding.tvMovieMovie.text =
                                commonResponse.mdata.main_data.top_banner.bnner_content.title
                            binding.tvYearMovie.text =
                                commonResponse.mdata.main_data.top_banner.bnner_content.release_year
                            binding.tvTimeMovie.text =
                                commonResponse.mdata.main_data.top_banner.bnner_content.content_duration
                            binding.tvCategoryMovie.text = commonResponse.mdata.main_data.top_banner.title
                            binding.tvCertificatMovie.text = "U/A"

                            homemovieid = topBannerModel!!.id.toString()
                            homemovieCatid = topBannerModel!!.cat_id.toString()

                            if (commonResponse.mdata.main_data.top_banner.bnner_content?.my_list_status == "1") {
                                isAddList_movie = true
                                binding.ivMyListMovie.setImageResource(R.drawable.ic_check_mark_blue)
                                //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
                            } else {
                                binding.ivMyListMovie.setImageResource(R.drawable.add_list)
                                //binding!!.ivMyList.colorFilter = null
                            }
                        }




                        binding.lytInfoMovie.setOnClickListener {
                            setInfo(commonResponse.mdata.main_data.top_banner)
                        }

                    } else {
//                        topbanner="null"
                    }
                    if (commonResponse.mdata.main_data.preview.size > 0) {
                        binding.lytPreviewMovie.visibility = View.VISIBLE
                        preview_movie_movie!!.addAll(commonResponse.mdata.main_data.preview)
                        Log.d("TAG", "callGetMovieApi: " + "if part preview")
                    } else {
                        Log.d("TAG", "callGetMovieApi: " + "else part preview")
                        binding.lytPreviewMovie.visibility = View.GONE
                    }

                    if (commonResponse.mdata.main_data.catgorie_content.size > 0) {
                        binding.rvAllMovie.visibility = View.VISIBLE
                        catgorie_content_movie!!.clear()
                        catgorie_content_movie!!.addAll(commonResponse.mdata.main_data.catgorie_content)
                        Log.d("TAG", "callGetMovieApi: " + " if part moview")
                    } else {
                        Log.d("TAG", "callGetMovieApi: " + "else part movie")
                        binding.rvAllMovie.visibility = View.GONE
                    }

                    if (commonResponse.mdata.movies_category != null) {
                        categoryList_movie!!.add(MoviesAndShowsCategory(0, "All", "", 0, "", ""))
                        categoryList_movie!!.addAll(commonResponse.mdata.movies_category)
                    }
                    setRecyclerview_movie_tab()
                    previewsAdapter_movie!!.notifyDataSetChanged()
                    movieAllAdapter_movie!!.notifyDataSetChanged()


                }
                else {
                    // binding!!.swipeContainer.isRefreshing = false
//                    lytTop_movie.visibility = View.GONE


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
//                    utils.simpleAlert(
//                        this,
//                        "Error",
//                        getString(R.string.check_network_connection)
//                    )
                    val intent=Intent(this,MyDownloadActivity::class.java)
                    startActivity(intent)
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


    fun home_api() {
        userPref1 = UserPref(applicationContext)
        catgorie_content = ArrayList()
        continue_watch = ArrayList()
        last_based_content = ArrayList()
        most_watched = ArrayList()
        mylist = ArrayList()
        preview = ArrayList()
        trending = ArrayList()
        mambooriginal = ArrayList()
        subTitleList = ArrayList()

        callDashboardApi()
        setRecyclerview_home()


        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter.Builder(applicationContext).build()
        val trackSelector = DefaultTrackSelector(applicationContext)

        // Create ExoPlayer instance
        exoPlayer = ExoPlayer.Builder(applicationContext)
            .setTrackSelector(trackSelector)
            .build()

        val videoURI: Uri = Uri.parse(videoURL)
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
        val mediaSource = mediaSourceFactory.createMediaSource(MediaItem.fromUri(videoURI))

        // binding?.exoPlayer?.player = exoPlayer
        // exoPlayer.setMediaSource(mediaSource)
        // exoPlayer.prepare()
        // exoPlayer.playWhenReady = true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun callDashboardApi() {
        catgorie_content!!.clear()
        continue_watch!!.clear()
        last_based_content!!.clear()
        most_watched!!.clear()
        mylist!!.clear()
        preview!!.clear()
        trending!!.clear()
        mambooriginal!!.clear()
        //userPref.getSubUserId().toString()
//        val apiService1= ApiService
        apiService.callDashboardApi(
            "Bearer " + userPref1.getToken(),
            userPref1.getSubUserId().toString(),
            userPref1.getFcmToken().toString(),
            userPref1.getPreferredLanguage(),getCountryCode(getCountryName(this))!!
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /* .doOnSubscribe(this::showProgressDialog)
             .doOnCompleted(this::hideProgressDialog)*/
            .subscribe({ commonResponse ->
                binding.llLayout.visibility = View.VISIBLE
                if (commonResponse.login_session == 0 || commonResponse.user_status == 0 ) {
                    showDialogSessionOut()
                }
                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    // binding!!.swipeContainer.isRefreshing = false
                    //binding!!.lytTop.visibility = View.VISIBLE
//                    shimmerFrameLayout.visibility = View.GONE


//                    commonResponse.mdata.top_banner?.let { setBannerData(it) }

                    val bannerimage = commonResponse.mdata.top_banner?.bnner_content?.poster
                    Glide.with(this)
                        .load(Uri.parse(bannerimage))
                        /*.apply(RequestOptions.fitCenterTransform())*/
                        .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
                        .apply(RequestOptions.errorOf(R.drawable.image_loading))
                        .into(binding.ivBanner)
                    topBannerModel = commonResponse.mdata.top_banner?.bnner_content
                    bannerUrl = commonResponse.mdata.top_banner?.bnner_content?.path!!
                    binding.tvMovie.text = commonResponse.mdata.top_banner.bnner_content.title
                    binding.tvYear.text = commonResponse.mdata.top_banner.bnner_content.release_year
                    binding.tvTime.text = commonResponse.mdata.top_banner.bnner_content.content_duration
                    binding.tvCategory.text = commonResponse.mdata.top_banner.title
                    binding.tvCertificat.text = "U/A"
                    homebannerid = topBannerModel!!.id.toString()
                    homebannerCatid = topBannerModel!!.cat_id.toString()
                    if (commonResponse.mdata.top_banner.bnner_content.my_list_status == "1") {
                        isAddList = true
                        binding.ivMyList.setImageResource(R.drawable.ic_check_mark_blue)
                        //binding!!.binding.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
                    } else {
                        binding.ivMyList.setImageResource(R.drawable.add_list)
                        //binding!!.ivMyList.colorFilter = null
                    }

                    binding.lytInfo.setOnClickListener {
                        setInfo(commonResponse.mdata.top_banner)
                    }

                    if (commonResponse.mdata.preview != null && commonResponse.mdata.preview.size > 0) {
                        binding.lytPreview.visibility = View.VISIBLE
                        binding.tvPreview.visibility = View.VISIBLE
                        preview!!.clear()
                        preview!!.addAll(commonResponse.mdata.preview)
                    } else {
                        binding.lytPreview.visibility = View.GONE
                    }
                    if (commonResponse.mdata.continue_watch != null && commonResponse.mdata.continue_watch.size > 0) {
                        binding.lytContinue.visibility = View.VISIBLE
                        continue_watch!!.clear()
                        continue_watch!!.addAll(commonResponse.mdata.continue_watch)
                    } else {
                        binding.lytContinue.visibility = View.GONE
                    }

                    if (commonResponse.mdata.mylist != null && commonResponse.mdata.mylist.size > 0) {
                        binding.lytMyList.visibility = View.VISIBLE
                        mylist!!.clear()
                        mylist!!.addAll(commonResponse.mdata.mylist)
                    } else {
                        binding.lytMyList.visibility = View.GONE
                    }
                    if (commonResponse.mdata.last_based_content != null && commonResponse.mdata.last_based_content.size > 0) {
                        binding.lytOfYou.visibility = View.VISIBLE
                        last_based_content!!.clear()
                        last_based_content!!.addAll(commonResponse.mdata.last_based_content)
                    } else {
                        binding.lytOfYou.visibility = View.GONE
                    }
                    if (commonResponse.mdata.trending != null && commonResponse.mdata.trending.size > 0) {
                        binding.lytTrendingNow.visibility = View.VISIBLE
                        trending!!.clear()
                        trending!!.addAll(commonResponse.mdata.trending)
                        //Log.e("trending ","<< If Size>> "+trending!!.size)
                    }
                    else {
                        // Log.e("trending ","<<Size>> "+trending!!.size)
                        binding.lytTrendingNow.visibility = View.GONE
                    }
                    if (commonResponse.mdata.mambo_original != null && commonResponse.mdata.mambo_original?.size!! > 0) {
                        binding.mambolayout.visibility = View.VISIBLE
                        mambooriginal!!.clear()
                        mambooriginal!!.addAll(commonResponse.mdata.mambo_original)
                        //Log.e("trending ","<< If Size>> "+trending!!.size)
                    }
                    else {
                        // Log.e("trending ","<<Size>> "+trending!!.size)
                        binding.mambolayout.visibility = View.GONE
                    }
                    if (commonResponse.mdata.most_watched != null && commonResponse.mdata.most_watched.size > 0) {
                        binding.lytMostWatched.visibility = View.VISIBLE
                        most_watched!!.clear()
                        most_watched!!.addAll(commonResponse.mdata.most_watched)
                    } else {
                        binding.lytMostWatched.visibility = View.GONE
                    }

                    if (commonResponse.mdata.catgorie_content != null && commonResponse.mdata.catgorie_content.size > 0) {
                        binding.rvAll.visibility = View.VISIBLE
                        catgorie_content!!.clear()
                        catgorie_content!!.addAll(commonResponse.mdata.catgorie_content)
                    } else {
                        binding.rvAll.visibility = View.GONE
                    }

                    previewsAdapter!!.notifyDataSetChanged()
                    movieAllAdapter_movie!!.notifyDataSetChanged()
                    continueWatchingAdapter!!.notifyDataSetChanged()
                    movieAllAdapter!!.notifyDataSetChanged()
                    myListHomeAdapter!!.notifyDataSetChanged()
                    becauseYouHomeAdapter!!.notifyDataSetChanged()
                    trendingNowHomeAdapter!!.notifyDataSetChanged()
                    mostWatchedHomeAdapter!!.notifyDataSetChanged()

                    //setBannerData(commonResponse.mdata.top_banner)
                }

            }, { throwable ->
                //hideProgressDialog()
                if (throwable is ConnectException) {
                    val intent=Intent(this,MyDownloadActivity::class.java)
                    startActivity(intent)

                }

            })
    }

    /* private fun setBannerData(model: TopBannerModel) {
         bannerMain = model
         if (model.bnner_content != null) {

             listLyt.visibility = View.VISIBLE


             Log.d("TAG", "setBannerData: " + model.bnner_content.poster)
             topBannerModel = model.bnner_content
             bannerUrl = model.bnner_content.path!!
             tvMovie.text = model.bnner_content.title
             tvYear.text = model.bnner_content.release_year
             tvTime.text = model.bnner_content.content_duration
             tvCategory.text = model.title
             tvCertificat.text = "U/A"
             Glide.with(this)
                 .load(Uri.parse(model.bnner_content.poster))
                 .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
                 .apply(RequestOptions.errorOf(R.drawable.image_loading))
                 .into(ivBanner)

             homebannerid = topBannerModel!!.id.toString()
             homebannerCatid = topBannerModel!!.cat_id.toString()


             if (model.bnner_content.my_list_status == "1") {
                 isAddList = true
                 ivMyList.setImageResource(R.drawable.ic_check_mark_blue)
                 //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
             } else {
                 ivMyList.setImageResource(R.drawable.add_list)
                 //binding!!.ivMyList.colorFilter = null
             }

             lytInfo.setOnClickListener {
                 setInfo(model)
             }

         }
     }*/

    private fun setBannerData_movie(model: TopBannerModel) {
        bannerMain = model
        if (model.bnner_content != null) {
            binding.listLytMovie.visibility = View.VISIBLE
            Glide.with(this)
                .load(Uri.parse(model.bnner_content.poster))
                /*.apply(RequestOptions.fitCenterTransform())*/
                .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
                .apply(RequestOptions.errorOf(R.drawable.image_loading))
                .into(binding.ivBannerMovie)

            topBannerModel = model.bnner_content
            bannerUrl = model.bnner_content.path!!
//            binding.tvMovieMovie.text = modeltvMovieMoviebnner_content.title
            binding.tvYearMovie.text = model.bnner_content.release_year
            binding.tvTimeMovie.text = model.bnner_content.content_duration
            binding.tvCategoryMovie.text = model.title
            binding.tvCertificatMovie.text = "U/A"

            homemovieid = topBannerModel!!.id.toString()
            homemovieCatid = topBannerModel!!.cat_id.toString()


            if (model.bnner_content.my_list_status == "1") {
                isAddList_movie = true
                binding.ivMyListMovie.setImageResource(R.drawable.ic_check_mark_blue)
                //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
            } else {
                binding.ivMyListMovie.setImageResource(R.drawable.add_list)
                //binding!!.ivMyList.colorFilter = null
            }
            binding.lytInfoMovie.setOnClickListener {
                setInfo(model)
            }
        }
    }

    private fun setBannerData_tv_show(model: TopBannerModel) {
        bannerMain = model
        if (model != null) {
            binding.listLytTvShow.visibility = View.VISIBLE

            Glide.with(this)
                .load(Uri.parse(model.bnner_content!!.poster))
                /*.apply(RequestOptions.fitCenterTransform())*/
                .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
                .apply(RequestOptions.errorOf(R.drawable.image_loading))
                .into(binding.ivBannerTvShow)

            topBannerModel = model.bnner_content
            bannerUrl = model.bnner_content.path!!
            binding.tvMovieTvShow.text = model.bnner_content.title
            binding.tvYearTvShow.text = model.bnner_content.release_year
            binding.tvTimeTvShow.text = model.bnner_content.content_duration
            binding.tvCategory.text = model.title
            binding.tvCertificatTvShow.text = "U/A"
            homemovietvid = topBannerModel!!.id.toString()
            homemovietvcatid = topBannerModel!!.cat_id.toString()

            if (model.bnner_content.my_list_status == "1") {
                isAddList_tv_show = true
                binding.ivMyListTvShow.setImageResource(R.drawable.ic_check_mark_blue)
                //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
            } else {
                binding.ivMyListTvShow.setImageResource(R.drawable.add_list)
                //binding!!.ivMyList.colorFilter = null
            }
            /* val view: View =
                 layoutInflater.inflate(R.layout.info_bottom_sheet, null)*/

            binding.lytInfoTvShow.setOnClickListener {
                setInfo(model)
            }
        }
    }

    private fun showDialogSessionOut() {
        val cDialog = Dialog(this@HomeActivity3, R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogSeassionOutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this@HomeActivity3),
            R.layout.dialog_seassion_out,
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

        bindingDialog.tvTitle1.setTextColor(
            ContextCompat.getColor(
                this@HomeActivity3,
                R.color.colorAccent
            )
        )

        Log.d("usertoken",userPref.getToken().toString())

        bindingDialog.btSubmit.setOnClickListener {
            cDialog.dismiss()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            callLogoutApi()


        }
    }

    private fun callLogoutApi() {
        apiService.callLogoutApi("Bearer " + userPref.getToken(), userPref.getFcmToken().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
//                    userPref.clearPref()
                    if (userPref1.getLoginType() == "2") {
                        googleSignOut()
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                        val googleSignInClient = GoogleSignIn.getClient(this@HomeActivity3, gso)
                        googleSignInClient.signOut()
                    } else if (userPref1.getLoginType() == "3") {
                        facebookLogout()

                    }
                    userPref1.clearPref()
                    startActivity(
                        Intent(
                            this,
                            LoginActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
//                    startActivity(Intent(this, LoginActivity::class.java))

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

    private fun setRecyclerview_home() {
        //Previews
        layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        previewsAdapter = PreviewsAdapter(applicationContext, preview!!)
        binding.rvPreviews.layoutManager = layoutManager
        binding.rvPreviews.adapter = previewsAdapter
        previewsAdapter!!.setOnItemClickListener(object : PreviewsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                Log.d("contentid",preview_movie_movie!![position].content_id.toString())
                startActivity(
                    Intent(applicationContext, PlayerActivity::class.java)
                        .putExtra("content_id", preview!![position].content_id)
                        .putExtra("watch_time", "0")
                        .putExtra("name", preview!![position].preview_content!!.title)
                        .putExtra("playUrl", preview!![position].preview_content!!.trailer_path)
                )

                /* if (preview!![position].preview_content?.path!=null && preview!![position].preview_content?.path !=""){
                     startActivity(Intent(requireContext(), PlayerViewActivity::class.java)
                         .putExtra("playUrl",preview!![position].preview_content?.path)
                         .putExtra("watch_duration","0"))
                 }else{
                     Toast.makeText(requireContext(),"No Preview details", Toast.LENGTH_SHORT).show()
                 }*/
            }
        })

        //continueWatchingAdapter
        layoutManager1 = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        continueWatchingAdapter = ContinueWarchingAdapter(applicationContext, continue_watch!!)
        binding.rvContinue.layoutManager = layoutManager1
        binding.rvContinue.adapter = continueWatchingAdapter


        continueWatchingAdapter!!.setOnItemClickListener(object :
            ContinueWarchingAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                if (continue_watch!![position].content_detail!!.content_type == "1") {
                    click_value = "movie"
                }
                else if ((continue_watch!![position].content_detail!!.content_type == "2")) {
                    click_value = "other"
                }
                Log.d("contentdata","content_id"+continue_watch!![position].content_id)
                Log.d("contentdata","watch_time"+continue_watch!![position].watch_duration)
                Log.d("contentdata","episode_id"+ continue_watch!![position].episode_id)

                startActivity(
                    Intent(applicationContext, ContentDetailsActivity::class.java)
                        .putExtra("content_id", continue_watch!![position].content_id)
                        .putExtra("watch_time", continue_watch!![position].watch_duration)
                        .putExtra("episode_id", continue_watch!![position].episode_id)
                        .putExtra("fragment_type", click_value)
                )
            }
        })

        //mambooriginal

        layoutManager0 = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        MomboOriginalAdapter = MomboOriginalAdapterpackage(applicationContext, mambooriginal!!)
        binding.rvMambo.layoutManager = layoutManager0
        binding.rvMambo.adapter = MomboOriginalAdapter

        MomboOriginalAdapter!!.setOnItemClickListener(object :
            MomboOriginalAdapterpackage.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
//                if (mambooriginal!![position].content_detail!!.content_type == "1") {
//                    click_value = "movie"
//                } else if ((mambooriginal!![position].content_detail!!.content_type == "2")) {
//                    click_value = "other"
//                }
                startActivity(
                    Intent(applicationContext, ContentDetailsActivity::class.java)
                        .putExtra("content_id", mambooriginal!![position].id.toString())
                        .putExtra("watch_time", "0")
                        .putExtra("episode_id", "0")
                        .putExtra("fragment_type", click_value)
                )
            }
        })

        //My List
        layoutManager2 = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        myListHomeAdapter = MyListHomeAdapter(applicationContext, mylist!!)
        binding.rvMyList.layoutManager = layoutManager2
        binding.rvMyList.adapter = myListHomeAdapter
        myListHomeAdapter!!.setOnItemClickListener(object : MyListHomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                if (mylist!![position].content.content_type == "1") {
                    click_value = "movie"
                } else if (mylist!![position].content.content_type == "2") {
                    click_value = "other"
                }
                Log.d("TAG", "onItemClick: " + mylist!![position])
                startActivity(
                    Intent(applicationContext, ContentDetailsActivity::class.java)
                        .putExtra("content_id", mylist!![position].content_id)
                        .putExtra("watch_time", "0")
                        .putExtra("fragment_type", click_value)
                        .putExtra("episode_id", mylist!![position].episode_id)
                )
            }
        })

        //Because you
        layoutManager3 = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        becauseYouHomeAdapter = BecauseYouHomeAdapter(applicationContext, last_based_content!!)
        binding.rvOfYou.layoutManager = layoutManager3
        binding.rvOfYou.adapter = becauseYouHomeAdapter
        becauseYouHomeAdapter!!.setOnItemClickListener(object :
            BecauseYouHomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {

                if (last_based_content!![position].content_type == "1") {
                    click_value = "movie"
                } else if (last_based_content!![position].content_type == "2") {
                    click_value = "other"
                }
                startActivity(
                    Intent(applicationContext, ContentDetailsActivity::class.java)
                        .putExtra("content_id", last_based_content!![position].id)
                        .putExtra("watch_time", "0")
                        .putExtra("fragment_type", click_value)
                        .putExtra("episode_id", "")
                )
            }
        })

        //Trending
        layoutManager4 = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        trendingNowHomeAdapter = TrendingNowHomeAdapter(applicationContext, trending!!)
        binding.rvTrendingNow.layoutManager = layoutManager4
        binding.rvTrendingNow.adapter = trendingNowHomeAdapter
        trendingNowHomeAdapter!!.setOnItemClickListener(object :
            TrendingNowHomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                if (trending!![position].only_content!!.content_type == "1") {
                    click_value = "movie"
                } else if (trending!![position].only_content!!.content_type == "2") {
                    click_value = "other"
                }
                startActivity(
                    Intent(applicationContext, ContentDetailsActivity::class.java)
                        .putExtra("content_id", trending!![position].content_id)
                        .putExtra("watch_time", "0")
                        .putExtra("fragment_type", click_value)
                        .putExtra("episode_id", trending!![position].episode_id)
                )
            }
        })

        //Most watched
        layoutManager5 = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        mostWatchedHomeAdapter = MostWatchedHomeAdapter(applicationContext, most_watched!!)
        binding.rvMostWatched.layoutManager = layoutManager5
        binding.rvMostWatched.adapter = mostWatchedHomeAdapter
        mostWatchedHomeAdapter!!.setOnItemClickListener(object :
            MostWatchedHomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                if (most_watched!![position].content.content_type == "1") {
                    click_value = "movie"
                } else if (most_watched!![position].content.content_type == "2") {
                    click_value = "other"
                }
                startActivity(
                    Intent(applicationContext, ContentDetailsActivity::class.java)
                        .putExtra("content_id", most_watched!![position].content_id)
                        .putExtra("watch_time", "0")
                        .putExtra("fragment_type", click_value)
                        .putExtra("episode_id", most_watched!![position].episode_id)
                )
            }
        })


        //MovieAllAdapter
        layoutManager6 = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        movieAllAdapter = MovieAllAdapter(applicationContext, catgorie_content!!, "dashboard")
        binding.rvAll.layoutManager = layoutManager6
        binding.rvAll.adapter = movieAllAdapter
        movieAllAdapter!!.setOnItemClickListener(object : MovieAllAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {

            }
        })
    }

    private fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }
    private fun setNavigationData() {
        binding.header.tvName0.text = "Welcome " + userPref1.getSubUserName()
        Glide.with(this).load(userPref1.getsubuserImg())
            .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
            .apply(RequestOptions.errorOf(R.drawable.user_profile))
            .into(binding.header.ivUser)
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

        headerList!!.add(
            DashboardHeaderMenuModel(
                getString(R.string.myDownloads),
                false
            )
        )
        headerList!!.add(
            DashboardHeaderMenuModel(
                getString(R.string.my_plans),
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
        listDataChild!![headerList!![9].title] = ArrayList()
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

                        getReadNotification("2")
                        startActivity(Intent(this, NotificationsActivity::class.java))

                    }

                    1 -> {
                        startActivity(Intent(this, MyDownloadActivity::class.java))
                    }

                    2 -> {
                        //PaymentBillingActivity
                        startActivity(
                            Intent(this, PurchasedHistory::class.java)
                                .putExtra("FromHome", "FromHome")
                        )
                    }
                    //Activity log
                    3 -> {
                        //PaymentBillingActivity
                        startActivity(Intent(this, PaymentBillingActivity::class.java))
                    }


                    4 -> {
                        startActivity(Intent(this, ManageDevicesActivity::class.java))
                    }

                    5 -> {

                        startActivity(Intent(this, SettingsActivity::class.java))

                    }

                    6 -> {
                        startActivity(Intent(this, FaqActivity::class.java))
                        //FaqActivity
                    }

                    7 -> {
                        startActivity(Intent(this, HelpActivity::class.java))

                    }

                    8 -> {
                        startActivity(Intent(this, ContacUsActivity::class.java))
                    }

                    9 -> {
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

        cDialog.show()
        bindingDialog.tvTitle.text = getString(R.string.logout)
        bindingDialog.tvMsg.text = getString(R.string.logoutConfirmation)

        bindingDialog.btOk.setOnClickListener {
            cDialog.dismiss()

            callLogoutApi()

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
                    val googleSignInClient = GoogleSignIn.getClient(this@HomeActivity3, gso)
                    googleSignInClient.signOut()
                }
            })

    }

    private fun facebookLogout() {
        LoginManager.getInstance().logOut()
    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun allclick() {
        binding.linearHomeSelected.setOnClickListener(this)
        binding.linearMovieSelected.setOnClickListener(this)
        binding.linearTvShowSelected.setOnClickListener(this)
        binding.linearMyListSelected.setOnClickListener(this)
        binding.linearMoreSelected.setOnClickListener(this)
        binding.ivSearchTvShow.setOnClickListener(this)
        binding.ivSearchMovie.setOnClickListener(this)
        binding.ivSearch.setOnClickListener(this)
        binding.tvEthnicityTypeMovie.setOnClickListener(this)
        binding.ivSearchContentMyList.setOnClickListener(this)
        binding.lytInfo.setOnClickListener(this)
        binding.lytInfoMovie.setOnClickListener(this)
        binding.lytInfoTvShow.setOnClickListener(this)
        binding.btnPlay.setOnClickListener(this)
        binding.btnPlayMovie.setOnClickListener(this)
        binding.btnPlayTvShow.setOnClickListener(this)
        binding.tvAddList.setOnClickListener(this)
        binding.tvAddListMovie.setOnClickListener(this)
        binding.tvAddList.setOnClickListener(this)
        binding.tvAddListTvShow.setOnClickListener(this)
        binding.tvEthnicityTypeTvShow.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvAddList -> handleAddListClick(homebannerid, homebannerCatid, binding.ivMyList)
            R.id.tvAddList_tv_show -> handleAddListClick(homemovietvid, homemovietvcatid, binding.ivMyListTvShow)
            R.id.tvAddList_movie -> handleAddListClick(homemovieid, homemovieCatid, binding.ivMyListMovie)
            R.id.tvEthnicityType_tv_show -> dialogCategory?.show()
            R.id.tvEthnicityType_movie -> dialogCategory1?.show()
            R.id.btnPlay, R.id.btnPlay_movie, R.id.btnPlay_tv_show -> {
                val fragmentType = if (v.id == R.id.btnPlay) "movie" else "tv"
                startContentDetailsActivity(topBannerModel?.id, fragmentType)
            }
            R.id.lytInfo, R.id.lytInfo_movie, R.id.lytInfo_tv_show -> bannerMain?.let { setInfo(it) }

            R.id.ivSearch_tv_show, R.id.ivSearchContent_my_list, R.id.ivSearch, R.id.ivSearch_movie -> {
                val contentType = when (v.id) {
                    R.id.ivSearch_tv_show -> "2"
                    R.id.ivSearchContent_my_list, R.id.ivSearch -> "3"
                    else -> "1"
                }
                startActivity(Intent(applicationContext, SearchActivity::class.java).putExtra("contentType", contentType))
            }

            R.id.linear_home_selected -> updateTabPosition(0)
            R.id.linear_movie_selected -> updateTabPosition(1)
            R.id.linear_tv_show_selected -> updateTabPosition(2)
            R.id.linear_my_list_selected -> {
                updateTabPosition(3)
                callMyListAPI()
            }
            R.id.linear_more_selected -> {
//                updateTabPosition(0)
                openDrawer()
            }
        }
    }

    private fun handleAddListClick(bannerId: String, categoryId: String, imageView: ImageView) {
        if (topbanner != "null") {
            val isAdding = imageView.tag == null || imageView.tag == false
            callMakeListApi(bannerId, categoryId, if (isAdding) "1" else "0")
            imageView.setImageResource(if (isAdding) R.drawable.ic_check_mark_blue else R.drawable.add_list)
            imageView.tag = isAdding
        }
    }

    private fun startContentDetailsActivity(contentId: String?, fragmentType: String) {
        try {
            startActivity(
                Intent(applicationContext, ContentDetailsActivity::class.java).apply {
                    putExtra("content_id", contentId)
                    putExtra("watch_time", "0")
                    putExtra("episode_id", "")
                    putExtra("fragment_type", fragmentType)
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "You cannot see", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTabPosition(position: Int) {
        tabPositionArray.add(position)
        tabPosition = tabPositionArray.last()
        Log.d("tab_position", tabPositionArray.toString())
        Log.d("tab_position_last_index", tabPositionArray.last().toString())

        binding.tvMovie.visibility = View.VISIBLE
        // Reset all visibility
        binding.apply {
            tvHome.visibility = View.INVISIBLE
            mambolayout.visibility = View.INVISIBLE
            tvMovieText.visibility = View.INVISIBLE
            tvShow.visibility = View.INVISIBLE
            tvMyList.visibility = View.INVISIBLE
            tvMoreText.visibility = View.INVISIBLE
            viewUnderHome.visibility = View.INVISIBLE
            viewUnderMovie.visibility = View.INVISIBLE
            viewUnderTvShow.visibility = View.INVISIBLE
            viewUnderMyList.visibility = View.INVISIBLE
            viewUnderMore.visibility = View.INVISIBLE
            linearHome.visibility = View.INVISIBLE
            linearMovie.visibility = View.INVISIBLE
            linearTvShow.visibility = View.INVISIBLE
            linearMyList.visibility = View.INVISIBLE
        }

        Log.d("moviewtabtext",binding.tvMovieText.visibility.toString())

        when (position) {
            0 -> {
                binding.tvHome.visibility = View.VISIBLE
                binding.mambolayout.visibility = View.VISIBLE
                binding.viewUnderHome.visibility = View.VISIBLE
                binding.linearHome.visibility = View.VISIBLE
            }
            1 -> {
                binding.tvMovieText.visibility = View.VISIBLE
                binding.viewUnderMovie.visibility = View.VISIBLE
                binding.linearMovie.visibility = View.VISIBLE
            }
            2 -> {
                binding.tvShow.visibility = View.VISIBLE
                binding.viewUnderTvShow.visibility = View.VISIBLE
                binding.linearTvShow.visibility = View.VISIBLE
            }
            3 -> {
                binding.tvMyList.visibility = View.VISIBLE
                binding.viewUnderMyList.visibility = View.VISIBLE
                binding.linearMyList.visibility = View.VISIBLE
            }
            4 -> {
                binding.tvMoreText.visibility = View.VISIBLE
                // Handle any additional visibility logic for "more"
            }
        }
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



    /*   override fun onClick(v: View?) {
           when (v!!.id) {
               R.id.tvAddList -> {

                   if (topbanner == "null") {

                   } else {
                       isAddList = if (isAddList) {
                           callMakeListApi(homebannerid, homebannerCatid, "0")

                           binding.ivMyList.setImageResource(R.drawable.add_list)
                           false
                       } else {
                           callMakeListApi(homebannerid, homebannerCatid, "1")
                           binding.ivMyList.setImageResource(R.drawable.ic_check_mark_blue)

                           true
                       }
                   }
               }

               R.id.tvAddList_tv_show -> {
                   if (topbanner == "null") {

                   } else {
                       isAddList_tv_show = if (isAddList_tv_show) {
                           callMakeListApi(homemovietvid, homemovietvcatid, "0")
                           //binding!!.ivMyList.colorFilter = null
                           binding.ivMyListTvShow.setImageResource(R.drawable.add_list)
                           false
                       } else {
                           callMakeListApi(homemovietvid, homemovietvcatid, "1")
                           binding.ivMyListTvShow.setImageResource(R.drawable.ic_check_mark_blue)
                           //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
                           true
                       }
                   }
               }

               R.id.tvAddList_movie -> {

                   if (topbanner == "null") {

                   } else {
                       isAddList_movie = if (isAddList_movie) {
                           callMakeListApi(homemovieid, homemovieCatid, "0")
   //                        callMakeListApi_movie(topBannerModel!!.id!!, topBannerModel!!.cat_id!!, "0")
                           //binding!!.ivMyList.colorFilter = null
                           binding.ivMyListMovie.setImageResource(R.drawable.add_list)
                           false
                       } else {
                           callMakeListApi(homemovieid, homemovieCatid, "1")
   //                        callMakeListApi_movie(topBannerModel!!.id!!, topBannerModel!!.cat_id!!, "1")
                           binding.ivMyListMovie.setImageResource(R.drawable.ic_check_mark_blue)
                           //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
                           true
                       }
                   }
               }

               R.id.tvEthnicityType_tv_show -> {
                   dialogCategory!!.show()
               }

               R.id.tvEthnicityType_movie -> {
                   dialogCategory1?.show()
               }

               R.id.btnPlay -> {
                   try {
                       startActivity(
                           Intent(applicationContext, ContentDetailsActivity::class.java)
                               .putExtra("content_id", topBannerModel!!.id)
                               .putExtra("watch_time", "0")
                               .putExtra("episode_id", "")
                               .putExtra("fragment_type", "movie")
                       )
                   } catch (e: Exception) {
                       e.printStackTrace()
                       Toast.makeText(applicationContext, "You can not see", Toast.LENGTH_SHORT).show()
                   }
               }

               R.id.btnPlay_movie -> {
                   try {
                       startActivity(
                           Intent(applicationContext, ContentDetailsActivity::class.java)
                               .putExtra("content_id", topBannerModel!!.id)
                               .putExtra("watch_time", "0")
                               .putExtra("fragment_type", "movie")
                               .putExtra("episode_id", "")
                       )
                   } catch (e: Exception) {
                       e.printStackTrace()
                       Toast.makeText(applicationContext, "You can not see", Toast.LENGTH_SHORT).show()
                   }
               }

               R.id.btnPlay_tv_show -> {
                   try {
                       startActivity(
                           Intent(applicationContext, ContentDetailsActivity::class.java)
                               .putExtra("content_id", topBannerModel!!.id)
                               .putExtra("watch_time", "0")
                               .putExtra("episode_id", "")
                               .putExtra("fragment_type", "movie")
                       )
                   } catch (e: Exception) {
                       e.printStackTrace()
                       Toast.makeText(applicationContext, "You can not see", Toast.LENGTH_SHORT).show()
                   }
               }

               R.id.lytInfo -> {
                   bannerMain?.let { setInfo(it) }
               }

               R.id.lytInfo_movie -> {
                   bannerMain?.let { setInfo(it) }
               }

               R.id.lytInfo_tv_show -> {
                   bannerMain?.let { setInfo(it) }
               }

               R.id.ivSearch_tv_show -> {
                   startActivity(
                       Intent(
                           applicationContext,
                           SearchActivity::class.java
                       ).putExtra("contentType", "2")
                   )
               }

               R.id.ivSearchContent_my_list -> {
                   startActivity(
                       Intent(
                           applicationContext,
                           SearchActivity::class.java
                       ).putExtra("contentType", "3")
                   )
               }

               R.id.ivSearch -> {
                   startActivity(
                       Intent(
                           applicationContext,
                           SearchActivity::class.java
                       ).putExtra("contentType", "3")
                   )
               }

               R.id.ivSearch_movie -> {
                   startActivity(
                       Intent(
                           applicationContext,
                           SearchActivity::class.java
                       ).putExtra("contentType", "1")
                   )
               }

               R.id.linear_home_selected -> {
                   Log.d("tab_postion", tabPositionArray.toString())
                   Log.d("tab_postion_last_index", tabPositionArray.last().toString())
                   tabPositionArray[0] = 0
                   tabPosition = tabPositionArray.last()
                   binding.tvHome.visibility = View.VISIBLE
                   binding.mambolayout.visibility = View.VISIBLE
                   binding.tvMovie.visibility = View.INVISIBLE
                   binding.tvShow.visibility = View.INVISIBLE
                   binding.tvMyList.visibility = View.INVISIBLE
                   binding.tvMoreText.visibility = View.INVISIBLE
                   binding.viewUnderHome.visibility = View.VISIBLE
                   binding.viewUnderMovie.visibility = View.INVISIBLE
                   binding.viewUnderTvShow.visibility = View.INVISIBLE
                   binding.viewUnderMyList.visibility = View.INVISIBLE
                   binding.viewUnderMore.visibility = View.INVISIBLE
                   binding.linearHome.visibility = View.VISIBLE
                   binding.linearMovie.visibility = View.INVISIBLE
                   binding.linearTvShow.visibility = View.INVISIBLE
                   binding.linearMyList.visibility = View.INVISIBLE
               }

               R.id.linear_movie_selected -> {
                   tabPositionArray.add(1)
                   tabPosition = tabPositionArray.last()
                   Log.d("tab_postion", tabPositionArray.toString())
                   Log.d("tab_postion_last_index", tabPositionArray.last().toString())
                   binding.tvHome.visibility = View.INVISIBLE
                   binding.mambolayout.visibility = View.INVISIBLE
                   binding.tvMovie.visibility = View.VISIBLE
                   binding.tvShow.visibility = View.INVISIBLE
                   binding.tvMyList.visibility = View.INVISIBLE
                   binding.tvMoreText.visibility = View.INVISIBLE
                   binding.viewUnderHome.visibility = View.INVISIBLE
                   binding.viewUnderMovie.visibility = View.VISIBLE
                   binding.viewUnderTvShow.visibility = View.INVISIBLE
                   binding.viewUnderMyList.visibility = View.INVISIBLE
                   binding.viewUnderMore.visibility = View.INVISIBLE
                   binding.linearHome.visibility = View.VISIBLE
                   binding.linearMovie.visibility = View.INVISIBLE
                   binding.linearTvShow.visibility = View.INVISIBLE
                   binding.linearMyList.visibility = View.INVISIBLE

               }

               R.id.linear_tv_show_selected -> {

                   tabPositionArray.add(2)
                   tabPosition = tabPositionArray.last()

                   Log.d("tab_postion", tabPositionArray.toString())
                   Log.d("tab_postion_last_index", tabPositionArray.last().toString())
                   binding.tvHome.visibility = View.INVISIBLE
                   binding.mambolayout.visibility = View.INVISIBLE
                   binding.tvMovie.visibility = View.INVISIBLE
                   binding.tvShow.visibility = View.VISIBLE
                   binding.tvMyList.visibility = View.INVISIBLE
                   binding.tvMoreText.visibility = View.INVISIBLE
                   binding.viewUnderHome.visibility = View.INVISIBLE
                   binding.viewUnderMovie.visibility = View.INVISIBLE
                   binding.viewUnderTvShow.visibility = View.VISIBLE
                   binding.viewUnderMyList.visibility = View.INVISIBLE
                   binding.viewUnderMore.visibility = View.INVISIBLE
                   binding.linearHome.visibility = View.INVISIBLE
                   binding.linearMovie.visibility = View.INVISIBLE
                   binding.linearTvShow.visibility = View.VISIBLE
                   binding.linearMyList.visibility = View.INVISIBLE

               }

               R.id.linear_my_list_selected -> {

                   tabPositionArray.add(3)
                   tabPosition = tabPositionArray.last()

                   Log.d("tab_postion", tabPositionArray.toString())
                   Log.d("tab_postion_last_index", tabPositionArray.last().toString())
                   callMyListAPI()
                   binding.tvHome.visibility = View.INVISIBLE
                   binding.mambolayout.visibility = View.INVISIBLE
                   binding.tvMovie.visibility = View.INVISIBLE
                   binding.tvShow.visibility = View.INVISIBLE
                   binding.tvMyList.visibility = View.VISIBLE
                   binding.tvMoreText.visibility = View.INVISIBLE
                   binding.viewUnderHome.visibility = View.INVISIBLE
                   binding.viewUnderMovie.visibility = View.INVISIBLE
                   binding.viewUnderTvShow.visibility = View.INVISIBLE
                   binding.viewUnderMyList.visibility = View.VISIBLE
                   binding.viewUnderMore.visibility = View.INVISIBLE
                   binding.linearHome.visibility = View.INVISIBLE
                   binding.linearMovie.visibility = View.INVISIBLE
                   binding.linearTvShow.visibility = View.INVISIBLE
                   binding.linearMyList.visibility = View.VISIBLE
               }

               R.id.linear_more_selected -> {

                   tabPositionArray.add(4)
                   tabPosition = tabPositionArray.last()

                   Log.d("tab_postion", tabPositionArray.toString())
                   Log.d("tab_postion_last_index", tabPositionArray.last().toString())
                   openDrawer()
   //                binding.tvHome.visibility = View.INVISIBLE
   //                tv_movie.visibility = View.INVISIBLE
   //                tv_show.visibility = View.INVISIBLE
   //                binding.tvMyList.visibility = View.INVISIBLE
                   binding.tvMoreText.visibility = View.VISIBLE
   //                  binding.viewUnderHome.visibility = View.INVISIBLE
   //                  binding.viewUnderMovie.visibility = View.INVISIBLE
   //                view_under_tv_show.visibility = View.INVISIBLE
   //                binding.viewUnderMyList.visibility = View.INVISIBLE
                   binding.viewUnderMore.visibility = View.INVISIBLE
               }
           }

       }*/


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return true
    }

    private fun setRecyclerview_movie_tab() {
        //Previews
        layoutManager_movie =
            LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        previewsAdapter_movie = PreviewsMoviewTabAdapter(applicationContext, preview_movie_movie!!)

        binding.rvPreviewsMovie.layoutManager = layoutManager_movie
        binding.rvPreviewsMovie.adapter = previewsAdapter_movie

        previewsAdapter_movie!!.setOnItemClickListener(object :
            PreviewsMoviewTabAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                /*startActivity(Intent(requireContext(), ContentDetailsActivity::class.java)
                    .putExtra("content_id",preview!![position].content_id)
                    .putExtra("watch_time","0")
                    .putExtra("episode_id","0"))*/
                if (topbanner == "null") {

                } else {
                    if (preview_movie_movie!![position].preview_content!!.content_type == "1") {
                        click_value = "movie"
                    } else if (preview_movie_movie!![position].preview_content!!.content_type == "2") {
                        click_value = "other"
                    }

                    Log.d("contentid",preview_movie_movie!![position].content_id.toString())

                    startActivity(
                        Intent(applicationContext, PlayerActivity::class.java)
                            .putExtra("content_id", preview_movie_movie!![position].content_id)
                            .putExtra("watch_time", "0")
                            .putExtra("name", preview_movie_movie!![position].preview_content!!.title)
                            .putExtra("playUrl", preview_movie_movie!![position].preview_content!!.trailer_path)
                            .putExtra("fragment_type", click_value)
                    )
                }
            }
        })
        //MovieAllAdapter
        layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        movieAllAdapter_movie =
            MovieAllAdapterMovieTab(applicationContext, catgorie_content_movie!!, "movie")

        binding.rvAllMovie.layoutManager = layoutManager
        binding.rvAllMovie.adapter = movieAllAdapter_movie

//        movieAllAdapter_movie!!.setOnItemClickListener(object :
//            MovieAllAdapterMovieTab.OnItemClickListener {
//            override fun onItemClick(position: Int, view: View) {
//            }
//        })
    }

    private fun setRecyclerview_my_list() {
        binding.ivSearchContentMyList.setOnClickListener(this)
        binding.btAddListMyList.setOnClickListener(this)
        myListAdapter = MyListAdapter(applicationContext, myList_list_tab!!)
        val linearLayoutManager =
            GridLayoutManager(applicationContext, 3, LinearLayoutManager.VERTICAL, false)
        binding.rvMyListMyList.layoutManager = linearLayoutManager
        binding.rvMyListMyList.adapter = myListAdapter
        myListAdapter!!.notifyDataSetChanged()

        myListAdapter!!.setOnItemClickListener(object : MyListAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(
                position: Int,
                view: View,
                filterList: ArrayList<com.mamboflix.model.mylist.MyListModel>
            ) {
                if (myList_list_tab!![position].single_content.content_type == "1") {
                    click_value = "movie"
                } else if (myList_list_tab!![position].single_content.content_type == "2") {
                    click_value = "other"
                }
                startActivity(
                    Intent(applicationContext, ContentDetailsActivity::class.java)
                        .putExtra("content_id", myList_list_tab!![position].content_id)
                        .putExtra("episode_id", myList_list_tab!![position].episode_id)
                        .putExtra("watch_time", "0")
                        .putExtra("fragment_type", click_value)
                )

            }

            override fun onLongPress(
                position: Int,
                view: View,
                filterList: ArrayList<com.mamboflix.model.mylist.MyListModel>
            ) {
                confirmationAlert(filterList[position])

            }


        })
    }

    private fun confirmationAlert(file: com.mamboflix.model.mylist.MyListModel) {
        val cDialog = Dialog(applicationContext, R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogCustomBinding = DataBindingUtil.inflate(
            LayoutInflater.from(applicationContext),
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
        bindingDialog.tvTitle.text = getString(R.string.confirmation)
        bindingDialog.tvMsg.text = getString(R.string.do_you_want)

        bindingDialog.btOk.setOnClickListener {
            cDialog.dismiss()

//            callMakeListApi("0",file)
        }

        bindingDialog.btCancel.setOnClickListener {
            cDialog.dismiss()
        }

        /*val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        builder.setTitle("Confirmation")
        builder.setMessage("Do you want to remove this movie?")
        builder.setPositiveButton("Yes") { dialogInterface, i ->

            callMakeListApi("0",file)

        }
        builder.setNegativeButton("No", null)
        builder.create()
        builder.show()*/
    }

    private fun callMyListAPI() {
        apiService.callMyListAPI(
            "Bearer " + userPref.getToken(),
            userPref.getSubUserId().toString(),
            userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage(),getCountryCode(getCountryName(this))!!
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)

            .subscribe({ commonResponse ->

                if (commonResponse.status != 0 && commonResponse.mdata != null) {
                    //binding!!.swipeContainer.isRefreshing = false
                    myList_list_tab!!.clear()
                    myList_list_tab!!.addAll(commonResponse.mdata)

                    myListAdapter!!.notifyDataSetChanged()
                    binding.rvMyListMyList.visibility = View.VISIBLE
                    binding.tvNoDataMyList.visibility = View.GONE
                    binding.llEmptyMyListMyList.visibility = View.GONE

                } else {
                    binding.tvNoDataMyList.visibility = View.VISIBLE
                    binding.llEmptyMyListMyList.visibility = View.VISIBLE
                    binding.rvMyListMyList.visibility = View.GONE
                    /*binding!!.tvNoData.visibility = View.VISIBLE
                    binding!!.tvNoData.text = "No List Data"*/
//                    utils.simpleAlert(
//                        this,
//                        "",
//                        commonResponse.message.toString()
//                    )
                    // hideProgressDialog()
                }

            }, { throwable ->
                //hideProgressDialog()
                if (throwable is ConnectException) {
//                    utils.simpleAlert(
//                        this,
//                        "Error",
//                        getString(R.string.check_network_connection)
//                    )
                    val intent=Intent(this,MyDownloadActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d("urlstringing", "callGetMovieApi: " + "else part movie")
                    utils.simpleAlert(
                        this,
                        "Error",
                        throwable.message.toString()
                    )

                }
            })
    }

    private fun setInfo(infoModel: TopBannerModel) {
        val bindingDialog: InfoBottomSheetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(applicationContext),
            R.layout.info_bottom_sheet,
            null,
            false
        )
        /* val view: View =
             layoutInflater.inflate(R.layout.info_bottom_sheet, null)*/

        val dialog = BottomSheetDialog(this@HomeActivity3, R.style.CommentDialogTheme)

        dialog.setContentView(bindingDialog.root)
        dialog.show()

        bindingDialog.tvName.text = infoModel.bnner_content!!.title
        bindingDialog.tvVideoQuality.text = "Video Quality : " + infoModel.bnner_content.video_quality
        bindingDialog.tvCategory.text = infoModel.title
        bindingDialog.tvActor.text = "Actor : " + infoModel.bnner_content.actor_name
        bindingDialog.tvDirector.text = "Director : " + infoModel.bnner_content.director_name
        bindingDialog.tvWrite.text = "Writer : " + infoModel.bnner_content.writer_name


        bindingDialog.infoPlay.setOnClickListener {
            dialog.dismiss()

            if (infoModel.bnner_content.content_type == "1") {
                click_value = "movie"
            } else if (infoModel.bnner_content.content_type == "2") {
                click_value = "other"
            }

            startActivity(
                Intent(applicationContext, ContentDetailsActivity::class.java)
                    .putExtra("content_id", infoModel.bnner_content.id)
                    .putExtra("watch_time", "0")
                    .putExtra("fragment_type", click_value)
                    .putExtra("episode_id", "")
            )
        }

    }

    private fun callMakeListApi(contentID: String, categoryID: String, status: String) {
        apiService.callMakeListApi(
            "Bearer " + userPref.getToken(),
            userPref.getSubUserId().toString(),
            contentID,
            categoryID,
            status,
            userPref.getFcmToken().toString()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
                    callMyListAPI()
//                    ivMyList.setImageResource(R.drawable.ic_check_mark_blue)
//                    Toast.makeText(requireContext(),""+commonResponse.message, Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("urlstringing", "callGetMovieApi: " + "else part movie")
                    utils.simpleAlert(
                        this@HomeActivity3,
                        "Note",
                        commonResponse.message.toString()
                    )
                    hideProgressDialog()
                }
            } , { throwable ->
                hideProgressDialog()
                if (throwable is ConnectException) {
//                    utils.simpleAlert(
//                        this@HomeActivity3,
//                        "Error",
//                        getString(R.string.check_network_connection)
//                    )
                    val intent=Intent(this,MyDownloadActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d("urlstringing", "callGetMovieApi: " + "else part movie")
                    utils.simpleAlert(
                        this@HomeActivity3,
                        "Error",
                        throwable.message.toString()
                    )
                }
            })
    }

    private fun setUpSpinner() {
        binding.tvEthnicityTypeMovie.text = getString(R.string.category_select)
        val dialogView1 = LayoutInflater.from(applicationContext)
            .inflate(R.layout.layout_pop_up_category, null, false)
        val dialogBinding1: LayoutPopUpCategoryBinding = DataBindingUtil.bind(dialogView1)!!
        dialogCategory1 = CommonUtils.createCustomDialog(this, dialogBinding1.root)
        categoryAdapter = CategoriesSpinnerAdapter(applicationContext, categoryList_tv_show!!)
        dialogBinding1.rvCategory.adapter = categoryAdapter
        categoryAdapter!!.setOnItemClickListener(object :
            CategoriesSpinnerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                if (position > 0) {
                    val model = categoryList_tv_show!![position]
                    val intent = Intent(applicationContext, FilterActivity::class.java)
                    intent.putExtra("categoryName", model.title)
                    intent.putExtra("cat_id", model.id.toString())
                    intent.putExtra("type", "1")
                    startActivity(intent)
                }
                dialogCategory1!!.dismiss()
            }
        })
    }

    private fun getReadNotification(type: String) {
        apiService.read_notification(
            "Bearer " + userPref.getToken(),
            userPref.getSubUserId().toString(),
            type
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.user_data != null) {
                    userPref1.setNotificationdot(true)
                    adapter = NavExpandableListAdapter(
                        this,
                        headerList as ArrayList<DashboardHeaderMenuModel>,
                        listDataChild!!, userPref1.getNotificationdot()
                    )
                    adapter.notifyDataSetChanged()
                }
            }, { throwable ->

            })
    }

    private fun getCountryCode(countryName: String): String? {
        val isoCountryCodes = Locale.getISOCountries()
        for (code in isoCountryCodes) {
            val locale = Locale("", code)
            if (countryName.equals(locale.displayCountry, ignoreCase = true)) {
                return code
            }
        }
        return ""
    }
    private fun getCountryName(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = telephonyManager.networkCountryIso.toUpperCase(Locale.getDefault())
        getCountryCode(Locale("", countryCode).displayCountry)
        return Locale("", countryCode).displayCountry
    }

}