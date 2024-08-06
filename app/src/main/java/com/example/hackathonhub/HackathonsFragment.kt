package com.example.hackathonhub

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
import com.example.hackathonhub.api.RetrofitClient
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
                    onLikeClick = { hackathon -> /* Handle like */ },
                    onCommentClick = { hackathon -> /* Handle comment */ }
                )
                recyclerView.adapter = hackathonAdapter
            } catch (e: Exception) {
                Log.d("HA - ", e.message ?: "dds");
            }
        }
    }
}
