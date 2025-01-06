package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
    }
}