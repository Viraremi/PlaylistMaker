package com.practicum.playlistmaker.player.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.domain.model.Playlist

class AddTrackAdapter(
    private val playlists: List<Playlist>
) : RecyclerView.Adapter<AddTrackAdapter.AddTrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddTrackViewHolder {
        return AddTrackViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_playlist_player, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: AddTrackViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    class AddTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistName: TextView
        val tracksCount: TextView
        val playlistImage: ImageView
        init {
            playlistName = itemView.findViewById(R.id.player_playlist_name)
            tracksCount = itemView.findViewById(R.id.player_playlist_count)
            playlistImage = itemView.findViewById(R.id.player_playlist_img)
        }

        fun bind(model: Playlist){
            playlistName.text = model.name
            tracksCount.text = countString(model.tracksCount)
            Glide.with(itemView)
                .load(model.imgPath)
                .placeholder(R.drawable.placeholder_medium)
                .transform(RoundedCorners(2))
                .centerCrop()
                .into(playlistImage)
        }

        private fun countString(count: Int): String {
            return when (count) {
                1 -> "$count трек"
                in 2..4 -> "$count трека"
                else -> "$count треков"
            }
        }
    }
}