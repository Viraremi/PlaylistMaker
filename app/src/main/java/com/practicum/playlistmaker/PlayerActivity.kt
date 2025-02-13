package com.practicum.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.ui.SearchActivity
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }

    private var playerState = STATE_DEFAULT
    private lateinit var playBtn: ImageView
    private lateinit var timer: TextView
    private var mediaPlayer = MediaPlayer()
    private var handler: Handler? = null

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playBtn.setImageResource(R.drawable.button_play)
            timer.text = getString(R.string.player_timer_zero_zero)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        playBtn.setImageResource(R.drawable.button_pause)
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        playBtn.setImageResource(R.drawable.button_play)
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> { pausePlayer() }
            STATE_PREPARED, STATE_PAUSED -> { startPlayer() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handler = Handler(Looper.getMainLooper())

        playBtn = findViewById(R.id.player_btn_play)
        timer = findViewById(R.id.player_current_time)
        val btnBack = findViewById<ImageView>(R.id.player_back)
        val artView = findViewById<ImageView>(R.id.player_art)
        val trackNameView = findViewById<TextView>(R.id.player_track_name)
        val trackArtistView = findViewById<TextView>(R.id.player_track_artist)
        val infoTimeView = findViewById<TextView>(R.id.player_track_time_value)
        val infoAlbumView = findViewById<TextView>(R.id.player_track_album_value)
        val infoYearView = findViewById<TextView>(R.id.player_track_year_value)
        val infoGenreView = findViewById<TextView>(R.id.player_track_genre_value)
        val infoCountryView = findViewById<TextView>(R.id.player_track_country_value)

        btnBack.setOnClickListener{
            finish()
        }

        val track = intent.getSerializableExtra(SearchActivity.PLAYER_INTENT_KEY) as Track
        Glide.with(this)
            .load(track.getCoverArtwork())
            .centerCrop()
            .placeholder(R.drawable.placeholder_big)
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.player_art_corner_radius)))
            .into(artView)
        trackNameView.text = track.trackName
        trackArtistView.text = track.artistName
        infoTimeView.text = track.timeValidFormat()
        infoAlbumView.text = track.collectionName
        infoYearView.text = track.releaseDate.substring(0, 4)
        infoGenreView.text = track.primaryGenreName
        infoCountryView.text = track.country
        preparePlayer(track.previewUrl)

        playBtn.setOnClickListener{
            playbackControl()
            handler?.post(createUpdateTimerTask())
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        handler?.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler?.removeCallbacksAndMessages(null)
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    val elapsedTime = mediaPlayer.getCurrentPosition()
                    timer.text = dateFormat.format(elapsedTime.toLong())
                    handler?.postDelayed(this, DELAY)
                }
            }
        }
    }

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
}