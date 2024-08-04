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
        val profileImg: ImageView = itemView.findViewById(R.id.hackathon_image)
        val hackathonName: TextView = itemView.findViewById(R.id.hackathon_title)
        val hackathonLocation: TextView = itemView.findViewById(R.id.hackathon_location)
        // Add more views

        init {
//            itemView.findViewById<View>(R.id.like_button).setOnClickListener {
//                onLikeClick(hackathons[adapterPosition])
//            }
//            itemView.findViewById<View>(R.id.comment_button).setOnClickListener {
//                onCommentClick(hackathons[adapterPosition])
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HackathonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hackathon, parent, false)
        return HackathonViewHolder(view)
    }

    override fun onBindViewHolder(holder: HackathonViewHolder, position: Int) {
        val hackathon = hackathons[position]
        // Bind data to views
        holder.hackathonName.text = hackathon.description
        holder.hackathonLocation.text = hackathon.location
        // Load image into profileImg using an image loading library
    }

    override fun getItemCount(): Int = hackathons.size
}
