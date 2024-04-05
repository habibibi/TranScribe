package com.ikp.transcribe.auth.data

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.ikp.transcribe.R
import com.ikp.transcribe.auth.ui.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthService : Service() {
    private val authRepository: AuthRepository by lazy {
        AuthRepository(applicationContext)
    }

    private var job: Job? = null
    private lateinit var coroutineScope: CoroutineScope

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!this::coroutineScope.isInitialized) startTokenCheck()
        return START_STICKY
    }

    override fun onDestroy() {
        if(this::coroutineScope.isInitialized) coroutineScope.cancel()
        super.onDestroy()
    }

    private fun startTokenCheck() {
        coroutineScope = CoroutineScope(Dispatchers.Default)
        job = coroutineScope.launch {
            while (isActive) {
                Log.d("token","checking")
                val result = authRepository.checkToken()
                Log.i("service", result.toString())
                if (result is Result.Error) {
                    logout()
                    break
                }
                delay(TimeUnit.SECONDS.toMillis(10))
            }
        }
    }

    private fun logout() {
        stopSelf()

        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("email").remove("token").apply()

        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

        val msg = getString(R.string.relogin)
        Handler(mainLooper).post {
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
