package com.practicum.playlistmaker.search.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.TimeFormatter

class TrackAdapter(
    private val tracks: List<Track>,
    private val click: (Track) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{
            click(tracks[position])
        }
    }

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackName: TextView
        val artistName: TextView
        val trackTime: TextView
        val trackImage: ImageView
        init {
            trackName = itemView.findViewById(R.id.track_name)
            artistName = itemView.findViewById(R.id.track_artist)
            trackTime = itemView.findViewById(R.id.track_time)
            trackImage = itemView.findViewById(R.id.track_image)
        }

        fun bind(model: Track){
            trackName.text = model.trackName
            artistName.text = model.artistName
            trackTime.text = TimeFormatter.getValidTimeFormat(model.trackTimeMillis.toLong())
            Glide.with(itemView)
                .load(model.getCoverArtwork())
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_image_corner)))
                .centerCrop()
                .into(trackImage)
        }
    }
}