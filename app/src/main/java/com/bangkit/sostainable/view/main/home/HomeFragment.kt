package com.bangkit.sostainable.view.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.sostainable.data.factory.EventModelFactory
import com.bangkit.sostainable.data.remote.response.event.DataItem
import com.bangkit.sostainable.databinding.FragmentHomeBinding
import com.bangkit.sostainable.view.main.home.adapter.HomeAdapter
import com.bangkit.sostainable.view.main.home.adapter.LoadingStateAdapter

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels {
        EventModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getEvents()
    }

    private fun getEvents() {
        adapter = HomeAdapter()
        binding.rvEvents.layoutManager = LinearLayoutManager(context)
        binding.rvEvents.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        homeViewModel.event.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
        adapter.setOnItemClickCallback(object : HomeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItem) {
                showSelectedEvent(data)
            }
        })
    }

    private fun showSelectedEvent(event: DataItem){
        Toast.makeText(requireContext(), event.judulEvent, Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}