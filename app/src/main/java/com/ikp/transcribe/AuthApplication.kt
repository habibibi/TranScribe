package com.ikp.transcribe

import android.app.Application
import com.ikp.transcribe.data.AppContainer
import com.ikp.transcribe.data.DefaultAppContainer

class AuthApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}