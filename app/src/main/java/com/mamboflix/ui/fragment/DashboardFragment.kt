package com.mamboflix.ui.fragment
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.MediaItem

import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory

import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mamboflix.BaseFragment
import com.mamboflix.R
import com.mamboflix.databinding.DialogSeassionOutBinding
import com.mamboflix.databinding.FragDashboardBinding
import com.mamboflix.databinding.InfoBottomSheetBinding
import com.mamboflix.model.SubtitleModel
import com.mamboflix.model.hometab.CategoryContentModel
import com.mamboflix.model.hometab.becauseyou.BecauseYouContentModel
import com.mamboflix.model.hometab.continuewatch.ContinueWatchModel
import com.mamboflix.model.hometab.mostwatched.MostWatchedModel
import com.mamboflix.model.hometab.mylist.MyListModel
import com.mamboflix.model.hometab.preview.PreviewModel
import com.mamboflix.model.hometab.topbanner.TopBannerContentModel
import com.mamboflix.model.hometab.topbanner.TopBannerModel
import com.mamboflix.model.hometab.trending.TrendingModel
import com.mamboflix.ui.activity.contentdetails.ContentDetailsActivity
import com.mamboflix.ui.activity.login.LoginActivity
import com.mamboflix.ui.activity.player.PlayerActivity
import com.mamboflix.ui.activity.search.SearchActivity
import com.mamboflix.ui.adapter.dashboard.*

import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

@Suppress("UNREACHABLE_CODE")
class DashboardFragment : BaseFragment(), View.OnClickListener {
    //https://awsrh.blogspot.com/2017/10/custom-pop-up-window-with-android-studio.html
    private val pingActivityRequestCode = 1001
    private var binding: FragDashboardBinding? =null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var layoutManager1: RecyclerView.LayoutManager? = null
    private var layoutManager2: RecyclerView.LayoutManager? = null
    private var layoutManager3: RecyclerView.LayoutManager? = null
    private var layoutManager4: RecyclerView.LayoutManager? = null
    private var layoutManager5: RecyclerView.LayoutManager? = null
    private var layoutManager6: RecyclerView.LayoutManager? = null
    private var previewsAdapter: PreviewsAdapter? = null
    private var continueWatchingAdapter: ContinueWarchingAdapter? = null
    private var movieAllAdapter: MovieAllAdapter? = null
    private var myListHomeAdapter: MyListHomeAdapter? = null
    private var becauseYouHomeAdapter: BecauseYouHomeAdapter? = null
    private var trendingNowHomeAdapter: TrendingNowHomeAdapter? = null
    private var mostWatchedHomeAdapter: MostWatchedHomeAdapter? = null
    private var catgorie_content : ArrayList<CategoryContentModel>? = null
    private var continue_watch : ArrayList<ContinueWatchModel>? = null
    private var last_based_content : ArrayList<BecauseYouContentModel>? = null
    var most_watched : ArrayList<MostWatchedModel>? = null
    var mylist : ArrayList<MyListModel>? = null
    var preview : ArrayList<PreviewModel>? = null
    var trending : ArrayList<TrendingModel>? = null
    private var bannerUrl :String = ""
    private var topBannerModel: TopBannerContentModel?=null
    private var bannerMain:TopBannerModel?=null
    var isAddList:Boolean = false
    lateinit var exoPlayer: SimpleExoPlayer
    var videoURL =
    "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"
    private var subTitleList: ArrayList<SubtitleModel>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         catgorie_content= ArrayList()
         continue_watch = ArrayList()
         last_based_content = ArrayList()
         most_watched = ArrayList()
         mylist = ArrayList()
         preview = ArrayList()
         trending = ArrayList()
//        Log.d("TAG", "onCreate:vvvvvvv "+userPref.getNotification())
        subTitleList = ArrayList()
        callDashboardApi()
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_dashboard, container, false)
        setHasOptionsMenu(true)

        binding!!.roomDetailsAppBarLayout.visibility = View.GONE
//        binding!!.nsvMain.visibility = View.GONE
        binding!!.lytTopAnimation.visibility = View.VISIBLE
        binding!!.shimmerFrameLayout.visibility = View.VISIBLE
        binding!!.shimmerFrameLayout.startShimmer()

