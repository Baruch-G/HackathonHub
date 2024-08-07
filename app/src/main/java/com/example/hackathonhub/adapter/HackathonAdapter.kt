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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class HackathonAdapter(
    public val hackathons: List<Hackathon>,
    private val onLikeClick: (Hackathon) -> Unit,
    private val onCommentClick: (Hackathon) -> Unit,
    private val onDeleteClick: (Hackathon) -> Unit
) : RecyclerView.Adapter<HackathonAdapter.HackathonViewHolder>() {

    inner class HackathonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hackathonImage: ImageView = itemView.findViewById(R.id.hackathon_image)
        val hackathonDescription: TextView = itemView.findViewById(R.id.hackathon_description)
        val hackathonLocation: TextView = itemView.findViewById(R.id.hackathon_location)
        val userAvatar: ImageView = itemView.findViewById(R.id.user_avatar)
        val hackathonDateRange: TextView = itemView.findViewById(R.id.hackathon_date_range)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val likeCount: TextView = itemView.findViewById(R.id.like_count)

        init {
            itemView.findViewById<View>(R.id.like_button).setOnClickListener {
                val hackathon = hackathons[adapterPosition]
                onLikeClick(hackathon)
            }
            itemView.findViewById<View>(R.id.comment_button).setOnClickListener {
                onCommentClick(hackathons[adapterPosition])
            }
            itemView.findViewById<View>(R.id.delete_button).setOnClickListener {
                onDeleteClick(hackathons[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HackathonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hackathon, parent, false)
        return HackathonViewHolder(view)
    }

    override fun onBindViewHolder(holder: HackathonViewHolder, position: Int) {
        val hackathon = hackathons[position]

        // Format dates
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMM yy", Locale.getDefault())

        val startDate = inputFormat.parse(hackathon.startDate)
        val endDate = inputFormat.parse(hackathon.endDate)

        val dateRange = if (startDate != null && endDate != null) {
            "${outputFormat.format(startDate)} - ${outputFormat.format(endDate)}"
        } else {
            "Invalid Date Range"
        }

        // Bind data to views
        holder.hackathonDescription.text = hackathon.description
        holder.hackathonLocation.text = hackathon.location
        holder.hackathonDateRange.text = dateRange
        holder.userName.text = "${hackathon.creator.firstName} ${hackathon.creator.lastName}"
        holder.likeCount.text = "${hackathon.likes.size}"

        // Load images
        Glide.with(holder.itemView.context)
            .load(hackathon.imgs.firstOrNull() ?: "https://images.pexels.com/photos/2990605/pexels-photo-2990605.jpeg?auto=compress&cs=tinysrgb&w=600")
            .placeholder(R.drawable.no_image)
            .error(R.drawable.no_image)
            .into(holder.hackathonImage)

        Glide.with(holder.itemView.context)
            .load("https://lh3.googleusercontent.com/ogw/AF2bZygEa2-X4saKIzV77knVNUldkA89-XQnQGgyTttUw2uOXww=s32-c-mo" ?: hackathon.creator.imgUrl)
            .placeholder(R.drawable.no_image)
            .error(R.drawable.ic_error)
            .into(holder.userAvatar)
    }

    override fun getItemCount(): Int = hackathons.size
}
