package com.practicum.playlistmaker.player.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.ui.model.PlayerViewState
import com.practicum.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.practicum.playlistmaker.search.ui.activity.SearchFragment
import com.practicum.playlistmaker.util.TimeFormatter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    companion object {
        fun show(context: Context, trackId: Int) {
            val intent = Intent(context, PlayerActivity::class.java)
            context.startActivity(intent)
        }
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

        binding.playerBack.setOnClickListener{
            finish()
        }


        val trackId = intent.getIntExtra(SearchFragment.PLAYER_INTENT_KEY, -1)
        val track = viewModel.getTrackById(trackId)
        Glide.with(this)
            .load(track.getCoverArtwork())
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
        viewModel.prepare(track.previewUrl)

        binding.playerBtnPlay.setOnClickListener{
            viewModel.playbackControl()
        }

        viewModel.getStatePlayerView().observe(this){ state ->
            when(state){
                is PlayerViewState.Pause, PlayerViewState.Prepare -> {
                    binding.playerBtnPlay.setImageResource(R.drawable.button_play)
                }
                is PlayerViewState.Play -> {
                    binding.playerCurrentTime.text = state.data
                    binding.playerBtnPlay.setImageResource(R.drawable.button_pause)
                }
            }
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
