package com.mamboflix.ui.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.BaseFragment
import com.mamboflix.R
import com.mamboflix.databinding.FragMoviesTabBinding
//import com.mamboflix.databinding.InfoBottomSheetBinding
//import com.mamboflix.databinding.LayoutPopUpCategoryBinding
import com.mamboflix.model.MoviesAndShowsCategory
import com.mamboflix.model.hometab.CategoryContentModel
import com.mamboflix.model.hometab.preview.PreviewModel
import com.mamboflix.model.hometab.topbanner.TopBannerContentModel
import com.mamboflix.model.hometab.topbanner.TopBannerModel
import com.mamboflix.ui.adapter.categories.CategoriesSpinnerAdapter
import com.mamboflix.ui.adapter.dashboard.MovieAllAdapter
import com.mamboflix.ui.adapter.dashboard.PreviewsAdapter

@Suppress("UNREACHABLE_CODE")
class MoviesTabFragment : BaseFragment(), View.OnClickListener {
    private val pingActivityRequestCode = 1001
    private var binding: FragMoviesTabBinding? = null
    private var previewsAdapter: PreviewsAdapter? = null
    private var movieAllAdapter: MovieAllAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    var catgorie_content: ArrayList<CategoryContentModel>? = null
    var preview: ArrayList<PreviewModel>? = null
    private var bannerUrl: String = ""
    private var topBannerModel: TopBannerContentModel? = null
    private var bannerMain: TopBannerModel? = null
    var isAddList: Boolean = false
    private var categoryAdapter: CategoriesSpinnerAdapter? = null
    private var categoryList: ArrayList<MoviesAndShowsCategory>? = null
    private var dialogCategory: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catgorie_content = ArrayList()
        preview = ArrayList()
        categoryList = ArrayList()
//
//        if (userPref.get_check_api()=="2"){
//
//        }else{
//        callGetMovieApi()
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_movies_tab, container, false)
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        setHasOptionsMenu(true)
//
//        binding!!.roomDetailsAppBarLayout.visibility = View.GONE
//        binding!!.nsvMain.visibility = View.GONE
//        binding!!.lytTopAnimation.visibility = View.VISIBLE
//        binding!!.shimmerFrameLayout.visibility = View.VISIBLE
//        binding!!.shimmerFrameLayout.startShimmer()

        return binding!!.root
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as HomeActivity).changeIcon(false)
        //(requireActivity() as HomeActivity).changeIcon(true)
        //(requireActivity() as HomeActivity).fragmenttitle("Home", true)
//        setClick()
//        setRecyclerview()
//        setUpSpinner()
        /* binding!!.swipeContainer.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

             callGetMovieApi()
         })*/
    }

    override fun onClick(v: View?) {

    }

//    private fun setClick() {
//        binding!!.btnPlay.setOnClickListener(this)
//        binding!!.tvAddList.setOnClickListener(this)
//        binding!!.ivSearch.setOnClickListener(this)
//        binding!!.tvEthnicityType.setOnClickListener(this)
//        binding!!.lytInfo.setOnClickListener(this)
//    }

//    private fun setUpSpinner() {
//        binding!!.tvEthnicityType.text = requireActivity().getString(R.string.category_select)
//        val dialogView = LayoutInflater.from(requireContext())
//            .inflate(R.layout.layout_pop_up_category, null, false)
//        val dialogBinding: LayoutPopUpCategoryBinding = DataBindingUtil.bind(dialogView)!!
//        dialogCategory = CommonUtils.createCustomDialog(requireContext(), dialogBinding.root)
//        categoryAdapter = CategoriesSpinnerAdapter(requireContext(), categoryList!!)
//        dialogBinding.rvCategory.adapter = categoryAdapter
//        categoryAdapter!!.setOnItemClickListener(object :
//            CategoriesSpinnerAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int, view: View) {
//                if (position > 0) {
//                    val model = categoryList!![position]
//                    val intent = Intent(requireContext(), FilterActivity::class.java)
//                    intent.putExtra("categoryName", model.title)
//                    intent.putExtra("cat_id", model.id.toString())
//                    intent.putExtra("type", "1")
//                    startActivity(intent)
//                }
//
//                dialogCategory!!.dismiss()
//            }
//
//        })
//
//    }

