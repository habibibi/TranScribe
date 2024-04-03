package com.ikp.transcribe

import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.ikp.transcribe.auth.data.AuthService
import com.ikp.transcribe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var connectivityManager : ConnectivityManager
    private lateinit var networkStatusLiveData : MutableLiveData<Boolean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authServiceIntent = Intent(this, AuthService::class.java)
        startService(authServiceIntent)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        networkStatusLiveData = MutableLiveData()
        networkStatusLiveData.value = true
        connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private val networkCallback = object : NetworkCallback() {
        override fun onAvailable(network : Network) {
            handleNetworkAvailable()
        }

        override fun onLost(network : Network) {
            handleNetworkLost()
        }
    }

    private fun handleNetworkAvailable() {
        val authServiceIntent = Intent(this, AuthService::class.java)
        if (networkStatusLiveData.value == false) {
            startService(authServiceIntent)
            Snackbar.make(binding.bottomNavigation, "Koneksi tersambung kembali.", Snackbar.LENGTH_LONG)
                .setAnchorView(binding.bottomNavigation).show()
            networkStatusLiveData.postValue(true)
        }
    }

    private fun handleNetworkLost() {
        val authServiceIntent = Intent(this, AuthService::class.java)
        if (networkStatusLiveData.value == true){
            stopService(authServiceIntent)
            Snackbar.make(binding.bottomNavigation, "Koneksi terputus. Beberapa fitur dimatikan.", Snackbar.LENGTH_LONG)
                .setAnchorView(binding.bottomNavigation).show()
            networkStatusLiveData.postValue(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
    fun getNetworkStatusLiveData(): LiveData<Boolean> {
        return networkStatusLiveData
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
