package com.example.chattutorial

import android.app.Application
import com.example.chattutorial.data.Auth
import com.example.chattutorial.data.toVideoUser
import io.getstream.android.push.firebase.FirebasePushDeviceGenerator
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.notifications.NotificationConfig

class TutorialApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val apiKey = Auth.apiKey
        val user = Auth.currentUser.toVideoUser()
        val token = Auth.currentUser.token

        StreamVideoBuilder(
            context = applicationContext,
            apiKey = apiKey,
            user = user,
            token = token,
            notificationConfig = NotificationConfig(
                pushDeviceGenerators = listOf(
                    FirebasePushDeviceGenerator(providerName = "firebase"),
                ),
            ),
        ).build()
    }
}