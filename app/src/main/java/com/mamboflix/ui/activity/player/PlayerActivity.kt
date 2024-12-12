package com.mamboflix.ui.activity.player

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityPlayerBinding
import com.mamboflix.ui.activity.contentdetails.ContentDetailsActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.Float.max
import java.lang.Float.min
import java.net.ConnectException

class PlayerActivity : BaseActivity(), View.OnClickListener, Player.Listener {
    private var binding: ActivityPlayerBinding? = null
    private var contentId: String? = null
    private var mName: String? = null
    private var type_fragment: String? = null

    //Exo player
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f
    private lateinit var simpleExoplayer: SimpleExoPlayer
    private var playbackPosition: Long = 0
    val playbackPositionskip: Long = 60000
    private var watch_duration: String? = null
    private var mp4Url =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
    private val dashUrl =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
    private val urlList = listOf(mp4Url to "default", dashUrl to "dash")
    var url =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)))
    }

    private var tvSkip: TextView? = null
    private var tvinfo: TextView? = null
    private var tvTitle: TextView? = null
    private var exo_settings: ImageView? = null
    private var icCut: ImageView? = null

    private val forwardDuration: Long = 10000 // 10 seconds
    private val backwardDuration: Long = 10000 // 10 seconds
    var exo_rew : ImageButton? = null
    var exo_ffwd : ImageButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())


        exo_settings = binding!!.exoplayerView.findViewById(R.id.exo_settings)
        tvSkip = binding!!.exoplayerView.findViewById(R.id.tvSkip)
        icCut = binding!!.exoplayerView.findViewById(R.id.icCut)
        tvTitle = binding!!.exoplayerView.findViewById(R.id.tvTitle)

        exo_settings?.setOnClickListener{
            finish()
        }

        tvSkip!!.setOnClickListener {
            simpleExoplayer.seekTo(playbackPositionskip)
        }


        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        val bandel = intent.extras
        if (bandel != null) {
            contentId = bandel.getString("content_id").toString()
            watch_duration = bandel.getString("watch_time").toString()
            mp4Url = bandel.getString("playUrl").toString()
            mName = bandel.getString("name").toString()
            try {
                type_fragment = bandel.getString("fragment_type").toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        viewsapi()
        setcontrolForwardDuration()
    }

    private fun setcontrolForwardDuration()
    {
        exo_rew = binding?.exoplayerView?.findViewById(R.id.exo_rew)
        exo_ffwd = binding?.exoplayerView?.findViewById(R.id.exo_ffwd)

        exo_rew?.setOnClickListener{
            skipBackward()
        }
        exo_ffwd?.setOnClickListener{
            skipForward()
        }
    }

    private fun skipForward() {
        val currentPosition = simpleExoplayer?.currentPosition
        val newPosition = currentPosition?.plus(forwardDuration)
        if (newPosition != null) {
            simpleExoplayer?.duration?.let { newPosition.coerceAtMost(it) }
                ?.let { simpleExoplayer?.seekTo(it) }
        } // Prevent going past the end
    }

    // Method to skip backward
    private fun skipBackward() {
        val currentPosition = simpleExoplayer?.currentPosition
        val newPosition = currentPosition?.minus(backwardDuration)
        if (newPosition != null) {
            simpleExoplayer?.seekTo(newPosition.coerceAtLeast(0))
        } // Prevent going before the start
    }

   private fun viewsapi(){
        apiService.views_updateApi("Bearer "+userPref.getToken().toString(),contentId.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)

            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
                } else {

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

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10.0f))
            binding?.exoplayerView?.scaleX  = scaleFactor
            binding?.exoplayerView?.scaleY = scaleFactor
            return true
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            scaleGestureDetector.onTouchEvent(event)
        }
        return true
    }

    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        exoPlayerView(mp4Url)
    }

    public override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    private fun exoPlayerView(videoLink: String) {

        preparePlayer(videoLink, "dashee")
        binding!!.exoplayerView.player = simpleExoplayer
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady = true
        //simpleExoplayer.addListener(this)
        tvTitle?.text  = mName

        defaultFullScreen()
    }


    private fun defaultFullScreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val params = binding!!.exoplayerView.layoutParams as RelativeLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding!!.exoplayerView.layoutParams = params
        //fullscreen = true
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


   /* private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return when (type.lowercase()) { // Use lowercase for case-insensitive comparison
            "dash" -> DashMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri)) // Use MediaItem for modern APIs
            "hls" -> com.google.android.exoplayer2.source.hls.HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))
            "progressive" -> ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))
            else -> throw IllegalArgumentException("Unsupported media type: $type") // Handle unsupported types
        }
    }*/


    private fun preparePlayer(videoUrl: String, type: String) {
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer.prepare(mediaSource)
    }

    private fun releasePlayer() {
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

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.exo_settings -> {
                finish()
            }
           /* R.id.tvSkip -> {

               tvSkip?.setBackgroundResource(R.drawable.round_corner_light1)
                startActivity(
                    Intent(this, ContentDetailsActivity::class.java)
                        .putExtra("content_id", contentId)
                        .putExtra("watch_time", "0")
                        .putExtra("episode_id", "0")
                )
                finish()
            }*/

        }
    }
}