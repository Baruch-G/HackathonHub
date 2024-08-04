package com.example.hackathonhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hackathonhub.R
import com.example.hackathonhub.models.Hackathon

class HackathonAdapter(
    private val hackathons: List<Hackathon>,
    private val onLikeClick: (Hackathon) -> Unit,
    private val onCommentClick: (Hackathon) -> Unit
) : RecyclerView.Adapter<HackathonAdapter.HackathonViewHolder>() {

    inner class HackathonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hackathonImage: ImageView = itemView.findViewById(R.id.hackathon_image)
        val hackathonDescription: TextView = itemView.findViewById(R.id.hackathon_description)
        val hackathonLocation: TextView = itemView.findViewById(R.id.hackathon_location)
        val userAvatar: ImageView = itemView.findViewById(R.id.user_avatar)
        val hackathonDateRange: TextView = itemView.findViewById(R.id.hackathon_date_range)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        // Add more views

        init {
            // Uncomment and use these lines when you have like and comment buttons in your layout
            // itemView.findViewById<View>(R.id.like_button).setOnClickListener {
            //     onLikeClick(hackathons[adapterPosition])
            // }
            // itemView.findViewById<View>(R.id.comment_button).setOnClickListener {
            //     onCommentClick(hackathons[adapterPosition])
            // }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HackathonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hackathon, parent, false)
        return HackathonViewHolder(view)
    }

    override fun onBindViewHolder(holder: HackathonViewHolder, position: Int) {
        val hackathon = hackathons[position]
        // Bind data to views
        holder.hackathonDescription.text = hackathon.description
        holder.hackathonLocation.text = hackathon.location
        holder.hackathonDateRange.text = "${hackathon.startDate} - ${hackathon.endDate}"
        holder.userName.text = "${hackathon.creator.firstName} ${hackathon.creator.firstName}"

        Glide.with(holder.itemView.context)
            .load(hackathon.img)
            .placeholder(R.drawable.no_image) // Add a placeholder image in your drawable folder
            .error(R.drawable.ic_error) // Add an error image in your drawable folder
            .into(holder.hackathonImage)

        Glide.with(holder.itemView.context)
            .load(hackathon.creator.imgUrl)
            .placeholder(R.drawable.profile_icon) // Add a placeholder image in your drawable folder
            .error(R.drawable.ic_error) // Add an error image in your drawable folder
            .into(holder.userAvatar)
    }

    override fun getItemCount(): Int = hackathons.size
}