//    private fun setRecyclerview() {
//        //Previews
//        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
//        previewsAdapter = PreviewsAdapter(requireActivity(), preview!!)
//
//        binding!!.rvPreviews.layoutManager = layoutManager
//        binding!!.rvPreviews.adapter = previewsAdapter
//
//        previewsAdapter!!.setOnItemClickListener(object : PreviewsAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int, view: View) {
//                /*startActivity(Intent(requireContext(), ContentDetailsActivity::class.java)
//                    .putExtra("content_id",preview!![position].content_id)
//                    .putExtra("watch_time","0")
//                    .putExtra("episode_id","0"))*/
//
//                startActivity(
//                    Intent(requireContext(), PlayerActivity::class.java)
//                        .putExtra("content_id", preview!![position].content_id)
//                        .putExtra("watch_time", "0")
//                        .putExtra("name", preview!![position].preview_content!!.title)
//                        .putExtra("playUrl", preview!![position].preview_content!!.trailer_path)
//                        .putExtra("fragment_type", "movie")
//
//                )
//
//            }
//
//        })
//
//
//        //MovieAllAdapter
//        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        movieAllAdapter = MovieAllAdapter(requireActivity(), catgorie_content!!, "movie")
//
//        binding!!.rvAll.layoutManager = layoutManager
//        binding!!.rvAll.adapter = movieAllAdapter
//
//        movieAllAdapter!!.setOnItemClickListener(object : MovieAllAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int, view: View) {
//            }
//        })
//    }


//    private fun callGetMovieApi() {
//        categoryList!!.clear()
//        catgorie_content!!.clear()
//        preview!!.clear()
//        apiService.callGetMovieApi(
//            "Bearer " + userPref.getToken(),
//            userPref.getSubUserId().toString(), userPref.getFcmToken().toString(),
//            userPref.getPreferredLanguage()
//        )
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            /* .doOnSubscribe(this::showProgressDialog)
//             .doOnCompleted(this::hideProgressDialog)*/
//
//
//            .subscribe({ commonResponse ->
//
//                if (commonResponse.status != 0 && commonResponse.mdata != null) {
//
//                    binding!!.roomDetailsAppBarLayout.visibility = View.VISIBLE
//                    binding!!.nsvMain.visibility = View.VISIBLE
//                    binding!!.lytTopAnimation.visibility = View.GONE
//                    binding!!.shimmerFrameLayout.stopShimmer()
//                    binding!!.shimmerFrameLayout.visibility = View.GONE
//
//                    if (commonResponse.mdata.main_data.top_banner != null) {
//                        setBannerData(commonResponse.mdata.main_data.top_banner)
//                    }
//
//
//
//                    if (commonResponse.mdata.main_data.preview != null && commonResponse.mdata.main_data.preview.size > 0) {
//                        binding!!.lytPreview.visibility = View.VISIBLE
//                        preview!!.addAll(commonResponse.mdata.main_data.preview)
//                    } else {
//                        binding!!.lytPreview.visibility = View.GONE
//                    }
//
//
//                    if (commonResponse.mdata.main_data.catgorie_content != null && commonResponse.mdata.main_data.catgorie_content.size > 0) {
//                        catgorie_content!!.addAll(commonResponse.mdata.main_data.catgorie_content)
//                    } else {
//                        binding!!.rvAll.visibility = View.GONE
//                    }
//
//                    if (commonResponse.mdata.movies_category != null) {
//                        categoryList!!.add(MoviesAndShowsCategory(0, "All", "", 0, "", ""))
//                        categoryList!!.addAll(commonResponse.mdata.movies_category)
//                    }
//
//                    previewsAdapter!!.notifyDataSetChanged()
//                    movieAllAdapter!!.notifyDataSetChanged()
//
//
//                } else {
//                    // binding!!.swipeContainer.isRefreshing = false
//                    binding!!.lytTop.visibility = View.GONE
//                    utils.simpleAlert(
//                        requireContext(),
//                        "Error",
//                        commonResponse.message.toString()
//                    )
//                    //hideProgressDialog()
//                }
//
//            }, { throwable ->
//                //hideProgressDialog()
//                if (throwable is ConnectException) {
//                    utils.simpleAlert(
//                        requireContext(),
//                        "Error",
//                        getString(R.string.check_network_connection)
//                    )
//
//                } else {
//
//                    utils.simpleAlert(
//                        requireContext(),
//                        "Error",
//                        throwable.message.toString()
//                    )
//
//                    /* utils.simpleAlert(
//                         requireContext(),
//                         "Error",
//                         getString(R.string.check_network_connection))*/
//
//                }
//
//            })
//    }

