package com.mamboflix.ui.activity

import android.app.Dialog
import android.os.Bundle
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.mamboflix.R
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Tracks
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroup
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.common.collect.ImmutableList
import com.mamboflix.BaseActivity
import com.mamboflix.HomeActivity3.HomeActivity3
import com.mamboflix.databinding.ActivityTestHlsactivityBinding
import com.mamboflix.databinding.DialogReportBinding
import com.mamboflix.databinding.LayoutPopUpWindowBinding
import com.mamboflix.databinding.LayoutPopUpWindowPlaybackSpeedBinding
import com.mamboflix.databinding.LayoutPopUpWindowSubtitlesBinding
import com.mamboflix.model.SubtitleModel
import com.mamboflix.ui.adapter.SubtitlesListAdapter
import com.mamboflix.utils.RelativePopupWindow
import com.mamboflix.utils.track_selection_dialog.TrackSelectionDialog

import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

class TestHLSActivity : BaseActivity(), Player.Listener, View.OnClickListener {
    private var cDialog: Dialog? = null
    private lateinit var simpleExoplayer: SimpleExoPlayer
    private lateinit var player: ExoPlayer

    private var playbackPosition: Long = 0
    private var mp4Url =
        "https://cdn.ottanalytics.com/2f008558-f194-40ba-bc25-89f7fc21e350/playlist.m3u8"
    private val dashUrl =
        "https://cdn.ottanalytics.com/2f008558-f194-40ba-bc25-89f7fc21e350/playlist.m3u8"
    private val urlList = listOf(mp4Url to "default", dashUrl to "dash")
    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)))
    }

    private var playerView: PlayerView? = null
    private var fullscreenButton: ImageView? = null
    private var forwardButton: ImageView? = null
    private var backwardButton: ImageView? = null
    private var fullscreen = false
    private var rowIndex = 0

    private lateinit var settingPopUp: RelativePopupWindow
    private lateinit var qualityPopUp: RelativePopupWindow
    private lateinit var playbackSpeedPopUp: RelativePopupWindow
    private lateinit var subtitlesPopUp: RelativePopupWindow
    private lateinit var subtitleList: ArrayList<SubtitleModel>
    private var settingPopUpBinding: LayoutPopUpWindowBinding? = null
    private var playbackSpeed: AppCompatRadioButton? = null
    private var videoQuality: String? = null
    private var update: TrackSelectionDialog = TrackSelectionDialog()
    private var contentId = ""
    private var contentType = ""
    private var infoTitle = ""
    private var TAG = "Player"
    private var isShowingTrackSelectionDialog = false
    var trackSelector: DefaultTrackSelector? = null
    private var trackSelectorParameters: DefaultTrackSelector.Parameters? = null
    var lastSeenTrackGroupArray: TrackGroupArray? = null
    val ABR_ALGORITHM_EXTRA = "abr_algorithm"
    val ABR_ALGORITHM_DEFAULT = "default"
    val ABR_ALGORITHM_RANDOM = "random"

    private var tvinfo: TextView? = null
    private var exo_settings: ImageView? = null
    private var icCut: ImageView? = null

    private lateinit var controllerView: View

    private var binding: ActivityTestHlsactivityBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_hlsactivity)



        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_hlsactivity)

        subtitleList = ArrayList()

        controllerView = LayoutInflater.from(this)
            .inflate(R.layout.exo_playback_control_preview_view, null)


        tvinfo = controllerView.findViewById(R.id.tvInfo)
        exo_settings = controllerView.findViewById(R.id.exo_settings)


        if (intent.hasExtra("SubtitleList")) {
            subtitleList.addAll(intent.getParcelableArrayListExtra("SubtitleList")!!)
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        trackSelectorParameters = DefaultTrackSelector.ParametersBuilder().build()

        val bandel = intent.extras
        if (bandel != null) {
            mp4Url = bandel.getString("playUrl").toString()
            contentId = bandel.getString("contentId").toString()
            infoTitle = bandel.getString("title").toString()
            contentType = bandel.getString("type").toString()
            playbackPosition = bandel.getString("watch_duration")?.toLong()!!



            Log.d("TAG", "onCreate: " + mp4Url + "    bbbvbvb " + playbackPosition)

            //   mp4Url= "http://demo.unified-streaming.com/video/tears-of-steel/tears-of-steel.ism/.m3u8"
        }

        controllerView.findViewById<TextView?>(R.id.tv_skip)?.setOnClickListener() {
            onBackPressed()
        }


    }
    private fun defaultFullScreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val params =
            binding!!.exoplayerView.layoutParams as RelativeLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding!!.exoplayerView.layoutParams = params
        //fullscreen = true
    }


    override fun onStart() {
        super.onStart()

        defaultFullScreen()

        initializePlayer()

        //Log.e("subtitle","<<>> "+binding!!.exoplayerView.subtitleView!!.resources)
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer() {
        val abrAlgorithm = intent.getStringExtra(ABR_ALGORITHM_EXTRA)

        // Create a DefaultTrackSelector
        val trackSelector = DefaultTrackSelector(this).apply {
            // Set parameters based on the chosen ABR algorithm
            when {
                abrAlgorithm == null || abrAlgorithm == ABR_ALGORITHM_DEFAULT -> {
                    // Use default adaptive track selection
                    setParameters(buildUponParameters())
                }
                abrAlgorithm == ABR_ALGORITHM_RANDOM -> {
                    // Handle random selection logic if needed
                    // Note: Implement custom logic if necessary since there's no direct RandomTrackSelection in newer ExoPlayer versions
                }
                else -> {
                    finish() // Exit if invalid algorithm
                    return // Exit the function
                }
            }
        }

        // Initialize the ExoPlayer with the track selector
        simpleExoplayer = SimpleExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()

        // If needed, set parameters here after initializing the player
        trackSelector.parameters = trackSelectorParameters ?: trackSelector.parameters // Handle nullable safely

        // Initialize last seen track group array
        lastSeenTrackGroupArray = null

        if (contentId == "36") {
            preparePlayerStaticVideo(mp4Url, "dasheee")
        } else {
            preparePlayer(mp4Url, "dashee")
        }


        if (userPref.getSubtitles()) {
            if (subtitleList.isNotEmpty()) {
                setSelectedSubtitle(
                    subtitleList[0].subtitle, mp4Url,
                    "dashee"
                )
            }
        }


        tvinfo?.text = infoTitle
        binding!!.exoplayerView.player = simpleExoplayer
        simpleExoplayer.seekTo(playbackPosition * 1000)
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.addListener(this)
        fullscreenButton = binding!!.exoplayerView.findViewById(R.id.exo_fullscreen_icon)

        exo_settings?.setOnClickListener {
            settingPopUp()
        }

        icCut?.setOnClickListener {
            if (contentId != "") {
                val videoWatchedTime = simpleExoplayer.currentPosition / 1000
                callMakeWatchHistoryAPI(contentId, videoWatchedTime.toString(), contentType)
            }
            startActivity(Intent(this, HomeActivity3::class.java))
            finish()
        }

        callResumeVideoApi()
    }


    private fun updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector!!.parameters
        }
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return when (type.lowercase()) { // Use lowercase for case-insensitive comparison
            "dash" -> DashMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri)) // Use MediaItem for modern APIs
            "hls" -> com.google.android.exoplayer2.source.hls.HlsMediaSource.Factory(
                dataSourceFactory
            )
                .createMediaSource(MediaItem.fromUri(uri))

            "progressive" -> ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))

            else -> throw IllegalArgumentException("Unsupported media type: $type") // Handle unsupported types
        }
    }


    private fun preparePlayer(videoUrl: String, type: String) {
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer.prepare(mediaSource)
        simpleExoplayer.playWhenReady = true

//        defaultFullScreen()

    }

    private fun preparePlayerStaticVideo(videoUrl: String, type: String) {
        val uri =
            Uri.parse("https://cdn.ottanalytics.com/85a286cc-a902-440f-8897-09b6aed284d5/playlist.m3u8")
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer.prepare(mediaSource)
        simpleExoplayer.playWhenReady = true


    }


    fun setSelectedSubtitle(subtitle: String?, videoUrl: String, type: String) {
        playbackPosition = simpleExoplayer.currentPosition
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
            simpleExoplayer.setMediaItem(mediaItem)
            simpleExoplayer.seekTo(playbackPosition)
            simpleExoplayer.prepare()
        } else {
            Toast.makeText(this, "There is no subtitle", Toast.LENGTH_SHORT).show()
        }
    }

    private fun releasePlayer() {
        updateTrackSelectorParameters()
        playbackPosition = simpleExoplayer.currentPosition
        simpleExoplayer.release()


    }

    override fun onPlayerError(error: PlaybackException) {
        // handle error
    }

    @Deprecated("Deprecated in Java")
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
            binding!!.progressBar.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            binding!!.progressBar.visibility = View.INVISIBLE
    }

    override fun onTracksChanged(tracks: Tracks) {
        val trackGroups = tracks.groups
        // Check if the track groups have changed
        if (lastSeenTrackGroupArray == null || !trackGroupsAreEqual(trackGroups, lastSeenTrackGroupArray)) {
            // Get the current mapped track info
            val mappedTrackInfo = trackSelector?.getCurrentMappedTrackInfo() ?: return

            // Check if video tracks are unsupported
            if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO) ==
                MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS
            ) {
                videoQuality = trackGroups[0].toString() // Set video quality based on the first track group
            }


            for (i in 0 until trackGroups.size) {
                TrackGroup(trackGroups[i].getTrackFormat(i))
            }

            val trackGroupArray = Array(trackGroups.size) { i ->
                val group = trackGroups[i]

                // Access formats correctly

                val formats = Array(group.length) { j -> group.getTrackFormat(j) }
                // Log formats to see what we are working with
                Log.d("TrackGroups", "Group $i has formats: ${formats.joinToString()}")

                // Create a TrackGroup with the retrieved formats
                TrackGroup(*formats)
            }

