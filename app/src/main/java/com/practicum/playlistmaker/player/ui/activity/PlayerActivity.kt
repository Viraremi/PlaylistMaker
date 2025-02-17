package com.practicum.playlistmaker.player.ui.activity

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.ui.model.PlayerViewState
import com.practicum.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.activity.SearchActivity
import com.practicum.playlistmaker.util.Creator
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var playBtn: ImageView
    private lateinit var timer: TextView

    val viewModel by lazy {
        ViewModelProvider(this)[PlayerViewModel::class.java]
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
        viewModel.prepare(track.previewUrl)

        playBtn.setOnClickListener{
            viewModel.playbackControl()
        }

        viewModel.getStatePlayer().observe(this){ state ->
            when(state){
                PlayerViewState.Pause, PlayerViewState.Prepare ->
                    playBtn.setImageResource(R.drawable.button_play)
                PlayerViewState.Play ->
                    playBtn.setImageResource(R.drawable.button_pause)
            }
        }

        viewModel.getStateTimer().observe(this){ state ->
            timer.text = state
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }
}