//    private fun setBannerData(model: TopBannerModel) {
//        bannerMain = model
//        if (model.bnner_content != null) {
//            binding!!.listLyt.visibility = View.VISIBLE
//            Glide.with(requireActivity())
//                .load(Uri.parse(model.bnner_content.poster))
//                /*.apply(RequestOptions.fitCenterTransform())*/
//                .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
//                .apply(RequestOptions.errorOf(R.drawable.image_loading))
//                .into(binding!!.ivBanner)
//
//            topBannerModel = model.bnner_content
//            bannerUrl = model.bnner_content.path!!
//            binding!!.tvMovie.text = model.bnner_content.title
//            binding!!.tvYear.text = model.bnner_content.release_year
//            binding!!.tvTime.text = model.bnner_content.content_duration
//            binding!!.tvCategory.text = model.title
//            binding!!.tvCertificat.text = "U/A"
//
//
//            if (model.bnner_content.my_list_status == "1") {
//                isAddList = true
//                binding!!.ivMyList.setImageResource(R.drawable.ic_check_mark_blue)
//                //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
//            } else {
//                binding!!.ivMyList.setImageResource(R.drawable.add_list)
//                //binding!!.ivMyList.colorFilter = null
//            }
//
//            /* if (model.bnner_content.my_list_status == "1") {
//                 isAddList = true
//                 binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
//             } else {
//                 binding!!.ivMyList.colorFilter = null
//             }*/
//        }
//    }

    //callCategoriesAPI
//    private fun callCategoriesAPI() {
//        apiService.callCategoriesAPI(
//            "Bearer " + userPref.getToken(),
//            userPref.getPreferredLanguage(),
//            userPref.getFcmToken().toString()
//        )
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe(this::showProgressDialog)
//            .doOnCompleted(this::hideProgressDialog)
//
//            .subscribe({ commonResponse ->
//
//                if (commonResponse.status != 0 && commonResponse.mdata != null) {
//                } else {
//                    utils.simpleAlert(
//                        requireContext(),
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
//                        requireContext(),
//                        "Error",
//                        getString(R.string.check_network_connection)
//                    )
//
//                } else {
//
//                    utils.simpleAlert(
//                        requireContext(),
//                        "Error",
//                        getString(R.string.check_network_connection)
//                    )
//
//                }
//
//            })
//    }

//    override fun onClick(v: View?) {
//        when (v?.id) {
//
//            R.id.btnPlay -> {
//                try {
//                    startActivity(
//                        Intent(requireContext(), ContentDetailsActivity::class.java)
//                            .putExtra("content_id", topBannerModel!!.id)
//                            .putExtra("episode_id", "").putExtra("fragment_type", "movie")
//                    )
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//
//                    Toast.makeText(requireContext(), "You can not see", Toast.LENGTH_SHORT).show()
//                }
//                /*if (bannerUrl!=""){
//                    startActivity(Intent(requireContext(), PlayerViewActivity::class.java)
//                        .putExtra("playUrl", bannerUrl))
//                }*/
//            }
//
//            R.id.tvAddList -> {
//                isAddList = if (isAddList) {
//                    callMakeListApi(topBannerModel!!.id!!, topBannerModel!!.cat_id!!, "0")
//                    //binding!!.ivMyList.colorFilter = null
//                    binding!!.ivMyList.setImageResource(R.drawable.add_list)
//                    false
//                } else {
//                    callMakeListApi(topBannerModel!!.id!!, topBannerModel!!.cat_id!!, "1")
//                    binding!!.ivMyList.setImageResource(R.drawable.ic_check_mark_blue)
//                    //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
//                    true
//                }
//            }
//
//            R.id.lytInfo -> {
//
//                bannerMain?.let { setInfo(it) }
//                /*  startActivity(Intent(requireContext(), ContentDetailsActivity::class.java)
//                          .putExtra("content_id",topBannerModel!!.id)
//                          .putExtra("episode_id",""))*/
//
//            }
//
//            R.id.ivSearch -> {
//
//
//                val intent = Intent(requireContext(), SearchActivity::class.java)
//                intent.putExtra("contentType", "1")
//                startActivity(intent)
//
//            }
//            R.id.tvEthnicityType -> {
//
//                dialogCategory!!.show()
//            }
//
//        }
//    }

