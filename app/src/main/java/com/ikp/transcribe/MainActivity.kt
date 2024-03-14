package com.ikp.transcribe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ikp.transcribe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNav : BottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.nav_host)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_transaction, R.id.navigation_scan, R.id.navigation_chart, R.id.navigation_setting
        ))
        binding.toolbar.setupWithNavController(navController,appBarConfiguration)
        bottomNav.setupWithNavController(navController)
    }
}