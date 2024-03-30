package com.ikp.transcribe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ikp.transcribe.auth.data.AuthService
import com.ikp.transcribe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val authServiceIntent = Intent(this, AuthService::class.java)
        startService(authServiceIntent)
        setContentView(binding.root)
        setupNavigation()
    }

    private fun setupNavigation() {
        val bottomNav: BottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.nav_host)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_transaction, R.id.navigation_scan, R.id.navigation_chart, R.id.navigation_setting
        ))
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)
    }
}