//            lastSeenTrackGroupArray = trackGroupArray
            lastSeenTrackGroupArray = TrackGroupArray(*trackGroupArray)
        }
    }

    private fun trackGroupsAreEqual(newGroups: ImmutableList<Tracks.Group>, oldGroups: TrackGroupArray?): Boolean {
        // Check if oldGroups is null or the lengths differ
        if (oldGroups == null || newGroups.size != oldGroups.length) {
            return false
        }

        // Compare each group in newGroups with corresponding group in oldGroups
        for (i in newGroups.indices) {
            val newGroup = newGroups[i]
            val oldGroup = oldGroups[i]

            // Check if the lengths of the tracks in both groups are equal
            if (newGroup.length != oldGroup.length) {
                return false
            }

            // Compare individual tracks
            for (j in 0 until newGroup.length) {
                if (newGroup.getTrackFormat(j) != oldGroup.getFormat(j)) {
                    return false
                }
            }
        }
        return true
    }


    override fun onBackPressed() {
        super.onBackPressed()

        if (contentId != null && contentId != "") {
            val videoWatchedTime = simpleExoplayer.currentPosition / 1000
            callMakeWatchHistoryAPI(contentId, videoWatchedTime.toString(), contentType)
        }


        /* if(fullscreen) {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
 //                fullscreenButton!!.setImageDrawable(getDrawable(R.drawable.ic_fullscreen_open))
             } else {
             //    fullscreenButton!!.setImageResource(R.drawable.ic_fullscreen_open)
             }
             window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
             if (supportActionBar != null) {
                 supportActionBar!!.show()
             }
             requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
             val params: RelativeLayout.LayoutParams =
                 binding!!.exoplayerView.layoutParams as RelativeLayout.LayoutParams
             params.width = ViewGroup.LayoutParams.MATCH_PARENT
             params.height =
                 ((200 * applicationContext.resources.displayMetrics.density).toInt())
             binding!!.exoplayerView.layoutParams = params
             fullscreen = false
         }else{
             super.onBackPressed()
         }*/

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            /* R.id.exo_ffwd->{
                 forwardVideo()
             }

             R.id.exo_rew->{
                 backwardVideo()
             }*/
            R.id.tvPopUpSetting1 -> {
                settingPopUp.dismiss()
                showDialogReport("Report")
                //callReportAPI(contentId)
            }

            R.id.tvPopUpSetting2 -> {
                settingPopUp.dismiss()
                if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(
                        trackSelector!!
                    )
                )
                {
                    isShowingTrackSelectionDialog = true
                    val trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(
                        trackSelector
                    ) { dismissedDialog ->
                        isShowingTrackSelectionDialog = false
                    }
                    trackSelectionDialog.show(supportFragmentManager, null)
                }
            }

            R.id.tvPopUpSetting3 -> {

                if (userPref.getSubtitles()) {
                    settingPopUp.dismiss()
                    subtitlesPopUp()
                } else {
                    Toast.makeText(
                        this@TestHLSActivity,
                        "Please enable subtitle from setting",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


            R.id.tvPopUpSetting4 -> {
                settingPopUp.dismiss()
                playBackSpeedPopUp()

            }

        }
    }


    private fun settingPopUp() {
        val popupView: View = layoutInflater.inflate(R.layout.layout_pop_up_window, null)
        settingPopUpBinding = DataBindingUtil.bind(popupView)!!
        settingPopUp =
            utils.showPopup(exo_settings, settingPopUpBinding!!.root)
        settingPopUpBinding!!.tvPopUpSetting2.text = update.update()

        //Specify the length and width through constants
        /*val width: Int = 280
        val height: Int = LinearLayout.LayoutParams.WRAP_CONTENT
        //Make Inactive Items Outside Of PopupWindow
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.showAtLocation(settingPopUpBinding!!.root, Gravity.CENTER, 0, 0)*/
        /* if(videoQuality!=null) {
         }*/
        if (subtitleList.isNotEmpty()) {
            if (userPref.getSubtitles()) {
                settingPopUpBinding!!.tvPopUpSetting3.text = subtitleList[rowIndex].language
            } else {
                settingPopUpBinding!!.tvPopUpSetting3.text = "Off"
            }
        } else {
            settingPopUpBinding!!.tvPopUpSetting3.text = "No Subtitle"
            settingPopUpBinding!!.tvPopUpSetting3.isEnabled = false
        }
        if (playbackSpeed != null) {
            settingPopUpBinding!!.tvPopUpSetting4.text = playbackSpeed!!.text
        }
        settingPopUpBinding!!.tvPopUpSetting1.setOnClickListener(this)
        settingPopUpBinding!!.tvPopUpSetting2.setOnClickListener(this)
        settingPopUpBinding!!.tvPopUpSetting3.setOnClickListener(this)
        settingPopUpBinding!!.tvPopUpSetting4.setOnClickListener(this)
    }

    private fun playBackSpeedPopUp() {
        val popupView: View =
            layoutInflater.inflate(R.layout.layout_pop_up_window_playback_speed, null)
        val popUpBinding: LayoutPopUpWindowPlaybackSpeedBinding = DataBindingUtil.bind(popupView)!!
        playbackSpeedPopUp =
            utils.showPopup(exo_settings, popUpBinding.root)
        if (playbackSpeed == null) {
            playbackSpeed = popUpBinding.cb3PlaybackPopUp
        }
        popupView.findViewById<AppCompatRadioButton>(playbackSpeed!!.id).isChecked = true
        popUpBinding.rgPlaybackSpeed.setOnCheckedChangeListener { group, checkedId ->
            val rbChecked = popupView.findViewById<AppCompatRadioButton>(checkedId)
            rbChecked.id
            when (rbChecked.id) {

                R.id.cb1PlaybackPopUp -> {
                    setSpeed(0.5f)
                }

                R.id.cb2PlaybackPopUp -> {
                    setSpeed(0.75f)
                }

                R.id.cb3PlaybackPopUp -> {
                    setSpeed(1f)
                }

                R.id.cb4PlaybackPopUp -> {
                    setSpeed(1.25f)
                }

                R.id.cb5PlaybackPopUp -> {
                    setSpeed(1.5f)
                }
            }

            playbackSpeed = rbChecked

            playbackSpeedPopUp.dismiss()


        }

    }

    fun setSpeed(speed: Float) {
        /* val parameters = PlaybackParameters(speed, 1f)
         simpleExoplayer.setPlaybackParameters(parameters)*/

        val parameters = PlaybackParameters(speed, 1f)
        simpleExoplayer.setPlaybackParameters(parameters)
    }

    /*  private fun videoQualitySpeedPopUp(){
          val popupView: View = layoutInflater.inflate(R.layout.layout_pop_up_window_quality,null)
          val popUpBinding:LayoutPopUpWindowQualityBinding= DataBindingUtil.bind(popupView)!!
        qualityPopUp=  utils.showPopup(binding!!.exoplayerView.exo_setting_button,popUpBinding.root)

          if(videoQuality==null) {
              videoQuality = popUpBinding.cb1QualityPopUp
          }
          popupView.findViewById<AppCompatRadioButton>(videoQuality!!.id).isChecked=true
          popUpBinding.rgVideoQuality.setOnCheckedChangeListener { group, checkedId ->

              val rbChecked= popupView.findViewById<AppCompatRadioButton>(checkedId)
              videoQuality=rbChecked
              qualityPopUp.dismiss()

          }
      }*/

    private fun subtitlesPopUp() {
        val popupView: View = layoutInflater.inflate(R.layout.layout_pop_up_window_subtitles, null)
        val popUpBinding: LayoutPopUpWindowSubtitlesBinding = DataBindingUtil.bind(popupView)!!
        subtitlesPopUp = utils.showPopup(exo_settings, popUpBinding.root)
        popUpBinding.rvSubtitles.setOnClickListener(this)
        val adapterSubtitles = SubtitlesListAdapter(this, subtitleList, rowIndex)
        popUpBinding.rvSubtitles.adapter = adapterSubtitles
        adapterSubtitles.setOnItemClickListener(object : SubtitlesListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                when (view.id) {
                    R.id.cbSubtitlesItemPopup -> {
                        /* if (userPref.getSubtitles()) {*/
                        settingPopUpBinding!!.tvPopUpSetting1.text = subtitleList[position].language
                        rowIndex = position
                        setSelectedSubtitle(subtitleList[position].subtitle, mp4Url, "dashee")
                        adapterSubtitles.notifyDataSetChanged()
                        // }else{
                        // Toast.makeText(this@PlayerViewActivity, "Please enable subtitle from setting", Toast.LENGTH_SHORT).show()
                        // }

                        subtitlesPopUp.dismiss()
                    }

                }

            }

        })
    }

    private fun callReportAPI(contentId: String, msg: String) {
        var token = ""
        if (userPref.getFcmToken() != null) {
            token = userPref.getFcmToken().toString()
        }

        apiService.callReportContentApi("Bearer " + userPref.getToken()!!, contentId, msg, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)

            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
                    //utils.simpleAlert(this@PlayerViewActivity, "Success.", commonResponse.message!!)
                    Toast.makeText(
                        this@TestHLSActivity,
                        commonResponse.message!!,
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    /*  utils.simpleAlert(
                              this,
                              "Error",
                              commonResponse.message.toString()
                      )*/
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
                        getString(R.string.check_network_connection)
                    )
                }
            })
    }


    private fun callMakeWatchHistoryAPI(contentId: String, duration: String, cType: String) {
        apiService.callMakeWatchHistoryApi(
            "Bearer " + userPref.getToken()!!,
            userPref.getSubUserId()!!,
            contentId,
            duration,
            userPref.getFcmToken()!!,
            cType,
            userPref.getPreferredLanguage()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*.doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)*/

            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
                    //utils.simpleAlert(this@PlayerViewActivity, "Report this video Successfully.", commonResponse.message!!)
                } else {

                    /*  utils.simpleAlert(
                              this,
                              "Error",
                              commonResponse.message.toString()
                      )*/
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
                        getString(R.string.check_network_connection)
                    )
                }
            })
    }

    private fun callResumeVideoApi() {
        apiService.callResumeVideoApi(
            "Bearer " + userPref.getToken()!!,
            userPref.user.id, contentId, contentType, userPref.getFcmToken()!!
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*.doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)*/

            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
                    binding!!.exoplayerView.player = simpleExoplayer
                    //simpleExoplayer.seekTo(playbackPosition)
                    simpleExoplayer.seekTo(commonResponse.mdata!!.watch_duration.toLong() * 1000)
                    //utils.simpleAlert(this@PlayerViewActivity, "Report this video Successfully.", commonResponse.message!!)
                } else {

                    /*  utils.simpleAlert(
                              this,
                              "Error",
                              commonResponse.message.toString()
                      )*/
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
                        getString(R.string.check_network_connection)
                    )
                }
            })
    }

    interface updateTrack {
        fun update(): String
    }


    private fun showDialogReport(topTitle: String) {
        cDialog = Dialog(this, R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogReportBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_report,
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
        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
        }

        bindingDialog.btSubmit.setOnClickListener {

            if (bindingDialog.etProfile.text.toString().isNotEmpty()) {
                cDialog!!.dismiss()
                callReportAPI(contentId, bindingDialog.etProfile.text.toString())
            } else {
                utils.simpleAlert(this, "Error", "Please enter report reason")
            }

        }
    }
}