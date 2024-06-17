package com.bangkit.sostainable.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bangkit.sostainable.R
import com.bangkit.sostainable.data.factory.AuthModelFactory
import com.bangkit.sostainable.databinding.ActivityMainBinding
import com.bangkit.sostainable.view.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val loginSession: MainViewModel by viewModels {
        AuthModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        checkLogin()
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.bottomNavigationView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(navView, navController)
    }

    private fun checkLogin() {
        loginSession.getSession().observe(this) { result ->
            if (result.token.isNullOrEmpty()) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}