//    private fun setInfo(infoModel: TopBannerModel) {
//        val bindingDialog: InfoBottomSheetBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(requireContext()),
//            R.layout.info_bottom_sheet,
//            null,
//            false
//        )
//        /* val view: View =
//             layoutInflater.inflate(R.layout.info_bottom_sheet, null)*/
//
//        val dialog = BottomSheetDialog(requireContext())
//        dialog.setContentView(bindingDialog.root)
//        dialog.show()
//
//        bindingDialog.tvName.text = infoModel.bnner_content!!.title
//        bindingDialog.tvVideoQuality.text =
//            "Video Quality : " + infoModel.bnner_content.video_quality
//        bindingDialog.tvCategory.text = infoModel.title
//        bindingDialog.tvActor.text = "Actor : " + infoModel.bnner_content.actor_name
//        bindingDialog.tvDirector.text = "Director : " + infoModel.bnner_content.director_name
//        bindingDialog.tvWrite.text = "Writer : " + infoModel.bnner_content.writer_name
//
//
//        bindingDialog.infoPlay.setOnClickListener {
//            dialog.dismiss()
//            startActivity(
//                Intent(requireContext(), ContentDetailsActivity::class.java)
//                    .putExtra("content_id", infoModel.bnner_content.id)
//                    .putExtra("watch_time", "0")
//                    .putExtra("episode_id", "")
//                    .putExtra("fragment_type", "movie")
//            )
//
//        }
//
//    }

//    private fun callMakeListApi(contentID: String, categoryID: String, status: String) {
//        apiService.callMakeListApi(
//            "Bearer " + userPref.getToken(),
//            userPref.getSubUserId().toString(),
//            contentID,
//            categoryID,
//            status,
//            userPref.getFcmToken().toString()
//        )
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe(this::showProgressDialog)
//            .doOnCompleted(this::hideProgressDialog)
//            .subscribe({ commonResponse ->
//                if (commonResponse.status != 0) {
//
//                    Toast.makeText(
//                        requireContext(),
//                        "" + commonResponse.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    utils.simpleAlert(
//                        requireContext(),
//                        "Error",
//                        commonResponse.message.toString()
//                    )
//                    hideProgressDialog()
//                }
//            }, { throwable ->
//                hideProgressDialog()
//                if (throwable is ConnectException) {
//                    utils.simpleAlert(
//                        requireContext(),
//                        "Error",
//                        getString(R.string.check_network_connection)
//                    )
//
//                } else {
//                    utils.simpleAlert(
//                        requireContext(),
//                        "Error",
//                        throwable.message.toString()
//                    )
//                }
//            })
//    }


    //inflate the menu
    /* override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         inflater.inflate(R.menu.notification, menu)
         //super.onCreateOptionsMenu(menu, inflater)
         val alertMenuItem = menu.findItem(R.id.activity_main_alerts_menu_item)
         val rootView = alertMenuItem!!.actionView as FrameLayout
         countTextView = rootView.findViewById(R.id.view_alert_count_textview)

         rootView.setOnClickListener {
             val fragment = NotificationFragment()
             CommonUtils.setFragment(fragment, false, requireActivity(), R.id.frameContainer)
         }

                 //Log.e("notification","<<onCreateOptionsMenu Count>> "+notificationCount)

         countTextView!!.text = notificationCount

         return super.onCreateOptionsMenu(menu,inflater)
     }*/


    /* override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         inflater.inflate(R.menu.notification, menu)
         val alertMenuItem = menu.findItem(R.id.activity_main_alerts_menu_item)
         val rootView = alertMenuItem!!.actionView as FrameLayout
         countTextView = rootView.findViewById(R.id.view_alert_count_textview)
         *//*redCircle = rootView.findViewById(R.id.view_alert_red_circle)
        countTextView = rootView.findViewById(R.id.view_alert_count_textview)*//*
        //  countTextView!!.setText(Utils.isNotification)

        Log.e("notification","<<onCreateOptionsMenu Count>> "+notificationCount)

        countTextView!!.text = notificationCount


        return super.onCreateOptionsMenu(menu,inflater)
    }*/

}


