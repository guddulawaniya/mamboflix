package com.mamboflix.ui.activity.contentdetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityPlayerViewBinding
import com.mamboflix.databinding.DialogPaymentBinding
import com.mamboflix.model.SubtitleModel
import com.mamboflix.model.contentdetails.ContentModel
import com.mamboflix.model.contentdetails.EpisodeData
import com.mamboflix.model.contentdetails.MoreLikeThisData
import com.mamboflix.model.contentdetails.MovieContentDetailsModel
import com.mamboflix.model.contentdetails.RelatedContentModel
import com.mamboflix.model.contentdetails.SessionList
import com.mamboflix.ui.activity.payment.PaymentBillingActivity
import com.mamboflix.ui.activity.player.PlayerViewActivity
import com.mamboflix.ui.activity.reviews.Reviews
import com.mamboflix.ui.adapter.EpisodeListAdapter
import com.mamboflix.ui.adapter.MoreLikeThisAdapter
import com.mamboflix.ui.fragment.PlayerMoreLikeFragment
import com.mamboflix.ui.pager.PlayerPagerAdapter
import com.mamboflix.utils.videoDownloadUtils.DownloadBinder
import com.mamboflix.utils.videoDownloadUtils.DownloadService
import com.mamboflix.utils.videoDownloadUtils.DownloadUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.net.ConnectException