//        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
//        val trackSelector: TrackSelector =
//            DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
     /*   exoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext(), trackSelector)

        val videoURI: Uri = Uri.parse(videoURL)
        val dataSourceFactory: DefaultHttpDataSourceFactory =
            DefaultHttpDataSourceFactory("Exoplayer_video")
        val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory();
        val mediaSourse: MediaSource =
            ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null)*/
//        binding!!.exoPlayer.player = exoPlayer
//        exoPlayer.prepare(mediaSourse)
//        exoPlayer.playWhenReady = true



        return binding!!.root


    }

//    private fun setupExoPlayer() {
//        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
//        binding?.exoPlayer?.player = exoPlayer // Ensure exoPlayer is a PlayerView in your layout
//        val mediaItem = MediaItem.fromUri(Uri.parse(videoURL))
//        exoPlayer.setMediaItem(mediaItem)
//        exoPlayer.prepare()
//        exoPlayer.playWhenReady = true
//    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClick()


        setRecyclerview()

        /*binding!!.swipeContainer.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            callDashboardApi()
        })*/
    }

    private fun setClick(){
//        binding!!.btnPlay.setOnClickListener(this)
//        binding!!.tvAddList.setOnClickListener(this)
        binding!!.ivSearch.setOnClickListener(this)
        binding!!.lytInfo.setOnClickListener(this)

    }

    private fun setUpCollapsingToolBar(){
       /* var isShow = true
        var scrollRange = -1
        binding?.roomDetailsAppBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1){
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                binding?.roomDetailsCollapsingToolbar?.title = resources.getString(R.string.app_name)
                isShow = true
            } else if (isShow){
                binding?.roomDetailsCollapsingToolbar?.title=" "
                isShow = false
            }
        })*/
    }




    private fun setRecyclerview(){
        //Previews
        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        previewsAdapter = PreviewsAdapter(requireActivity(),preview!!)
        binding!!.rvPreviews.layoutManager = layoutManager
        binding!!.rvPreviews.adapter = previewsAdapter
        previewsAdapter!!.setOnItemClickListener(object : PreviewsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
               requireActivity(). startActivity(Intent(requireContext(), PlayerActivity::class.java)
                    .putExtra("content_id",preview!![position].content_id)
                    .putExtra("watch_time","0")
                    .putExtra("name",preview!![position].preview_content!!.title)
                    .putExtra("playUrl",preview!![position].preview_content!!.trailer_path))

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
        layoutManager1 = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        continueWatchingAdapter = ContinueWarchingAdapter(requireActivity(),continue_watch!!)
        binding!!.rvContinue.layoutManager = layoutManager1
        binding!!.rvContinue.adapter = continueWatchingAdapter

        continueWatchingAdapter!!.setOnItemClickListener(object : ContinueWarchingAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
               requireActivity(). startActivity(Intent(requireContext(), ContentDetailsActivity::class.java)
                    .putExtra("content_id",continue_watch!![position].content_id)
                    .putExtra("watch_time",continue_watch!![position].watch_duration)
                    .putExtra("episode_id",continue_watch!![position].episode_id))
            }
        })

        //My List
        layoutManager2 = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        myListHomeAdapter = MyListHomeAdapter(requireActivity(),mylist!!)
        binding!!.rvMyList.layoutManager = layoutManager2
        binding!!.rvMyList.adapter = myListHomeAdapter
        myListHomeAdapter!!.setOnItemClickListener(object : MyListHomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {

                Log.d("TAG", "onItemClick: "+mylist!![position])
                requireActivity().  startActivity(Intent(requireContext(), ContentDetailsActivity::class.java)
                    .putExtra("content_id",mylist!![position].content_id)
                    .putExtra("watch_time","0")
                    .putExtra("episode_id",mylist!![position].episode_id))
            }
        })

        //Because you
        layoutManager3 = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        becauseYouHomeAdapter = BecauseYouHomeAdapter(requireActivity(),last_based_content!!)
        binding!!.rvOfYou.layoutManager = layoutManager3
        binding!!.rvOfYou.adapter = becauseYouHomeAdapter
        becauseYouHomeAdapter!!.setOnItemClickListener(object : BecauseYouHomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                requireActivity().   startActivity(Intent(requireContext(), ContentDetailsActivity::class.java)
                    .putExtra("content_id",last_based_content!![position].id)
                    .putExtra("watch_time","0")
                    .putExtra("episode_id",""))
            }
        })
        //Trending
        layoutManager4 = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        trendingNowHomeAdapter = TrendingNowHomeAdapter(requireActivity(),trending!!)
        binding!!.rvTrendingNow.layoutManager = layoutManager4
        binding!!.rvTrendingNow.adapter = trendingNowHomeAdapter
        trendingNowHomeAdapter!!.setOnItemClickListener(object : TrendingNowHomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                requireActivity().   startActivity(Intent(requireContext(), ContentDetailsActivity::class.java)
                        .putExtra("content_id",trending!![position].content_id)
                    .putExtra("watch_time","0")
                        .putExtra("episode_id",trending!![position].episode_id))
            }
        })
        //Most watched
        layoutManager5 = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mostWatchedHomeAdapter = MostWatchedHomeAdapter(requireActivity(),most_watched!!)
        binding!!.rvMostWatched.layoutManager = layoutManager5
        binding!!.rvMostWatched.adapter = mostWatchedHomeAdapter
        mostWatchedHomeAdapter!!.setOnItemClickListener(object : MostWatchedHomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                requireActivity().  startActivity(Intent(requireContext(), ContentDetailsActivity::class.java)
                        .putExtra("content_id",most_watched!![position].content_id)
                    .putExtra("watch_time","0")
                        .putExtra("episode_id",most_watched!![position].episode_id))
            }
        })
        //MovieAllAdapter
        layoutManager6 = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        movieAllAdapter = MovieAllAdapter(requireActivity(), catgorie_content!!,"dashboard")
        binding!!.rvAll.layoutManager = layoutManager6
        binding!!.rvAll.adapter = movieAllAdapter
        movieAllAdapter!!.setOnItemClickListener(object : MovieAllAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
            }
        })
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
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finishAffinity()
                } else {
                    utils.simpleAlert(
                        requireContext(),
                        "Error",
                        commonResponse.message.toString()
                    )
                    hideProgressDialog()
                }

            }, { throwable ->
                hideProgressDialog()
                if (throwable is ConnectException) {
                    utils.simpleAlert(
                        requireContext(),
                        "Error",
                        getString(R.string.check_network_connection)
                    )

                } else {
                    utils.simpleAlert(
                        requireContext(),
                        "Error",
                        throwable.message.toString())


                }
            })
    }

    private fun callDashboardApi() {
        catgorie_content!!.clear()
        continue_watch !!.clear()
        last_based_content !!.clear()
        most_watched !!.clear()
        mylist !!.clear()
        preview !!.clear()
        trending!!.clear()
        //userPref.getSubUserId().toString()
        apiService.callDashboardApi("Bearer "+userPref.getToken(),
            userPref.user.id,
            userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage(),"")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
           /* .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)*/
            .subscribe({ commonResponse ->

                if (commonResponse.login_session==1){
                    showDialogSessionOut()
                }
                if (commonResponse.status !=0 && commonResponse.mdata != null) {


                   // binding!!.swipeContainer.isRefreshing = false

                    //binding!!.lytTop.visibility = View.VISIBLE


                    binding!!.roomDetailsAppBarLayout.visibility = View.VISIBLE
                    binding!!.nsvMain.visibility = View.VISIBLE
                    binding!!.lytTopAnimation.visibility = View.GONE
                    binding!!.shimmerFrameLayout.stopShimmer()
                    binding!!.shimmerFrameLayout.visibility = View.GONE


                    commonResponse.mdata.top_banner?.let { setBannerData(it) }


                    if (commonResponse.mdata.preview!=null && commonResponse.mdata.preview.size>0){
                        binding!!.lytPreview.visibility = View.VISIBLE
                        binding!!.tvPreview.visibility = View.VISIBLE
                        preview!!.addAll(commonResponse.mdata.preview)
                    }else{
                        binding!!.lytPreview.visibility = View.GONE
                    }
                    if (commonResponse.mdata.continue_watch!=null && commonResponse.mdata.continue_watch.size>0){
                        binding!!.lytContinue.visibility = View.VISIBLE
                        continue_watch!!.addAll(commonResponse.mdata.continue_watch)
                    }else{
                        binding!!.lytContinue.visibility = View.GONE
                    }

                    if (commonResponse.mdata.mylist!=null && commonResponse.mdata.mylist.size>0){
                        binding!!.lytMyList.visibility = View.VISIBLE
                        mylist!!.addAll(commonResponse.mdata.mylist)
                    }else{
                        binding!!.lytMyList.visibility = View.GONE
                    }

                    if (commonResponse.mdata.last_based_content!=null && commonResponse.mdata.last_based_content.size>0){
                        binding!!.lytOfYou.visibility = View.VISIBLE
                        last_based_content!!.addAll(commonResponse.mdata.last_based_content)
                    }else{
                        binding!!.lytOfYou.visibility = View.GONE
                    }
                    if (commonResponse.mdata.trending!=null && commonResponse.mdata.trending.size>0){
                        binding!!.lytTrendingNow.visibility = View.VISIBLE
                        trending!!.addAll(commonResponse.mdata.trending)
                        //Log.e("trending ","<< If Size>> "+trending!!.size)
                    }else{
                       // Log.e("trending ","<<Size>> "+trending!!.size)
                        binding!!.lytTrendingNow.visibility = View.GONE
                    }
                    if (commonResponse.mdata.most_watched!=null && commonResponse.mdata.most_watched.size>0){
                        binding!!.lytMostWatched.visibility = View.VISIBLE
                        most_watched!!.addAll(commonResponse.mdata.most_watched)
                    }else{
                        binding!!.lytMostWatched.visibility = View.GONE
                    }

                    if (commonResponse.mdata.catgorie_content!=null && commonResponse.mdata.catgorie_content.size>0){
                        binding!!.rvAll.visibility = View.VISIBLE
                        catgorie_content!!.addAll(commonResponse.mdata.catgorie_content)
                    }else{
                        binding!!.rvAll.visibility = View.GONE
                    }

                    previewsAdapter!!.notifyDataSetChanged()
                    continueWatchingAdapter!!.notifyDataSetChanged()
                    movieAllAdapter!!.notifyDataSetChanged()
                    myListHomeAdapter!!.notifyDataSetChanged()
                    becauseYouHomeAdapter!!.notifyDataSetChanged()
                    trendingNowHomeAdapter!!.notifyDataSetChanged()
                   mostWatchedHomeAdapter!!.notifyDataSetChanged()

                    //setBannerData(commonResponse.mdata.top_banner)
                } else {
                    //binding!!.swipeContainer.isRefreshing = false
                    //binding!!.lytTop.visibility = View.GONE
                   /* utils.simpleAlert(
                        requireContext(),
                        "Error",
                        commonResponse.message.toString()
                    )*/
                   // hideProgressDialog()
                }

            }, { throwable ->
                //hideProgressDialog()
                if (throwable is ConnectException) {
                    utils.simpleAlert(
                        requireContext(),
                        "Error",
                        getString(R.string.check_network_connection)
                    )
                } else {

                 /*   utils.simpleAlert(
                        requireContext(),
                        "Error",
                        throwable.message.toString())*/
                    /*utils.simpleAlert(
                        requireContext(),
                        "Error",
                        getString(R.string.check_network_connection))*/
                }

            })
    }

    private fun setBannerData(model: TopBannerModel){
        bannerMain = model
        if (model.bnner_content!=null) {

            binding!!.listLyt.visibility = View.VISIBLE

            Glide.with(requireActivity())
                .load(Uri.parse(model.bnner_content.poster))
                .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
                .apply(RequestOptions.errorOf(R.drawable.image_loading))
                .into(binding!!.ivBanner)

            topBannerModel = model.bnner_content
            bannerUrl = model.bnner_content.path!!
            binding!!.tvMovie.text = model.bnner_content.title
            binding!!.tvYear.text = model.bnner_content.release_year
            binding!!.tvTime.text = model.bnner_content.content_duration
            binding!!.tvCategory.text = model.title
            binding!!.tvCertificat.text = "U/A"


            if (model.bnner_content.my_list_status=="1"){
                isAddList = true
                binding!!.ivMyList.setImageResource(R.drawable.ic_check_mark_blue)
                //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
            }else{
                binding!!.ivMyList.setImageResource(R.drawable.add_list)
                //binding!!.ivMyList.colorFilter = null
            }
        }
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPlay ->{
                try {
                    startActivity(Intent(requireContext(), ContentDetailsActivity::class.java)
                        .putExtra("content_id",topBannerModel!!.id)
                        .putExtra("watch_time","0")
                        .putExtra("episode_id",""))
                }catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "You can not see", Toast.LENGTH_SHORT).show()
                }

                 //if (bannerUrl!=""){
                     /*val bundle = Bundle()
                     bundle.putString("playUrl", bannerUrl)
                     bundle.putString("contentId", topBannerModel!!.id)
                     bundle.putString("watch_duration", "0")
                     val intent = Intent(requireContext(), PlayerViewActivity::class.java)
                     intent.putExtra("SubtitleList", subTitleList)
                     intent.putExtras(bundle)
                     startActivity(intent)*/
                    /* startActivity(Intent(requireContext(), PlayerViewActivity::class.java)
                         .putExtra("playUrl", bannerUrl))*/
                //}
            }

            R.id.tvAddList ->{
                isAddList = if (isAddList){
                    callMakeListApi(topBannerModel!!.id!!,topBannerModel!!.cat_id!!,"0")
                    //binding!!.ivMyList.colorFilter = null
                    binding!!.ivMyList.setImageResource(R.drawable.add_list)
                    false
                }else{
                    callMakeListApi(topBannerModel!!.id!!,topBannerModel!!.cat_id!!,"1")

                    //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
                    true
                }
            }

            R.id.lytInfo ->{

                        bannerMain?.let { setInfo(it) }
            }

            R.id.ivSearch->{
                val intent=Intent(requireContext(), SearchActivity::class.java)
                intent.putExtra("contentType","3")
                    .putExtra("watch_time","0")
                startActivity(intent)
            }
        }
    }


    private fun setInfo(infoModel:TopBannerModel){
        val bindingDialog: InfoBottomSheetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.info_bottom_sheet,
            null,
            false)
        /* val view: View =
             layoutInflater.inflate(R.layout.info_bottom_sheet, null)*/

        val dialog = BottomSheetDialog(requireContext(),R.style.CommentDialogTheme)

        dialog.setContentView(bindingDialog.root)
        dialog.show()

        bindingDialog.tvName.text = infoModel.bnner_content!!.title
        bindingDialog.tvVideoQuality.text = "Video Quality : "+infoModel.bnner_content!!.video_quality
        bindingDialog.tvCategory.text = infoModel.title
        bindingDialog.tvActor.text = "Actor : "+infoModel.bnner_content!!.actor_name
        bindingDialog.tvDirector.text = "Director : "+infoModel.bnner_content!!.director_name
        bindingDialog.tvWrite.text = "Writer : "+infoModel.bnner_content!!.writer_name


        bindingDialog!!.infoPlay.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(requireContext(), ContentDetailsActivity::class.java)
                .putExtra("content_id",infoModel!!.bnner_content!!.id)
                .putExtra("watch_time","0")
                .putExtra("episode_id",""))

        }

    }


    private fun callMakeListApi(contentID:String, categoryID:String,status:String) {
        apiService.callMakeListApi("Bearer "+userPref.getToken(),userPref.getSubUserId().toString(),contentID,categoryID,status,userPref.getFcmToken().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status !=0) {
                    binding!!.ivMyList.setImageResource(R.drawable.ic_check_mark_blue)
//                    Toast.makeText(requireContext(),""+commonResponse.message, Toast.LENGTH_SHORT).show()
                } else {
                    utils.simpleAlert(
                        requireContext(),
                        "Error",
                        commonResponse.message.toString()
                    )
                    hideProgressDialog()
                }
            }, { throwable ->
                hideProgressDialog()
                if (throwable is ConnectException) {
                    utils.simpleAlert(
                        requireContext(),
                        "Error",
                        getString(R.string.check_network_connection)
                    )
                } else {
                    utils.simpleAlert(
                        requireContext(),
                        "Error",
                        throwable.message.toString())
                }
            })
    }


    private fun showDialogSessionOut() {
        var cDialog = Dialog(requireContext(),R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogSeassionOutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_seassion_out,
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
        bindingDialog.tvTitle1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        bindingDialog.btSubmit.setOnClickListener {
            cDialog!!.dismiss()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
           // callLogoutApi()


        }
    }

}


