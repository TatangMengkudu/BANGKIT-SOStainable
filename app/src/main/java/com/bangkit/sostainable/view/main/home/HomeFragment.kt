package com.bangkit.sostainable.view.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.sostainable.R
import com.bangkit.sostainable.data.factory.AuthModelFactory
import com.bangkit.sostainable.data.factory.EventModelFactory
import com.bangkit.sostainable.data.remote.response.event.DataItem
import com.bangkit.sostainable.databinding.FragmentHomeBinding
import com.bangkit.sostainable.view.login.LoginActivity
import com.bangkit.sostainable.view.main.MainViewModel
import com.bangkit.sostainable.view.main.bookmark.BookmarkActivity
import com.bangkit.sostainable.view.main.donate.historyDonate.HistoryDonateActivity
import com.bangkit.sostainable.view.main.home.adapter.HomeAdapter
import com.bangkit.sostainable.view.main.home.adapter.LoadingStateAdapter
import com.bangkit.sostainable.view.main.joinVolunter.JoinVolunterActivity
import com.bangkit.sostainable.view.main.myEvent.MyEventActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels {
        EventModelFactory.getInstance(requireContext())
    }
    private val userSession: MainViewModel by viewModels {
        AuthModelFactory.getInstance(requireContext())
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

        setMenuAppBar()
        checkLogin()
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

    private fun setMenuAppBar() {
        binding.topAppBar.setOnMenuItemClickListener{ menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_bookmark -> {
                    startActivity(Intent(requireContext(), BookmarkActivity::class.java))
                    true
                }
                R.id.navigation_event -> {
                    startActivity(Intent(requireContext(), MyEventActivity::class.java))
                    true
                }
                R.id.navigation_joinEvent -> {
                    startActivity(Intent(requireContext(), JoinVolunterActivity::class.java))
                    true
                }
                R.id.navigation_historyEvent -> {
                    startActivity(Intent(requireContext(), HistoryDonateActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun showSelectedEvent(event: DataItem){
        Toast.makeText(requireContext(), event.judulEvent, Toast.LENGTH_SHORT).show()
    }

    private fun checkLogin() {
        userSession.getSession().observe(viewLifecycleOwner) { result ->
            if (result.token.isNullOrEmpty()) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }
}