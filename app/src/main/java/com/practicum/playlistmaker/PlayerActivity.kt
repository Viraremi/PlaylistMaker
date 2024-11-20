package com.practicum.playlistmaker

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

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
            .into(artView)
        trackNameView.text = track.trackName
        trackArtistView.text = track.artistName
        infoTimeView.text = track.timeValidFormat()
        infoAlbumView.text = track.collectionName
        infoYearView.text = track.releaseDate.toString()
        infoGenreView.text = track.primaryGenreName
        infoCountryView.text = track.country
    }
}