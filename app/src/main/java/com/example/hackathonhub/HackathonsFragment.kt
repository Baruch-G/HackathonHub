package com.example.hackathonhub
import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hackathonhub.adapter.HackathonAdapter
import com.example.hackathonhub.api.LikeRequest
import com.example.hackathonhub.api.RetrofitClient
import com.example.hackathonhub.models.Hackathon
import kotlinx.coroutines.launch

class HackathonsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var hackathonAdapter: HackathonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hackathons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch data from the API
        fetchHackathons()
    }

    private fun fetchHackathons() {
        lifecycleScope.launch {
            try {
                val hackathons = RetrofitClient.apiService.getHackathons()
                hackathonAdapter = HackathonAdapter(
                    hackathons,
                    onLikeClick = { hackathon -> handleLikeClick(hackathon) },
                    onCommentClick = { hackathon -> /* Handle comment */ },
                    onDeleteClick = { hackathon -> /* Handle delete */ }
                )
                recyclerView.adapter = hackathonAdapter
            } catch (e: Exception) {
                Log.d("HA - ", e.message ?: "Error fetching hackathons")
            }
        }
    }
    fun getCurrentUserId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }
    private fun handleLikeClick(hackathon: Hackathon) {
        val userId = getCurrentUserId(requireContext())
        lifecycleScope.launch {
            try {
                val call = RetrofitClient.apiService.likeHackathon(
                    hackathon._id,
                    LikeRequest(userId ?: "5e0ea607832df0473cacacdc")
                )

                // Enqueue the request
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            // Update UI with new like count
                            val updatedList = hackathonAdapter.hackathons.toMutableList()
                            val index = updatedList.indexOfFirst { it._id == hackathon._id }
                            if (index != -1) {
                                val updatedHackathon = updatedList[index]
                                val updatedLikes = if (updatedHackathon.likes.contains(userId)) {
                                    // Unlike
                                    updatedHackathon.likes.filter { it != userId }
                                } else {
                                    // Like
                                    updatedHackathon.likes + (userId ?: "5e0ea607832df0473cacacdc")
                                }
                                updatedHackathon.likes = updatedLikes
                                updatedList[index] = updatedHackathon

                                // Update the adapter with the new list
                                hackathonAdapter = HackathonAdapter(
                                    updatedList,
                                    onLikeClick = { updatedHackathon -> handleLikeClick(updatedHackathon) },
                                    onCommentClick = { updatedHackathon -> /* Handle comment */ },
                                    onDeleteClick = { updatedHackathon -> /* Handle delete */ }
                                )
                                recyclerView.adapter = hackathonAdapter
                            }
                        } else {
                            Log.d("HA - ", "Error liking hackathon: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("HA - ", "Failure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d("HA - ", "Exception: ${e.message}")
            }
        }
    }


}
