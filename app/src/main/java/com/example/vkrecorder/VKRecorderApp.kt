package com.example.vkrecorder

import android.app.Application
import com.example.vkrecorder.vk_auth.presentation.WelcomeActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VKRecorderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            WelcomeActivity.startFrom(this@VKRecorderApp)
        }
    }
}