package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

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
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis.toLong())
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_image_corner)))
            .centerCrop()
            .into(trackImage)
    }
}