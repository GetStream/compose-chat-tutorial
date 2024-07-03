package com.example.chattutorial

import android.app.Application
import com.example.chattutorial.data.Auth
import com.example.chattutorial.data.toChatUser
import com.example.chattutorial.data.toVideoUser
import com.example.chattutorial.notifications.MyFirebaseMessagingService
import io.getstream.android.push.firebase.FirebasePushDeviceGenerator
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.notifications.NotificationConfig

class TutorialApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initChatClient()
        initVideoClient()
    }

    private fun initChatClient() {
        // 1 - Set up the OfflinePlugin for offline storage
        val offlinePluginFactory = StreamOfflinePluginFactory(
            appContext = applicationContext,
        )
        val statePluginFactory = StreamStatePluginFactory(
            config = StatePluginConfig(
                backgroundSyncEnabled = true,
                userPresence = true,
            ),
            appContext = this,
        )

        // 2 - Set up the client for API calls and with the plugin for offline storage
        val client = ChatClient.Builder("hm6usc9wx2wv", applicationContext)
            .notifications(
                notificationConfig = io.getstream.chat.android.client.notifications.handler.NotificationConfig(
                    ignorePushMessagesWhenUserOnline = false,
                    pushDeviceGenerators = listOf(FirebasePushDeviceGenerator(providerName = MyFirebaseMessagingService.FIREBASE_CONFIG_NAME_ON_DASHBOARD))
                )
            )
            .withPlugins(offlinePluginFactory, statePluginFactory)
            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
            .build()

        // 3 - Authenticate and connect the user
        val user = Auth.currentUser
        client.connectUser(user.toChatUser(), user.token).enqueue()
    }

    private fun initVideoClient() {
        val apiKey = "hm6usc9wx2wv"
        val user = Auth.currentUser.toVideoUser()
        val token = Auth.currentUser.token

        StreamVideoBuilder(
            context = applicationContext,
            apiKey = apiKey,
            user = user,
            token = token,
            notificationConfig = NotificationConfig(
                // Make the notification low prio if the app is in foreground, so its not visible as a popup, since we want to handle
                // the incoming call in full screen when app is running.
                hideRingingNotificationInForeground = false,
                // Make sure that the provider name is equal to the "Name" of the configuration in Stream Dashboard.
                pushDeviceGenerators = listOf(FirebasePushDeviceGenerator(providerName = MyFirebaseMessagingService.FIREBASE_CONFIG_NAME_ON_DASHBOARD))
            ),
        ).build()
    }
}
