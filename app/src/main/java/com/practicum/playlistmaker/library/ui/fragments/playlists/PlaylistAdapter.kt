package com.practicum.playlistmaker.library.ui.fragments.playlists

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
import com.practicum.playlistmaker.util.StringFormatter

class PlaylistAdapter(
    private val playlists: List<Playlist>
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_playlist, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistName: TextView
        val tracksCount: TextView
        val playlistImage: ImageView
        init {
            playlistName = itemView.findViewById(R.id.playlist_item_name)
            tracksCount = itemView.findViewById(R.id.playlist_item_count)
            playlistImage = itemView.findViewById(R.id.playlist_item_img)
        }

        fun bind(model: Playlist){
            playlistName.text = model.name
            tracksCount.text = StringFormatter.countString(model.tracksCount)
            Glide.with(itemView)
                .load(model.imgPath)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder_medium)
                        .transform(RoundedCorners(8))
                )
                .into(playlistImage)
        }
    }
}