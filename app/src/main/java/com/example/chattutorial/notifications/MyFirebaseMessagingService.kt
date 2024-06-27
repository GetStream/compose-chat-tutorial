package com.example.chattutorial.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.getstream.android.push.firebase.FirebaseMessagingDelegate

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        /** Used as a configuration [providerName] for the Firebase config on Stream website. */
        // This field value should be equal to the configuration name on your stream Dashboard.
        const val FIREBASE_CONFIG_NAME_ON_DASHBOARD = "firebase"
    }

    override fun onNewToken(token: String) {
        // Update device's token on Stream backend
        try {
            FirebaseMessagingDelegate.registerFirebaseToken(
                token,
                providerName = FIREBASE_CONFIG_NAME_ON_DASHBOARD
            )
        } catch (exception: IllegalStateException) {
            // StreamVideo was not initialized
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        try {
            if (!FirebaseMessagingDelegate.handleRemoteMessage(message)) {
                // RemoteMessage was not for stream and needs further processing
                Log.d("Firebase", "PN was not for stream")
            }
        } catch (exception: IllegalStateException) {
            // StreamVideo was not initialized, you can do nothing here, about Stream SDK
            // Maybe log some errors
        }
    }
}