class ContentDetailsActivity : BaseActivity(), View.OnClickListener, Player.Listener,
    PlayerMoreLikeFragment.OnItemMoreClickListener, EpisodeListAdapter.EpisodeClick,
    MoreLikeThisAdapter.MoreLikeThisClick, SessionListAdapter.SesionItemClick {
    private var cDialog: Dialog? = null
    lateinit var binding: ActivityPlayerViewBinding
    private var validation: AwesomeValidation? = null
    private var pagerAdapter: PlayerPagerAdapter? = null
    private var currentQuality: VideoQuality = VideoQuality.AUTO
    private var session_adapter: SessionListAdapter? = null
    private lateinit var episode_adapter: EpisodeListAdapter
    private lateinit var more_like_this_adapter: MoreLikeThisAdapter
    private var mPlace: Int? = 0
    private var myliststatus: Int? = 0
    private var zero_position_id: String? = ""
    private var contentId: String? = null
    private var commonId: String? = null
    private var session_id: String? = ""
    private var type_fragment: String? = ""
    private var contentype: String? = null
    private var episodeId: String? = null
    private var content_type: String? = null
    private var content_mode: String? = null
    private var subscribed_users: String? = null

    var model: ContentModel? = null
    var listData: ArrayList<RelatedContentModel>? = null
    //    var episode_data_list: List<EpisodeData>?
    var episode_data_list: ArrayList<EpisodeData> = ArrayList()
    var session_list: ArrayList<SessionList> = ArrayList()
    var more_like_this_data_list: ArrayList<MoreLikeThisData> = ArrayList()
    var isLike: Boolean = false
    var isAddList: Boolean = false
    private var subTitleList: ArrayList<SubtitleModel>? = null
    //Exo player
    private var simpleExoPlayer: SimpleExoPlayer? = null
    private lateinit var simpleExoPlayer2: SimpleExoPlayer
    private var playbackPosition: Long = 0
    private var watch_duration: String? = null
    private var mp4Url =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
    private val dashUrl =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
    private val urlList = listOf(mp4Url to "default", dashUrl to "dash")
    var url =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name))) }
    var downloadFileName = ""
    private var downloadBinder: DownloadBinder? = null
    private var updateButtonStateHandler: Handler? = null
    private val MESSAGE_UPDATE_START_BUTTON = 2


    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            downloadBinder = service as DownloadBinder
        }
        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

    var exoZoomOut : ImageView? = null
    var exo_rew2 : ImageButton? = null
    var exo_ffwd2 : ImageButton? = null

    var exo_rew : ImageButton? = null
    var exo_ffwd : ImageButton? = null

    private val forwardDuration: Long = 10000 // 10 seconds
    private val backwardDuration: Long = 10000 // 10 seconds
    private var skipduration: String = "10000" // 10 seconds


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player_view)
        binding.lytTop.visibility = View.GONE
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.lytPlace.visibility = View.VISIBLE
        binding.shimmerFrameLayout.startShimmer()


        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        val bandel = intent.extras
        listData = ArrayList()
        subTitleList = ArrayList()

        try {
            type_fragment = intent.getStringExtra("fragment_type").toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val switchQualityButton: ImageView = findViewById(R.id.quality)
        switchQualityButton.setOnClickListener {
            showQualitySelectionDialog()
        }

        if (bandel != null) {
            contentId = bandel.getString("content_id").toString()
            episodeId = bandel.getString("episode_id").toString()
            watch_duration = bandel.getString("watch_time").toString()
            type_fragment = bandel.getString("fragment_type").toString()
            content_type = bandel.getString("content_type").toString()

            Log.d("ContentDetailsActivity", "<<watch time is >>    " + type_fragment)

            callAccessContentApi(contentId!!, "")

        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        clicListenerView()
        startAndBindDownloadService()
        initControls()
        setupPlayerViews()

    }

    override fun onResume() {
        super.onResume()
        callAccessContentApi(contentId!!, "")
    }

    // Method to skip forward
    private fun skipForward() {
        val currentPosition = simpleExoPlayer?.currentPosition
        val newPosition = currentPosition?.plus(forwardDuration)
        if (newPosition != null) {
            simpleExoPlayer?.duration?.let { newPosition.coerceAtMost(it) }
                ?.let { simpleExoPlayer?.seekTo(it) }
        } // Prevent going past the end
    }

    // Method to skip backward
    private fun skipBackward() {
        val currentPosition = simpleExoPlayer?.currentPosition
        val newPosition = currentPosition?.minus(backwardDuration)
        if (newPosition != null) {
            simpleExoPlayer?.seekTo(newPosition.coerceAtLeast(0))
        } // Prevent going before the start
    }

    private fun setupPlayerViews() {
        exoZoomOut = binding.exoplayerView2.findViewById(R.id.exoZoomOut)
        exo_rew2 = binding.exoplayerView2.findViewById(R.id.exo_rew)
        exo_ffwd2 = binding.exoplayerView2.findViewById(R.id.exo_ffwd)
        exo_rew = binding.exoplayerView.findViewById(R.id.exo_rew)
        exo_ffwd = binding.exoplayerView.findViewById(R.id.exo_ffwd)

        exo_rew2?.setOnClickListener{
            skipBackward()
        }
        exo_ffwd2?.setOnClickListener{
            skipForward()
        }

        exo_rew?.setOnClickListener{
            skipBackward()
        }
        exo_ffwd?.setOnClickListener{
            skipForward()
        }

        exoZoomOut?.setOnClickListener {
            Log.d("zoomOutButtonClick", "click zoom out button")

            // Ensure the player is not null
            simpleExoPlayer?.let {
                // Detach player from exoplayerView2 to stop playback there
                binding.exoplayerView2.player = null
                // Set the player to exoplayerView
                binding.exoplayerView.player = it

                // Restore the playback state
                it.playWhenReady = true // or false based on your needs
            }

            binding.scrollView.smoothScrollTo(0, 0)
            binding.scrollView.visibility = View.VISIBLE
            binding.exoplayerView2.visibility = View.INVISIBLE
        }

        binding.imgViewZoomIn.setOnClickListener {
            Log.d("zoomInButtonClick", "click zoom in button")

            // Ensure the player is not null
            simpleExoPlayer?.let {
                // Detach player from exoplayerView to stop playback there
                binding.exoplayerView.player = null
                // Set the player to exoplayerView2
                binding.exoplayerView2.player = it

                // Restore the playback state
                it.playWhenReady = true // or false based on your needs
            }

            binding.scrollView.smoothScrollTo(100, 100)
            binding.exoplayerView2.visibility = View.VISIBLE
            binding.scrollView.visibility = View.INVISIBLE
        }
    }



    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    private fun initializePlayer() {
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()

        val exoSettings = binding.exoplayerView.findViewById<ImageView>(R.id.exo_settings)

        exoSettings?.setOnClickListener {
            Toast.makeText(applicationContext, "Not ready to work fine", Toast.LENGTH_SHORT).show()
        }


    }

    private fun setPager(content_Id: String/*,episode_Id: String*/) {
        pagerAdapter =
            PlayerPagerAdapter(supportFragmentManager, listData!!, content_Id/*,episode_Id*/)
        binding.viewPager.adapter = pagerAdapter
        binding.tab.setupWithViewPager(binding.viewPager)
        binding.viewPager.currentItem = mPlace!!
        pagerAdapter!!.notifyDataSetChanged()

    }


    fun clicListenerView() {
        binding.lytLike.setOnClickListener(this)
        binding.lytPlay.setOnClickListener(this)
        binding.lytShare.setOnClickListener(this)
        binding.icCut.setOnClickListener(this)
        binding.ivMyList.setOnClickListener(this)
        binding.llDownload.setOnClickListener(this)
        binding.progressshow.setOnClickListener(this)
        binding.rating.setOnClickListener(this)
        binding.tvEpisode.setOnClickListener(this)
        binding.tvMoreLikeThis.setOnClickListener(this)
        // binding!!.exoplayerView.exo_setting_button.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.lytPlay -> {
                if (content_mode == "1") {
                    if (subscribed_users == "1"){
                        val bundle = Bundle()
                        bundle.putString("playUrl", model!!.path)
                        bundle.putString("contentId", contentId)
                        if (skipduration != null) {
                            bundle.putString("skipduration", skipduration)
                        } else {
                            // Handle the case where skipduration is null
                            // For example, you can set a default value or log an error
                            bundle.putString("skipduration", "10000")  // or leave it out entirely
                        }
                        bundle.putString("watch_duration", watch_duration)
                        bundle.putString("title", model!!.title)
                        bundle.putString("type", model!!.content_type)
                        intent.putExtra("SubtitleList", subTitleList)
                        val intent = Intent(this, PlayerViewActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    } else {
                        showDialogPayment(
                            getString(R.string.subscription),
                            getString(R.string.purchase_package)
                        )
                    }
                    //startActivity(Intent(this, PaymentBillingActivity::class.java))
                } else {
                    val bundle = Bundle()
                    bundle.putString("playUrl", model!!.path)
                    bundle.putString("contentId", contentId)
                    bundle.putString("watch_duration", watch_duration)
                    bundle.putString("title", model!!.title)
                    bundle.putString("type", model!!.content_type)
                    val intent = Intent(this, PlayerViewActivity::class.java)
                    intent.putExtra("SubtitleList", subTitleList)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
                //startActivity(Intent(this, PlayerActivity::class.java))
            }

            R.id.icCut -> {
                finish()
            }
            R.id.tv_episode -> {
//                episode_data_list.clear()
                binding.viewEpisode.visibility = View.VISIBLE
                binding.viewMoreLikeThis.visibility = View.GONE
                binding.rvListEpisode.visibility = View.VISIBLE
                binding.rvListMoreLike.visibility = View.GONE
//                call_episode_Api(contentId!!, zero_position_id.toString())
//                episode_adapter!!.notifyDataSetChanged()

            }
            R.id.tv_more_like_this -> {
//                more_like_this_data_list.clear()
                binding.viewMoreLikeThis.visibility = View.VISIBLE
                binding.rvListEpisode.visibility = View.GONE
                binding.rvListMoreLike.visibility = View.VISIBLE
                binding.viewEpisode.visibility = View.GONE
//                call_episode_Api(contentId!!, zero_position_id.toString())
//                more_like_this_adapter.notifyDataSetChanged()
            }
            R.id.lytLike -> {
                if (contentId != null) {
                    if (isLike) {
                        callMakeLikeApi("0", model!!.content_type)
                        isLike = false
                        binding.ivLike.colorFilter = null
                    } else {
                        callMakeLikeApi("1", model!!.content_type)
                        isLike = true
                        binding.ivLike.setColorFilter(
                            ContextCompat.getColor(this, R.color.blue),
                            android.graphics.PorterDuff.Mode.MULTIPLY
                        )
                    }
                }
            }
            R.id.ivMyList -> {
                if (isAddList == false){
                    binding.ivMyList.setImageResource(R.drawable.ic_check_mark_blue)
                    callMakeListApi("1")
                    isAddList = true
                    //binding!!.ivMyList.colorFilter = null
                }else{
                    binding.ivMyList.setImageResource(R.drawable.add_list)
                    callMakeListApi("0")
                    isAddList = false
                }
//                if (isAddList) {
//                    callMakeListApi("0")
//                    isAddList = false
//                    //binding!!.ivMyList.colorFilter = null
//                    binding!!.ivMyList.setImageResource(R.drawable.add_list)
//                } else {
//                    callMakeListApi("1")
//                    isAddList = true
//                    binding!!.ivMyList.setImageResource(R.drawable.ic_check_mark_blue)
//                    /*binding!!.ivMyList.setColorFilter(
//                        ContextCompat.getColor(this, R.color.blue),
//                        android.graphics.PorterDuff.Mode.MULTIPLY
//                    )*/
//                }

            }
            R.id.lytShare -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Mamboflix \n" + model!!.trailer_path
                    /*"Hey check out my app at: https://play.google.com/store/apps/details?id=" + this.packageName*/
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
                //model!!.trailer_path
            }
            R.id.rating -> {
                val intent = Intent(this, Reviews::class.java)
                intent.putExtra("contentId", commonId)
                intent.putExtra("contenttype", contentype)
                startActivity(intent)
            }
            R.id.progressshow -> {
                showDownloadCancelledDialog()
            }

            R.id.llDownload -> {
                if (model!!.content_mode == "1") {
                    showDialogPayment(getString(R.string.subscription), getString(R.string.purchase_package))
                } else {
                    if (userPref.getDownloadWifi()) {
                        if (!utils.haveWifiNetworkConnection()) {
                            if (url != "") {
                                callDownloadApi()

                                downloadBinder!!.startDownload(url, model!!.title, 0,userPref.getSubUserId().toString())


                                val enableButtonThread: Thread = object : Thread() {
                                    override fun run() {
                                        while (true) {
                                            try {
                                                this@ContentDetailsActivity.runOnUiThread(java.lang.Runnable {
                                                    if (downloadBinder!!.downloadManager!!.isDownloadSuccess) {
//                                                        insertVideo(userPref.getSubUserId().toString(),model!!.title,url)
                                                        binding.llDownload.isEnabled = false
                                                        binding.progressshow.visibility =
                                                            View.GONE
                                                        binding.ivDownload.visibility =
                                                            View.VISIBLE
                                                        binding.ivDownload.setImageResource(R.drawable.ic_check_mark_blue)
                                                        binding.tvDownload.text =
                                                            getString(R.string.downloaded)

                                                    } else if (downloadBinder!!.downloadManager!!.isDownloadCanceled) {
                                                        val msg = Message()
                                                        binding.progressshow.visibility = GONE
                                                        binding.ivDownload.visibility = VISIBLE
                                                        binding.llDownload.visibility = View.VISIBLE
                                                        msg.what = MESSAGE_UPDATE_START_BUTTON
                                                        updateButtonStateHandler!!.sendMessage(msg)
                                                    } else {
                                                        binding.progressshow.visibility = VISIBLE
                                                        binding.ivDownload.visibility = GONE
                                                        binding.progressBar1.progress = downloadBinder!!.downloadManager!!.lastDownloadProgress
                                                        binding.txtProgress.text = downloadBinder!!.downloadManager!!.lastDownloadProgress.toString() + "%"
                                                    }

                                                })
                                                sleep(2000)
                                            } catch (ex: Exception) {
                                                Log.e(
                                                    DownloadUtil.TAG_DOWNLOAD_MANAGER,
                                                    ex.message!!,
                                                    ex
                                                )
                                            }
                                        }
                                    }
                                }
                                enableButtonThread.start()
                            }
                        }
                        else {
                            utils.toaster("wifi network is not available in your device")
                        }
                    }
                    else {
                        callDownloadApi()

                        downloadBinder?.startDownload(url, model!!.title, 0,userPref.getSubUserId().toString())

                        val enableButtonThread: Thread = object : Thread() {
                            override fun run() {
                                while (true) {
                                    try {
                                        this@ContentDetailsActivity.runOnUiThread(java.lang.Runnable {
                                            if (downloadBinder!!.downloadManager!!.isDownloadSuccess) {
                                                binding.llDownload.isEnabled = false
                                                binding.progressshow.visibility = View.GONE
                                                binding.ivDownload.visibility = View.VISIBLE
                                                binding.ivDownload.setImageResource(R.drawable.ic_check_mark_blue)
                                                binding.tvDownload.text =
                                                    getString(R.string.downloaded)
                                            } else if (downloadBinder!!.downloadManager!!.isDownloadCanceled) {
                                                val msg = Message()
                                                binding.progressshow.visibility = GONE
                                                binding.ivDownload.visibility = VISIBLE
                                                binding.llDownload.visibility = View.VISIBLE
                                                msg.what = MESSAGE_UPDATE_START_BUTTON
                                                updateButtonStateHandler!!.sendMessage(msg)
                                            } else {
                                                binding.progressshow.visibility = VISIBLE
                                                binding.ivDownload.visibility = GONE
                                                binding.progressBar1.progress =
                                                    downloadBinder!!.downloadManager!!.lastDownloadProgress
                                                binding.txtProgress.text =
                                                    downloadBinder!!.downloadManager!!.lastDownloadProgress.toString() + "%"
                                            }

                                        })
                                        sleep(2000)
                                    } catch (ex: Exception) {
                                        Log.e(DownloadUtil.TAG_DOWNLOAD_MANAGER, ex.message!!, ex)
                                    }
                                }
                            }
                        }

                        enableButtonThread.start()
                    }
                }
            }
        }
    }




    private fun setData(contentDetails: MovieContentDetailsModel) {
        subTitleList!!.clear()
        model = contentDetails.content
        contentId = model!!.content_id
        commonId = model!!.common_id
        contentype = model!!.content_type
        if (contentype == "1") {
            binding.linearEpisode.visibility = View.GONE
            binding.viewMoreLikeThis.visibility = View.VISIBLE
            binding.rvListEpisode.visibility = View.GONE
            binding.rvListMoreLike.visibility = View.VISIBLE
        } else {

            binding.linearEpisode.visibility = View.VISIBLE
        }

        exoPlayerView(model!!.trailer_path)

        if (contentDetails.related_content.size > 0) {
            listData!!.addAll(contentDetails.related_content)
        }
        if (contentDetails.content!!.subtitle != null && contentDetails.content.subtitle!!.size > 0) {
            contentDetails.content.subtitle?.let { subTitleList!!.addAll(it) }
        }
        //subTitleList!!.addAll(contentDetails.content!!.subtitle)

        url = model!!.path

        downloadFileName = url.substring(url.lastIndexOf("/") + 1)
        val path: String =
            applicationContext.filesDir.absolutePath.toString() + "/Movies/" + downloadFileName
        val directory = File(path)
        if (directory.exists()) {
            binding.llDownload.isEnabled = false
            binding.ivDownload.setImageResource(R.drawable.ic_check_mark_blue)
        }

        val controllerView = LayoutInflater.from(this)
            .inflate(R.layout.exo_playback_control_preview_view, null)

        val txtInfo: TextView? = controllerView.findViewById(R.id.txtInfo)

        binding.txtInfo.text = model!!.title
        if (txtInfo != null) {
            txtInfo.text = ""
        }
        binding.tvMovie.text = model!!.title
        binding.tvYear.text = model!!.release_year
        binding.tvTime.text = model!!.content_duration
        binding.tvCategory.text = model!!.category_name
        //binding.tvCertificat.text = "U/A"
        binding.tvDescription.text =
            HtmlCompat.fromHtml(model!!.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.tvCast.text = "Cast: " + model!!.actor_name
        binding.tvDirector.text = "Director: " + model!!.director_name
        binding.tvWriter.text = "Writer: " + model!!.writer_name
        if (contentDetails.like_status == "1") {
            isLike = true
            binding.ivLike.setColorFilter(
                ContextCompat.getColor(this, R.color.blue),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            isLike = false
            binding.ivLike.colorFilter = null
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun callAccessContentApi(content_Id: String, episode_Id: String) {
        listData!!.clear()
        apiService.callAccessContentApi(
            "Bearer " + userPref.getToken(),
            userPref.getSubUserId().toString(),
            content_Id,
            episode_Id,
            userPref.getFcmToken().toString(),
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {

                    skipduration = commonResponse.skipduration.toString()
                    myliststatus = commonResponse.mdata.my_list_status
                    binding.lytTop.visibility = VISIBLE
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = GONE
                    binding.lytPlace.visibility = GONE
                    content_mode = commonResponse.mdata.content_mode
                    subscribed_users = commonResponse.mdata.subscribed_uses
                    if (myliststatus!!.equals(0)) {
                        isAddList = false
                        binding.ivMyList.setImageResource(R.drawable.add_list)

                        //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(this, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
                    } else {
                        isAddList = true
                        binding.ivMyList.setImageResource(R.drawable.ic_check_mark_blue)
                        //binding!!.ivMyList.colorFilter = null
                    }

                    if (commonResponse.mdata.related_content.size > 0) {
                        setPager(content_Id.toString()/*,episode_Id.toString()*/)
                        //pagerAdapter!!.notifyDataSetChanged()
                    }
                    if (commonResponse.mdata.episode.size > 0) {
                        binding.rvListSession.visibility = View.VISIBLE
                        try {
                            zero_position_id = commonResponse.mdata.episode[0].seasion_id

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        session_list.clear()
                        session_list.addAll(commonResponse.mdata.episode)
                        session_adapter = SessionListAdapter(
                            applicationContext,
                            session_list,
                            this
                        )

//                        Toast.makeText(applicationContext, ""+session_id_zero_pos, Toast.LENGTH_SHORT).show()
                        binding.rvListSession.adapter = session_adapter
                        session_adapter!!.notifyDataSetChanged()
                    }
                    call_episode_Api(contentId!!, zero_position_id!!)
                    setData(commonResponse.mdata)

                    if (commonResponse.mdata.subtitle_e?.master_su_te != null) {
                        setSelectedSubtitle(
                            commonResponse.mdata.subtitle_e?.master_su_te, mp4Url,
                            "dashe"
                        )
                    }

                } else {
                    pagerAdapter!!.notifyDataSetChanged()
                }
            }
    }


    fun setSelectedSubtitle(subtitle: String?, videoUrl: String, type: String) {
        playbackPosition = simpleExoPlayer!!.currentPosition
        val uri = Uri.parse(videoUrl)
        val contentMediaSource = buildMediaSource(uri, type)

        if (subtitle != null) {
            val subtitleUri = Uri.parse(subtitle)

            // Create a subtitle configuration using MediaItem
            val subtitleConfiguration = MediaItem.SubtitleConfiguration.Builder(subtitleUri)
                .setMimeType(MimeTypes.APPLICATION_SUBRIP)
                .setLanguage("en")
                .build()

            // Create a MediaItem for the content
            val mediaItem = MediaItem.Builder()
                .setUri(uri)
                .setSubtitleConfigurations(listOf(subtitleConfiguration))
                .build()

            // Prepare the ExoPlayer with the media item
            simpleExoPlayer!!.setMediaItem(mediaItem)
            simpleExoPlayer!!.seekTo(playbackPosition)
            simpleExoPlayer!!.prepare()
        } else {
            Toast.makeText(this, "There is no subtitle", Toast.LENGTH_SHORT).show()
        }
    }



    private fun call_episode_Api(content_Id: String, seaion_id: String) {
        apiService.more_like_this(
            "Bearer " + userPref.getToken(),
            userPref.getSubUserId().toString(),
            content_Id,
            seaion_id,
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { commonResponse ->
                if (commonResponse.status != 0 && commonResponse.mdata != null) {

                    if (commonResponse.mdata.episode.isNotEmpty()) {
                        episode_data_list.clear()
                        episode_data_list.addAll(commonResponse.mdata.episode)
                        episode_adapter =
                            EpisodeListAdapter(applicationContext, episode_data_list, this)
                        binding.rvListEpisode.adapter = episode_adapter
                        episode_adapter.notifyDataSetChanged()
                    }
                    if (commonResponse.mdata.more_like_this.isNotEmpty()) {
                        more_like_this_data_list.clear()
                        more_like_this_data_list.addAll(commonResponse.mdata.more_like_this)
                        more_like_this_adapter =
                            MoreLikeThisAdapter(
                                applicationContext,
                                more_like_this_data_list,
                                this
                            )
                        binding.rvListMoreLike.adapter = more_like_this_adapter
                        more_like_this_adapter.notifyDataSetChanged()

                    }

                }
            }
    }

    private fun callMakeLikeApi(isStatus: String, cType: String) {
        apiService.callMakeLikeApi(
            "Bearer " + userPref.getToken(),
            userPref.getSubUserId().toString(),
            model!!.cat_id,
            commonId!!,
            isStatus,
            cType,
            userPref.getFcmToken().toString()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {}
    }

    private fun callMakeListApi(isStatus: String) {
        apiService.callMakeListApi(
            "Bearer " + userPref.getToken(),
            userPref.getSubUserId().toString(),
            contentId!!,
            model!!.cat_id,
            isStatus,
            userPref.getFcmToken().toString()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {

                } else {
                    hideProgressDialog()
                }
            }, {
                hideProgressDialog()
            })
    }

    private fun callDownloadApi() {
        apiService.callDownloadApi(
            "Bearer " + userPref.getToken(),
            userPref.getSubUserId().toString(),
            model!!.cat_id,
            commonId!!,
            userPref.getFcmToken().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun exoPlayerView(videoLink: String) {
        preparePlayer(videoLink, "dashe")
        binding.exoplayerView.player = simpleExoPlayer
        simpleExoPlayer!!.seekTo(playbackPosition)
        simpleExoPlayer!!.playWhenReady = userPref.getautoplay()
    }



    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        android.util.Log.d("MediaSource", "Building media source for URI: $uri with type: $type")
        return try {
            when (type) {
                "dash" -> {
                    android.util.Log.d("MediaSource", "Creating DASH media source")
                    val mediaItem = MediaItem.fromUri(uri)  // Create a MediaItem from the Uri
                    DashMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mediaItem)  // Use the MediaItem here
                }
                else -> {
                    android.util.Log.d("MediaSource", "Creating Progressive media source")
                    val mediaItem = MediaItem.fromUri(uri)  // Create a MediaItem for progressive content
                    ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mediaItem)  // Use the MediaItem here
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MediaSource", "Error creating media source: ${e.message}", e)
            throw e // Handle the error accordingly
        }
    }


    private fun preparePlayer(videoUrl: String, type: String) {
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri, type)
        simpleExoPlayer!!.prepare(mediaSource)
    }

    private fun releasePlayer() {
        playbackPosition = simpleExoPlayer!!.currentPosition
        simpleExoPlayer!!.release()
    }

    override fun onPlayerError(error: PlaybackException) {
        Log.d("playerError",error.message.toString())
        // handle error
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING) {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBar2.visibility = View.VISIBLE
        }
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
            binding.progressBar.visibility = View.INVISIBLE
            binding.progressBar2.visibility = View.INVISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    // For Video Download

    private fun startAndBindDownloadService() {
        val downloadIntent = Intent(this, DownloadService::class.java)
        startService(downloadIntent)
        bindService(downloadIntent, serviceConnection, BIND_AUTO_CREATE)
    }


    private fun initControls() {
        if (updateButtonStateHandler == null) {
            updateButtonStateHandler = MyVeryOwnHandler(MESSAGE_UPDATE_START_BUTTON, binding)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    companion object {
        private class MyVeryOwnHandler(
            val MESSAGE_UPDATE_START_BUTTON: Int,
            val binding: ActivityPlayerViewBinding
        ) : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_UPDATE_START_BUTTON) {
                    binding.llDownload.isEnabled = true
                }
            }
        }
    }

    override fun onItemClick(contentId: String, episodeId: String) {
        binding.lytTop.visibility = View.GONE
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.lytPlace.visibility = View.VISIBLE
        binding.shimmerFrameLayout.startShimmer()

        callAccessContentApi(contentId, episodeId)

    }

    private fun showDownloadCancelledDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        // Set the title of the dialog
        dialogBuilder.setTitle("Download Canceled")

        // Set the message to inform the user
        dialogBuilder.setMessage("The video download has been canceled. Do you want to retry or cancel?")

        // Define the "Retry" button action
        dialogBuilder.setPositiveButton("Cancel") { dialog, which ->
            dialog.dismiss()  // Close the dialog
        }

        // Define the "Cancel" button action
        dialogBuilder.setNegativeButton("Cancel Downloading") { dialog, which ->
            downloadBinder!!.cancelDownload()
            dialog.dismiss()  // Close the dialog
            binding.llDownload.visibility = View.VISIBLE
        }


        // Create and show the dialog
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }



    private fun showDialogPayment(topTitle: String, titleTwo: String) {
        cDialog = Dialog(this, R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogPaymentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_payment,
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
        bindingDialog.tvTitle1.text = topTitle
        bindingDialog.tvTitle1.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        bindingDialog.tvTitle2.text = titleTwo
        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
        }

        bindingDialog.btSubmit.setOnClickListener {
            cDialog!!.dismiss()
            startActivity(Intent(this, PaymentBillingActivity::class.java))

        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun MoreLikeThisItemClick(
        position: Int,
        playUrl: String,
        contentId: String,
        watch_duration: String,
//        title: String,
        type: String,
        content_mode: Int,
        is_subscribed:Int
    ) {
        val bundle = Bundle()
        bundle.putString("playUrl", playUrl)
        bundle.putString("contentId", contentId)
        bundle.putString("watch_duration", "0")
        bundle.putString("title", title.toString())
        bundle.putString("type", type)
        Log.d("TAG", "onCreate2: " +"sk")
        if (content_mode.equals(1)) {
            if (is_subscribed.equals(1)){
                startActivity(
                    Intent(applicationContext, ContentDetailsActivity::class.java)
                        .putExtra("content_id", contentId)
                        .putExtra("watch_time", "0")
                        .putExtra("episode_id", "")
                )
            } else{
                showDialogPayment(
                    getString(R.string.subscription),
                    getString(R.string.purchase_package)
                )
            }
        }else{
            startActivity(
                Intent(applicationContext, ContentDetailsActivity::class.java)
                    .putExtra("content_id", contentId)
                    .putExtra("watch_time", "0")
                    .putExtra("episode_id", "")
            )

        }
    }

    override fun EpisodeItemClick(
        position: Int,
        playUrl: String,
        contentId: String,
        watch_duration: String,
//        title: String,
        type: String,
        content_mode: Int


    ) {
        val bundle = Bundle()
        bundle.putString("playUrl", playUrl)
        bundle.putString("contentId", contentId)
        bundle.putString("watch_duration", "0")
//        bundle.putString("title", title)
        bundle.putString("type", type)

        if (content_mode.equals(1)) {
            showDialogPayment(
                getString(R.string.subscription),
                getString(R.string.purchase_package)
            )
        } else{
            startActivity(
                Intent(applicationContext, ContentDetailsActivity::class.java)
                    .putExtra("content_id", contentId)
                    .putExtra("watch_time", "0")
                    .putExtra("episode_id", "")
            )
        }

    }

    override fun click_seasion_item(position: Int, seasion_id: String) {
        call_episode_Api(contentId!!, seasion_id.toString())
    }

    private fun updateMediaSource(videoUrl: String) {
        val mediaSource = buildMediaSource(Uri.parse(videoUrl),"")
        simpleExoPlayer!!.prepare(mediaSource)
        simpleExoPlayer!!.playWhenReady = true
    }

    private fun showQualitySelectionDialog() {
        val qualityNames = VideoQuality.values().map { it.label }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Select Video Quality")
            .setSingleChoiceItems(qualityNames, currentQuality.ordinal) { dialog, which ->
                currentQuality = VideoQuality.values()[which]
                updateMediaSource(currentQuality.url)
                dialog.dismiss()
            }
            .show()
    }
}

enum class VideoQuality(val label: String, val url: String) {
    AUTO("Auto", "your_auto_quality_url"),
    QUALITY_1080P("1080p", "your_1080p_url"),
    QUALITY_720P("720p", "your_720p_url"),
    QUALITY_480P("480p", "your_480p_url"),
    QUALITY_360P("360p", "your_360p_url"),
    QUALITY_240P("240p", "your_240p_url"),
    QUALITY_144P("144p", "your_144p_url")
}