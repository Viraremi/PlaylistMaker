package com.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.TimeFormatter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_ID = "player_intent_key"
    }

    private lateinit var binding: ActivityPlayerBinding
    val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.playerBack.setOnClickListener{ onBackPressedDispatcher.onBackPressed() }

        var track: Track? = null

        try {
            val trackId = intent.getIntExtra(TRACK_ID, -1)
            track = viewModel.getTrackById(trackId)
        } catch (e: Exception) {
            val json = intent.getStringExtra(TRACK_ID)
            track = Gson().fromJson(json, object : TypeToken<Track>() {}.type)
        }

        Glide.with(this)
            .load(track!!.getCoverArtwork())
            .centerCrop()
            .placeholder(R.drawable.placeholder_big)
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.player_art_corner_radius)))
            .into(binding.playerArt)
        binding.playerTrackName .text = track.trackName
        binding.playerTrackArtist .text = track.artistName
        binding.playerTrackTimeValue.text = TimeFormatter.getValidTimeFormat(track.trackTimeMillis.toLong())
        binding.playerTrackAlbumValue.text = track.collectionName
        binding.playerTrackYearValue.text = track.releaseDate.substring(0, 4)
        binding.playerTrackGenreValue.text = track.primaryGenreName
        binding.playerTrackCountryValue.text = track.country
        setFavoriteButtonIcon(track.isFavorite)
        viewModel.prepare(track.previewUrl)

        binding.playerBtnPlay.setOnClickListener{
            viewModel.playbackControl()
        }

        binding.playerBtnLikeIco.setOnClickListener {
            viewModel.onClickFavorite(track)
        }

        viewModel.getStatePlayerView().observe(this){ state ->
            binding.playerBtnPlay.isEnabled = state.isPlayButtonEnabled
            setPlayButtonIcon(state.buttonType)
            binding.playerCurrentTime.text = state.progress
        }

        viewModel.getStateFavorite().observe(this){ state ->
            setFavoriteButtonIcon(state)
        }
    }

    private fun setPlayButtonIcon(iconType: Boolean) {
        if (iconType) {
            binding.playerBtnPlay.setImageResource(R.drawable.button_play)
        } else {
            binding.playerBtnPlay.setImageResource(R.drawable.button_pause)
        }
    }

    private fun setFavoriteButtonIcon(iconType: Boolean) {
        if (iconType) {
            binding.playerBtnLikeIco.setImageResource(R.drawable.button_like_active)
        }
        else {
            binding.playerBtnLikeIco.setImageResource(R.drawable.button_like)
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
